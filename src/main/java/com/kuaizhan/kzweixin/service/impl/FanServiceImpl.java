package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.cache.FanCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.FanMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.OpenIdMapper;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.fan.UserInfoDTO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.manager.WxFanManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.DBTableUtil;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 粉丝模块服务接口
 * Created by fangtianyu on 6/15/17.
 */
@Service("fanService")
public class FanServiceImpl implements FanService {

    @Resource
    private FanDao fanDao;
    @Resource
    private FanCache fanCache;
    @Resource
    private FanMapper fanMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private OpenIdMapper openIdMapper;


    @Override
    public int createTag(long weixinAppid, String tagName) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        int tagId = WxFanManager.createTag(accessToken, tagName);
        fanCache.deleteTags(weixinAppid);
        return tagId;
    }

    @Override
    public List<TagDTO> getTags(long weixinAppid) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        List<TagDTO> tagsList;

        //先从Redis缓存查找数据
        tagsList = fanCache.getTags(weixinAppid);
        if (tagsList != null) {
            return tagsList;
        }

        //如果缓存没找到，再去微信后台请求，拿到数据后存入Redis缓存
        tagsList = WxFanManager.getTags(accessToken);
        fanCache.setTag(weixinAppid, tagsList);
        return tagsList;
    }

    @Override
    public void updateTag(long weixinAppid, int tagId, String tagName) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        WxFanManager.updateTag(accessToken, tagId, tagName);
        fanCache.deleteTags(weixinAppid);
    }

    @Override
    public void deleteTag(long weixinAppid, int tagId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        WxFanManager.deleteTag(accessToken, tagId);
        fanCache.deleteTags(weixinAppid);

        //删除数据库对应标签下的粉丝信息
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andTagIdsJsonLike("%" + tagId + "%");
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());
        List<FanPO> taggedFans = fanMapper.selectByExample(example, table);

        //比较并删除粉丝拥有的已被删除的标签，同时更新到数据库
        for (FanPO fan: taggedFans) {
            List<Integer> originalTagsList = JsonUtil.string2List(fan.getTagIdsJson(), Integer.class);
            List<Integer> tagsList = new ArrayList<>();
            for (int curr: originalTagsList) {
                if (curr != tagId) {
                    tagsList.add(curr);
                }
            }
            FanPO fanPO = new FanPO();
            fanPO.setFanId(fan.getFanId());
            fanPO.setTagIdsJson(JsonUtil.list2Str(tagsList));
            fanPO.setUpdateTime(DateUtil.curSeconds());
            fanMapper.updateByPrimaryKeySelective(fanPO, table);
        }
    }

    @Override
    public FanPO getFanByOpenId(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();
        String table = DBTableUtil.getFanTableName(appId);
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId);
        return fanMapper.selectByExample(example, table).get(0);
    }

    @Override
    public void addFanTag(long weixinAppid, List<String> fansOpenId, List<Integer> newTagsId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        if (newTagsId == null) {
            return;
        }

        //发送给粉丝贴标签请求
        for (int tagId: newTagsId) {
            WxFanManager.addFanTag(accessToken, fansOpenId, tagId);
        }

        //更新粉丝标签信息到本地数据库
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdIn(fansOpenId);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());
        List<FanPO> updateFans = fanMapper.selectByExample(example, table);

        for (FanPO fan: updateFans) {
            List<Integer> tagsList = JsonUtil.string2List(fan.getTagIdsJson(), Integer.class);
            tagsList.addAll(newTagsId);
            Collections.sort(tagsList);

            FanPO fanPO = new FanPO();
            fanPO.setFanId(fan.getFanId());
            fanPO.setTagIdsJson(JsonUtil.list2Str(tagsList));
            fanPO.setUpdateTime(DateUtil.curSeconds());
            fanMapper.updateByPrimaryKeySelective(fanPO, table);
        }

    }

    @Override
    public void deleteFanTag(long weixinAppid, List<String> fansOpenId, List<Integer> deleteTagsId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        if (deleteTagsId == null) {
            return;
        }

        for (int tagId: deleteTagsId) {
            WxFanManager.deleteFanTag(accessToken, fansOpenId, tagId);
        }

        //更新粉丝标签信息到本地数据库
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdIn(fansOpenId);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());
        List<FanPO> updateFans = fanMapper.selectByExample(example, table);

        for (FanPO fan: updateFans) {
            List<Integer> tagsList = JsonUtil.string2List(fan.getTagIdsJson(), Integer.class);

            //比较并删除粉丝标签列表里需要删除的标签
            for (int curr: deleteTagsId) {
                for (int i = 0; i < tagsList.size(); i++) {
                    if (tagsList.get(i) == curr) {
                        tagsList.remove(i);
                        break;
                    }
                }
            }

            FanPO fanPO = new FanPO();
            fanPO.setFanId(fan.getFanId());
            fanPO.setTagIdsJson(JsonUtil.list2Str(tagsList));
            fanPO.setUpdateTime(DateUtil.curSeconds());
            fanMapper.updateByPrimaryKeySelective(fanPO, table);
        }

    }

    @Override
    public void addFanBlacklist(long weixinAppid, List<String> fansOpenId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        WxFanManager.addBlacklist(accessToken, fansOpenId);

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdIn(fansOpenId);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());
        List<FanPO> updateFans = fanMapper.selectByExample(example, table);

        for (FanPO fan: updateFans) {
            if (fan.getInBlacklist() == 0) {
                FanPO fanPO = new FanPO();
                fanPO.setFanId(fan.getFanId());
                fanPO.setInBlacklist(1);
                fanPO.setUpdateTime(DateUtil.curSeconds());
                fanMapper.updateByPrimaryKeySelective(fanPO, table);
            }
        }
    }

    @Override
    public void removeFanBlacklist(long weixinAppid, List<String> fansOpenId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        WxFanManager.removeBlacklist(accessToken, fansOpenId);

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdIn(fansOpenId);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());
        List<FanPO> updateFans = fanMapper.selectByExample(example, table);

        for (FanPO fan: updateFans) {
            if (fan.getInBlacklist() == 1) {
                FanPO fanPO = new FanPO();
                fanPO.setFanId(fan.getFanId());
                fanPO.setInBlacklist(0);
                fanPO.setUpdateTime(DateUtil.curSeconds());
                fanMapper.updateByPrimaryKeySelective(fanPO, table);
            }
        }
    }

    @Override
    public Page<FanPO> listFansByPage(long weixinAppid, int pageNum, int pageSize, List<Integer> tagIds, String queryStr, int isBlacklist) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        Page<FanPO> fanPage = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());

        long totalCount = fanDao.countFan(accountPO.getAppId(), isBlacklist, tagIds, queryStr, table);
        List<FanPO> fanPOList = fanDao.listFansByPage(accountPO.getAppId(), fanPage, isBlacklist, tagIds, queryStr, table);

        fanPage.setTotalCount(totalCount);
        fanPage.setResult(fanPOList);

        return fanPage;
    }

    @Override
    public void addFanOpenId(String appId, String openId) {
        OpenIdPOExample example = new OpenIdPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId);

        String table = DBTableUtil.getOpenIdTableName(appId);
        List<OpenIdPO> updateFans = openIdMapper.selectByExample(example, table);

        if (updateFans.size() == 0) {
            OpenIdPO newUserPO = new OpenIdPO();
            newUserPO.setAppId(appId);
            newUserPO.setOpenId(openId);
            newUserPO.setStatus(1);
            newUserPO.setCreateTime(DateUtil.curSeconds());
            newUserPO.setUpdateTime(DateUtil.curSeconds());
            openIdMapper.insertSelective(newUserPO, table);
        } else {
            OpenIdPO oldUserPO = new OpenIdPO();
            oldUserPO.setId(updateFans.get(0).getId());
            oldUserPO.setStatus(1);
            oldUserPO.setUpdateTime(DateUtil.curSeconds());
            openIdMapper.updateByPrimaryKeySelective(oldUserPO, table);
        }
    }

    @Override
    public void delFanOpenId(String appId, String openId) {
        OpenIdPO oldUserPO = new OpenIdPO();
        oldUserPO.setStatus(2);
        oldUserPO.setUpdateTime(DateUtil.curSeconds());

        OpenIdPOExample example = new OpenIdPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);

        String table = DBTableUtil.getOpenIdTableName(appId);
        openIdMapper.updateByExampleSelective(oldUserPO, example, table);

        //TODO:删除Redis缓存

    }

    @Override
    public FanPO addFan(long weixinAppid, String appId, String openId, boolean hasInteract) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        UserInfoDTO userInfoDTO = WxFanManager.getFanInfo(accessToken, openId);

        FanPO fanPO = new FanPO();

        //从微信服务器接收并转换的部分
        fanPO.setAppId(appId);
        fanPO.setOpenId(openId);
        fanPO.setStatus(userInfoDTO.getStatus());
        fanPO.setNickName(userInfoDTO.getNickName());
        fanPO.setCity(userInfoDTO.getCity());
        fanPO.setProvince(userInfoDTO.getProvince());
        fanPO.setCountry(userInfoDTO.getCountry());
        fanPO.setHeadImgUrl(userInfoDTO.getHeadImgUrl());
        fanPO.setGroupId(userInfoDTO.getGroupId());
        fanPO.setLanguage(userInfoDTO.getLanguage());
        fanPO.setRemark(userInfoDTO.getRemark());
        fanPO.setUnionId(userInfoDTO.getUnionId());
        fanPO.setSex(userInfoDTO.getSex());
        fanPO.setSubscribeTime(userInfoDTO.getSubscribeTime());
        fanPO.setTagIdsJson(userInfoDTO.getTagIdsJson());

        //自定义部分
        fanPO.setUpdateTime(DateUtil.curSeconds());
        fanPO.setInBlacklist(userInfoDTO.getGroupId() == 1 ? 1 : 0);
        fanPO.setLastInteractTime(hasInteract ? DateUtil.curSeconds() : 0);

        String table = DBTableUtil.getFanTableName(appId);
        fanMapper.updateByPrimaryKeySelective(fanPO, table);

        return fanPO;
    }

}
