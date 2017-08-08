package com.kuaizhan.kzweixin.service.impl;

import com.google.common.collect.ImmutableList;
import com.kuaizhan.kzweixin.constant.MqConstant;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.cache.FanCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.FanMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.OpenIdMapper;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.fan.UserInfoDTO;
import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.enums.WxAuthority;
import com.kuaizhan.kzweixin.manager.WxFanManager;
import com.kuaizhan.kzweixin.mq.dto.FanDTO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

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
        List<FanPO> taggedFans = fanMapper.selectByExample(example);

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
            fanMapper.updateByPrimaryKeySelective(fanPO);
        }
    }

    @Override
    public FanPO getFanByOpenId(String appId, String openId) {
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId)
                .andStatusEqualTo(1);

        List<FanPO> fanPOList = fanMapper.selectByExample(example);
        return fanPOList.isEmpty() ? null : fanPOList.get(0);
    }

    @Override
    public void addFanTag(long weixinAppid, List<String> openIds, List<Integer> newTagIds) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        if (newTagIds == null) {
            return;
        }

        //发送给粉丝贴标签请求
        for (int tagId: newTagIds) {
            WxFanManager.addFanTag(accessToken, openIds, tagId);
        }

        //更新粉丝标签信息到本地数据库
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdIn(openIds);
        List<FanPO> updateFans = fanMapper.selectByExample(example);

        for (FanPO fan: updateFans) {
            Set<Integer> tagSet = new HashSet<>();
            // 老的tags
            String tagIdsJson = fan.getTagIdsJson();
            if (StringUtils.isNotBlank(tagIdsJson)) {
                tagSet.addAll(JsonUtil.string2List(tagIdsJson, Integer.class));
            }
            // 新的tags
            tagSet.addAll(newTagIds);

            // 排序
            List<Integer> tagList = new ArrayList<>(tagSet);
            Collections.sort(tagList);

            FanPO fanPO = new FanPO();
            fanPO.setFanId(fan.getFanId());
            fanPO.setTagIdsJson(JsonUtil.list2Str(tagList));
            fanPO.setUpdateTime(DateUtil.curSeconds());
            fanMapper.updateByPrimaryKeySelective(fanPO);
        }

    }

    @Override
    public void deleteFanTag(long weixinAppid, List<String> openIds, List<Integer> deleteTagIds) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        if (deleteTagIds == null) {
            return;
        }

        for (int tagId: deleteTagIds) {
            WxFanManager.deleteFanTag(accessToken, openIds, tagId);
        }

        //更新粉丝标签信息到本地数据库
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdIn(openIds);
        List<FanPO> updateFans = fanMapper.selectByExample(example);

        for (FanPO fan: updateFans) {
            List<Integer> tagsList = JsonUtil.string2List(fan.getTagIdsJson(), Integer.class);

            //比较并删除粉丝标签列表里需要删除的标签
            for (int curr: deleteTagIds) {
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
            fanMapper.updateByPrimaryKeySelective(fanPO);
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

        FanPO record = new FanPO();
        record.setInBlacklist(1);
        record.setUpdateTime(DateUtil.curSeconds());
        fanMapper.updateByExampleSelective(record, example);
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

        FanPO record = new FanPO();
        record.setInBlacklist(0);
        record.setUpdateTime(DateUtil.curSeconds());
        fanMapper.updateByExampleSelective(record, example);
    }

    @Override
    public PageV2<FanPO> listFansByPage(long weixinAppid, int offset, int limit, List<Integer> tagIds,
                                        String queryStr, Boolean isBlacklist, Boolean hasInteract) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());

        //计算当前时间前48小时的时间，最近互动时间大于此时间的符合筛选条件
        Integer baseInteractTime = null;
        if (hasInteract) {
            baseInteractTime = DateUtil.curSeconds() - 48 * 3600;
        }
        List<FanPO> fanPOList = fanDao.listFansByPage(accountPO.getAppId(),
                offset,
                limit,
                baseInteractTime,
                isBlacklist ? 1: 0,
                CollectionUtils.isEmpty(tagIds) ? null: tagIds,
                StringUtils.isBlank(queryStr) ? null: queryStr,
                table);
        long totalCount = fanDao.countFan(accountPO.getAppId(),
                baseInteractTime,
                isBlacklist ? 1: 0,
                CollectionUtils.isEmpty(tagIds) ? null: tagIds,
                StringUtils.isBlank(queryStr) ? null: queryStr,
                table);

        return new PageV2<>(totalCount, fanPOList);
    }

    @Override
    public List<FanPO> listFansByOpenIds(long weixinAppid, List<String> openIds) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        // 未认证的返回空列表
        if (accountPO.getVerifyType() != 0 || CollectionUtils.isEmpty(openIds)) {
            return ImmutableList.of();
        }

        String tableName = DBTableUtil.getFanTableName(accountPO.getAppId());
        return fanDao.listFansByOpenIds(accountPO.getAppId(), openIds, tableName);
    }

    @Override
    public void addFanOpenId(String appId, String openId) {

        OpenIdPOExample example = new OpenIdPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId);
        List<OpenIdPO> updateFans = openIdMapper.selectByExample(example);

        // 新建
        if (updateFans.size() == 0) {
            OpenIdPO newUserPO = new OpenIdPO();
            newUserPO.setAppId(appId);
            newUserPO.setOpenId(openId);
            newUserPO.setStatus(1);
            newUserPO.setCreateTime(DateUtil.curSeconds());
            newUserPO.setUpdateTime(DateUtil.curSeconds());
            openIdMapper.insertSelective(newUserPO);
        // 已存在，修改
        } else {
            OpenIdPO oldUserPO = new OpenIdPO();
            oldUserPO.setId(updateFans.get(0).getId());
            oldUserPO.setStatus(1);
            oldUserPO.setUpdateTime(DateUtil.curSeconds());
            openIdMapper.updateByPrimaryKeySelective(oldUserPO);
        }
    }

    @Override
    public void refreshFan(String appId, String openId) {
        AccountPO accountPO = accountService.getAccountByAppId(appId);

        // 接口权限判断
        if (accountPO == null || !accountService.hasAuthority(WxAuthority.USER_MANAGEMENT, accountPO)) {
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
        String nickname = StrUtil.replaceUtf8mb4(userInfoDTO.getNickName());
        nickname = StrUtil.chopStr(nickname, 45);
        fanPO.setNickName(nickname);
        fanPO.setCity(StrUtil.replaceUtf8mb4(userInfoDTO.getCity()));
        fanPO.setProvince(StrUtil.replaceUtf8mb4(userInfoDTO.getProvince()));
        fanPO.setCountry(StrUtil.replaceUtf8mb4(userInfoDTO.getCountry()));
        fanPO.setHeadImgUrl(userInfoDTO.getHeadImgUrl());
        fanPO.setGroupId(userInfoDTO.getGroupId());
        fanPO.setLanguage(userInfoDTO.getLanguage());
        fanPO.setRemark(StrUtil.removeEmojis(userInfoDTO.getRemark()));
        String unionId = userInfoDTO.getUnionId() == null? "": userInfoDTO.getUnionId();
        fanPO.setUnionId(unionId);
        fanPO.setSex(userInfoDTO.getSex());
        fanPO.setSubscribeTime(userInfoDTO.getSubscribeTime());
        fanPO.setTagIdsJson(JsonUtil.list2Str(userInfoDTO.getTagIdsList()));

        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andAppIdEqualTo(appId)
                .andOpenIdEqualTo(openId);
        List<FanPO> fanPOList = fanMapper.selectByExample(example);

        //自定义部分
        fanPO.setUpdateTime(DateUtil.curSeconds());
        fanPO.setInBlacklist(userInfoDTO.getGroupId() == 1 ? 1 : 0);
        fanPO.setLastInteractTime(DateUtil.curSeconds());

        if (fanPOList.size() != 0) {
            fanPO.setFanId(fanPOList.get(0).getFanId());
            fanMapper.updateByPrimaryKeySelective(fanPO);
        } else {
            fanPO.setCreateTime(DateUtil.curSeconds());
            fanMapper.insertSelective(fanPO);
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
        openIdMapper.updateByExampleSelective(record, example);

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
        fanMapper.updateByExampleSelective(record, example);

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
        List<OpenIdPO> openIdPOS = openIdMapper.selectByExample(example);

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
        fanMapper.updateByExampleSelective(record, example);
    }
}
