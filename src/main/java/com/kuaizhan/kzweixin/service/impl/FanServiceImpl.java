package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.cache.FanCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.FanMapper;
import com.kuaizhan.kzweixin.dao.po.auto.FanPOExample;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.weixin.*;
import com.kuaizhan.kzweixin.manager.WxFanManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.service.WeixinFanService;
import com.kuaizhan.kzweixin.utils.DBTableUtil;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.apache.ibatis.session.RowBounds;
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
    private WeixinFanService weixinFanService;
    @Resource
    private AccountService accountService;


    @Override
    public int createTag(long weixinAppid, String tagName) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        int tagId;
        try {
            tagId = WxFanManager.createTag(accessToken, tagName);
        } catch (WxDuplicateTagException e) {
            throw new BusinessException(ErrorCode.DUPLICATED_TAG);
        } catch (WxTagNumExceedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_NUM);
        }
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
        try {
            WxFanManager.updateTag(accessToken, tagId, tagName);
        } catch (WxDuplicateTagException e) {
            throw new BusinessException(ErrorCode.DUPLICATED_TAG);
        } catch (WxTagReservedModifiedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_MODIFIED);
        }
        fanCache.deleteTags(weixinAppid);
    }

    @Override
    public void deleteTag(long weixinAppid, int tagId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        try {
            WxFanManager.deleteTag(accessToken, tagId);
        } catch (WxTagReservedModifiedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_MODIFIED);
        } catch (WxFansNumExceedException e) {
            throw new BusinessException(ErrorCode.DELETE_TAG_FANS_EXCEED_10W);
        }
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
        try {
            for (int tagId: newTagsId) {
                WxFanManager.addFanTag(accessToken, fansOpenId, tagId);
            }
        } catch (WxOpenIdExceedException e) {
            throw new BusinessException(ErrorCode.OPEN_ID_EXCEED);
        } catch (WxInvalidTagException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_ERROR);
        } catch (WxFansTagExceedException e) {
            throw new BusinessException(ErrorCode.FANS_TAG_EXCEED);
        } catch (WxInvalidOpenIdException e) {
            throw new BusinessException(ErrorCode.INVALID_OPEN_ID_ERROR);
        } catch (WxOpenIdMismatchException e) {
            throw new BusinessException(ErrorCode.OPEN_ID_MISMATCH_ERROR);
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
        try {
            for (int tagId: deleteTagsId) {
                WxFanManager.deleteFanTag(accessToken, fansOpenId, tagId);
            }
        } catch (WxOpenIdExceedException e) {
            throw new BusinessException(ErrorCode.OPEN_ID_EXCEED);
        } catch (WxInvalidTagException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_ERROR);
        } catch (WxInvalidOpenIdException e) {
            throw new BusinessException(ErrorCode.INVALID_OPEN_ID_ERROR);
        } catch (WxOpenIdMismatchException e) {
            throw new BusinessException(ErrorCode.OPEN_ID_MISMATCH_ERROR);
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
    public Page<FanPO> listFansByPage(long weixinAppid, int pageNum, int pageSize, List<Integer> tagIds, String queryStr, int isBlacklist) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        Page<FanPO> fanPage = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        FanPOExample example = new FanPOExample();

        if (tagIds != null && tagIds.size() != 0) {
            for (int tagId: tagIds) {
                String tagIdStr = tagId + "";
                if ("2".equals(tagIdStr)) {
                    FanPOExample.Criteria criteria1 = example.createCriteria();
                    criteria1.andStatusEqualTo(1)
                            .andAppIdEqualTo(accountPO.getAppId())
                            .andInBlacklistEqualTo(isBlacklist)
                            .andTagIdsJsonLike("%[2]%");
                    example.or(criteria1);

                    FanPOExample.Criteria criteria2 = example.createCriteria();
                    criteria2.andStatusEqualTo(1)
                            .andAppIdEqualTo(accountPO.getAppId())
                            .andInBlacklistEqualTo(isBlacklist)
                            .andTagIdsJsonLike("%,2]%");
                    example.or(criteria2);

                    FanPOExample.Criteria criteria3 = example.createCriteria();
                    criteria3.andStatusEqualTo(1)
                            .andAppIdEqualTo(accountPO.getAppId())
                            .andInBlacklistEqualTo(isBlacklist)
                            .andTagIdsJsonLike("%[2,%");
                    example.or(criteria3);

                    FanPOExample.Criteria criteria4 = example.createCriteria();
                    criteria4.andStatusEqualTo(1)
                            .andAppIdEqualTo(accountPO.getAppId())
                            .andInBlacklistEqualTo(isBlacklist)
                            .andTagIdsJsonLike("%,2,%");
                    example.or(criteria4);

                } else {
                    FanPOExample.Criteria criteria5 = example.createCriteria();
                    criteria5.andStatusEqualTo(1)
                            .andAppIdEqualTo(accountPO.getAppId())
                            .andInBlacklistEqualTo(isBlacklist)
                            .andTagIdsJsonLike("%" + tagIdStr + "%");
                    example.or(criteria5);
                }
            }
        } else if (queryStr != null && !"".equals(queryStr)) {
            FanPOExample.Criteria criteria6 = example.createCriteria();
            criteria6.andStatusEqualTo(1)
                    .andAppIdEqualTo(accountPO.getAppId())
                    .andInBlacklistEqualTo(isBlacklist)
                    .andNickNameLike(queryStr);
            example.or(criteria6);
        } else {
            FanPOExample.Criteria criteria7 = example.createCriteria();
            criteria7.andStatusEqualTo(1)
                    .andAppIdEqualTo(accountPO.getAppId())
                    .andInBlacklistEqualTo(isBlacklist);
            example.or(criteria7);
        }

        RowBounds rowBounds = new RowBounds(fanPage.getOffset(), fanPage.getLimit());
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());
        long totalCount = fanMapper.countByExample(example, table);
        List<FanPO> fanList = fanMapper.selectByExampleWithRowbounds(example, rowBounds, table);
        fanPage.setTotalCount(totalCount);
        fanPage.setResult(fanList);
        return fanPage;
    }

    @Override
    public void addFanBlacklist(long weixinAppid, List<String> fansOpenId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String accessToken = accountService.getAccessToken(weixinAppid);
        try {
            WxFanManager.addBlacklist(accessToken, fansOpenId);
        } catch (WxInvalidOpenIdException e) {
            throw new BusinessException(ErrorCode.INVALID_OPEN_ID_ERROR);
        } catch (WxOpenIdMismatchException e) {
            throw new BusinessException(ErrorCode.OPEN_ID_MISMATCH_ERROR);
        } catch (WxBlacklistExceedException e) {
            throw new BusinessException(ErrorCode.ADD_BLACKLIST_EXCEED_LIMIT);
        }

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
        try {
            WxFanManager.removeBlacklist(accessToken, fansOpenId);
        } catch (WxInvalidOpenIdException e) {
            throw new BusinessException(ErrorCode.INVALID_OPEN_ID_ERROR);
        } catch (WxOpenIdMismatchException e) {
            throw new BusinessException(ErrorCode.OPEN_ID_MISMATCH_ERROR);
        } catch (WxBlacklistExceedException e) {
            throw new BusinessException(ErrorCode.ADD_BLACKLIST_EXCEED_LIMIT);
        }

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
    public Page<FanPO> listFansByPageFromDao(long weixinAppid, int pageNum, int pageSize, List<Integer> tagIds, String queryStr, int isBlacklist) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        Page<FanPO> fanPage = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        String table = DBTableUtil.getFanTableName(accountPO.getAppId());

        long totalCount = fanDao.countFan(accountPO.getAppId(), isBlacklist, tagIds, queryStr, table);
        List<FanPO> fanPOList = fanDao.listFansByPage(accountPO.getAppId(), fanPage, isBlacklist, tagIds, queryStr, table);

        fanPage.setTotalCount(totalCount);
        fanPage.setResult(fanPOList);

        return fanPage;

    }
}
