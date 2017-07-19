package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.MqConstant;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.cache.FanCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.FanMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.OpenIdMapper;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.fan.UserInfoDTO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.manager.WxFanManager;
import com.kuaizhan.kzweixin.mq.dto.FanDTO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.*;
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
    @Resource
    private MqUtil mqUtil;


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
    public FanPO getFanByOpenId(String appId, String openId) {
        String table = DBTableUtil.getFanTableName(appId);
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);

        List<FanPO> fanPOList = fanMapper.selectByExample(example, table);
        return fanPOList.isEmpty() ? null : fanPOList.get(0);
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

        // 新建
        if (updateFans.size() == 0) {
            OpenIdPO newUserPO = new OpenIdPO();
            newUserPO.setAppId(appId);
            newUserPO.setOpenId(openId);
            newUserPO.setStatus(1);
            newUserPO.setCreateTime(DateUtil.curSeconds());
            newUserPO.setUpdateTime(DateUtil.curSeconds());
            openIdMapper.insertSelective(newUserPO, table);
        // 已存在，修改
        } else {
            OpenIdPO oldUserPO = new OpenIdPO();
            oldUserPO.setId(updateFans.get(0).getId());
            oldUserPO.setStatus(1);
            oldUserPO.setUpdateTime(DateUtil.curSeconds());
            openIdMapper.updateByPrimaryKeySelective(oldUserPO, table);
        }
    }

    @Override
    public void refreshFan(String appId, String openId) {
        AccountPO accountPO = accountService.getAccountByAppId(appId);

        // 只有认证的公众号才能获取粉丝信息
        if (accountPO == null || accountPO.getVerifyType() != 0) {
            return;
        }

        String accessToken = accountService.getAccessToken(accountPO.getWeixinAppid());
        UserInfoDTO userInfoDTO = WxFanManager.getFanInfo(accessToken, openId);

        // 未关注状态
        if (userInfoDTO.getSubscribe() != 1) {
            return;
        }

        //从微信服务器接收并转换的部分
        FanPO fanPO = new FanPO();
        fanPO.setAppId(appId);
        fanPO.setOpenId(openId);
        fanPO.setStatus(userInfoDTO.getSubscribe() == 1 ? 1 : 2);
        String nickname = StrUtil.removeEmojis(userInfoDTO.getNickName());
        nickname = StrUtil.chopStr(nickname, 45);
        fanPO.setNickName(nickname);
        fanPO.setCity(userInfoDTO.getCity());
        fanPO.setProvince(userInfoDTO.getProvince());
        fanPO.setCountry(userInfoDTO.getCountry());
        fanPO.setHeadImgUrl(userInfoDTO.getHeadImgUrl());
        fanPO.setGroupId(userInfoDTO.getGroupId());
        fanPO.setLanguage(userInfoDTO.getLanguage());
        fanPO.setRemark(userInfoDTO.getRemark());
        String unionId = userInfoDTO.getUnionId() == null? "": userInfoDTO.getUnionId();
        fanPO.setUnionId(unionId);
        fanPO.setSex(userInfoDTO.getSex());
        fanPO.setSubscribeTime(userInfoDTO.getSubscribeTime());
        fanPO.setTagIdsJson(userInfoDTO.getTagIdsList().toString());

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId);
        String table = DBTableUtil.getFanTableName(appId);
        List<FanPO> fanPOList = fanMapper.selectByExample(example, table);

        //自定义部分
        fanPO.setUpdateTime(DateUtil.curSeconds());
        fanPO.setInBlacklist(userInfoDTO.getGroupId() == 1 ? 1 : 0);
        fanPO.setLastInteractTime(DateUtil.curSeconds());

        if (fanPOList.size() != 0) {
            fanPO.setFanId(fanPOList.get(0).getFanId());
            fanMapper.updateByPrimaryKeySelective(fanPO, table);
        } else {
            fanPO.setCreateTime(DateUtil.curSeconds());
            fanMapper.insertSelective(fanPO, table);
        }
    }

    @Override
    public void delFanOpenId(String appId, String openId) {
        OpenIdPO record = new OpenIdPO();
        record.setStatus(2);
        record.setUpdateTime(DateUtil.curSeconds());

        OpenIdPOExample example = new OpenIdPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);

        String table = DBTableUtil.getOpenIdTableName(appId);
        openIdMapper.updateByExampleSelective(record, example, table);

        fanCache.deleteFan(appId, openId);
    }

    @Override
    public void delFan(String appId, String openId) {
        FanPO record = new FanPO();
        record.setStatus(2);
        record.setUpdateTime(DateUtil.curSeconds());

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);
        String table = DBTableUtil.getFanTableName(appId);
        fanMapper.updateByExampleSelective(record, example, table);

        fanCache.deleteFan(appId, openId);
    }

    @Override
    public boolean isSubscribe(String appId, String openId) {
        // 从缓存
        if (fanCache.getSubscribeStatus(appId, openId) != null) {
            return true;
        }

        // 从数据库
        OpenIdPOExample example = new OpenIdPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);
        String table = DBTableUtil.getOpenIdTableName(appId);
        List<OpenIdPO> openIdPOS = openIdMapper.selectByExample(example, table);

        // 未关注
        if (openIdPOS.size() == 0) {
            // TODO: 量大了，小心缓存击穿
            return false;
        }

        // 已关注，更新缓存
        fanCache.setSubscribeStatus(appId, openId);
        return true;
    }

    @Override
    public void asyncAddFan(String appId, String openId) {
        FanDTO fanDTO = new FanDTO();
        fanDTO.setAppId(appId);
        fanDTO.setOpenId(openId);

        mqUtil.publish(MqConstant.FAN_SUBSCRIBE, JsonUtil.bean2String(fanDTO));
    }

    @Override
    public void asyncUpdateFan(String appId, String openId) {
        FanDTO fanDTO = new FanDTO();
        fanDTO.setAppId(appId);
        fanDTO.setOpenId(openId);

        mqUtil.publish(MqConstant.FAN_UPDATE, JsonUtil.bean2String(fanDTO));
    }

    @Override
    public void asyncDeleteFan(String appId, String openId) {
        FanDTO fanDTO = new FanDTO();
        fanDTO.setAppId(appId);
        fanDTO.setOpenId(openId);

        mqUtil.publish(MqConstant.FAN_UNSUBSCRIBE, JsonUtil.bean2String(fanDTO));
    }

    @Override
    public void refreshInteractionTime(String appId, String openId) {
        FanPO record = new FanPO();
        record.setLastInteractTime(DateUtil.curSeconds());

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);
        String table = DBTableUtil.getFanTableName(appId);
        fanMapper.updateByExampleSelective(record, example, table);
    }
}
