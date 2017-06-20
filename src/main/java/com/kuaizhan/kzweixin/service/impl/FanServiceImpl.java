package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.cache.FanCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.FanMapper;
import com.kuaizhan.kzweixin.dao.po.auto.FanPOExample;
import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.exception.common.RedisException;
import com.kuaizhan.kzweixin.dao.po.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.weixin.*;
import com.kuaizhan.kzweixin.manager.WxFanManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.FanService;
import com.kuaizhan.kzweixin.service.WeixinFanService;
import com.kuaizhan.kzweixin.utils.DBTableUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
        } catch (WxTagLengthExceedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_LENGTH);
        } catch (WxTagNumExceedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_NUM);
        } catch (WxApiException e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        }
        fanCache.deleteTags(weixinAppid);
        return tagId;
    }

    @Override
    public List<TagDTO> getTags(long weixinAppid) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        List<TagDTO> tagsList;
        try {
            //先从Redis缓存查找数据
            //TODO: Redis读取存储操作出错概率极低，可以考虑移出try catch block
            tagsList = fanCache.getTags(weixinAppid);
            if (tagsList != null) {
                return tagsList;
            }

            //如果缓存没找到，再去微信后台请求，拿到数据后存入Redis缓存
            tagsList = WxFanManager.getTags(accessToken);
            fanCache.setTag(weixinAppid, tagsList);
        } catch (IOException e) {
            throw new RedisException(e);
        } catch (WxApiException e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        }
        return tagsList;
    }

    @Override
    public void updateTag(long weixinAppid, int tagId, String tagName) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        try {
            WxFanManager.updateTag(accessToken, tagId, tagName);
        } catch (WxDuplicateTagException e) {
            throw new BusinessException(ErrorCode.DUPLICATED_TAG);
        } catch (WxTagLengthExceedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_LENGTH);
        } catch (WxTagReservedModifiedException e) {
            throw new BusinessException(ErrorCode.INVALID_TAG_NUM);
        } catch (WxApiException e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR);
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
            throw new BusinessException(ErrorCode.INVALID_TAG_NUM);
        } catch (WxFansNumExceedException e) {
            throw new BusinessException(ErrorCode.DELETE_TAG_FANS_EXCEED_10W);
        } catch (WxApiException e) {
            throw new BusinessException(ErrorCode.SERVER_ERROR);
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
            fan.setTagIdsJson(JsonUtil.list2Str(tagsList));
            fanMapper.updateByPrimaryKeySelective(fan, table);
        }
    }

    @Override
    public FanPO getFanByOpenId(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();
        String table = DBTableUtil.getFanTableName(appId);
        FanPOExample example = new FanPOExample();
        example.createCriteria()
                .andOpenIdEqualTo(openId);
        return fanMapper.selectByExample(example, table).get(0);
    }


}
