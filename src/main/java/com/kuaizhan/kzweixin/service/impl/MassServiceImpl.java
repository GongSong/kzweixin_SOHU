package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.dao.mapper.auto.CustomMassMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.MassMapper;
import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.entity.msg.MassMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.enums.WxMsgType;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.common.JsonConvertException;
import com.kuaizhan.kzweixin.manager.WxMsgManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MassService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Chen on 17/7/11.
 */
@Service("massService")
public class MassServiceImpl implements MassService {

    @Resource
    protected MassMapper massMapper;

    @Resource
    protected CustomMassMapper CustomMassMapper;

    @Resource
    protected AccountService accountService;

    @Resource
    protected PostService postService;

    @Override
    public MassPO getMassById(long id) {
        MassPOExample example = new MassPOExample();
        example.createCriteria()
                .andMassIdEqualTo(id)
                .andIsTimingEqualTo(1)
                .andStatusNotEqualTo(0);
        List<MassPO> massList = massMapper.selectByExample(example);
        if (massList.size() == 0) {
            throw new BusinessException(ErrorCode.MASS_NOT_EXIST);
        }
        return massList.get(0);
    }

    @Override
    public List<MassPO> getMassByWxAppId(long wxAppId) {
        MassPOExample example = new MassPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(wxAppId)
                .andStatusNotEqualTo(0);
        example.setOrderByClause("publish_time desc");
        List<MassPO> massList = massMapper.selectByExample(example);
        if (massList.size() == 0) {
            throw new BusinessException(ErrorCode.MASS_NOT_EXIST);
        }
        return massList;
    }

    @Override
    public List<CustomMassPO> getCustomMassByWxAppId(long wxAppid) {
        CustomMassPOExample example = new CustomMassPOExample();
        example.or()
                .andWeixinAppidEqualTo(wxAppid)
                .andStatusNotEqualTo(0)
                .andStatusNotEqualTo(4);
        example.or()
                .andWeixinAppidEqualTo(wxAppid)
                .andStatusNotEqualTo(0)
                .andIsTimingEqualTo(1) ;
        example.setOrderByClause("update_time desc");
        List<CustomMassPO> customMassList = CustomMassMapper.selectByExample(example);
        if (customMassList.size() == 0) {
            throw new BusinessException(ErrorCode.CUSTOM_MASS_NOT_EXIST);
        }
        return customMassList;
    }

    @Override
    public String wrapPreviewMsg(long weixinAppid, MsgType type, String json, int isMulti) {
        String msg = json;
        if(type == MsgType.MP_NEWS) {
            List<Long> postIds = JsonUtil.string2List(json, Long.class);
            if(postIds == null) {
                throw new BusinessException(ErrorCode.MASS_POSTID_INVALID);
            }
            CustomMsg.MpNews mpNews = new CustomMsg.MpNews(postIds);
            return JsonUtil.bean2String(mpNews);
        }
        return JsonUtil.bean2String(json);
    }

    @Override
    public Object wrapMassMsg(long wxAppId, MsgType type, String json, int isMulti) {
        Object msg = null;
        if(type == MsgType.MP_NEWS) {
            List<Long> postIds = JsonUtil.string2List(json, Long.class);
            if(postIds == null) {
                throw new BusinessException(ErrorCode.MASS_POSTID_INVALID);
            } else if(postIds.size() == 1 || isMulti == 1) {
                PostPO post = postService.getPostByPageId(postIds.get(0));
                if(post != null) {
                    MassMsg.MpNews mpNews = new MassMsg.MpNews(post.getMediaId(), null);
                    return mpNews;
                }
            } else if(postIds.size() > 1) {
                List<PostPO> postPOS = postService.getPostsByPageId(postIds);
                long pageId = postService.addMultiPosts(wxAppId, postPOS);
                MassMsg.MpNews mpNews = new MassMsg.MpNews(postService.getPostMediaId(pageId), postIds);
                return mpNews;
            }
        } else if(type == MsgType.IMAGE) {
            msg = JsonUtil.string2Bean(json, MassMsg.Image.class);
        } else if(type == MsgType.TEXT){
            msg = JsonUtil.string2Bean(json, CustomMsg.Text.class);
        } else {
            throw new BusinessException(ErrorCode.MASS_TYPE_INVALID);
        }
        return msg;
    }

    @Override
    public void sendMassMsg(long weixinAppid, int tagId, MsgType msgType, Object contentObj) {
        String accessToken = accountService.getAccessToken(weixinAppid);

        WxMsgType wxMsgType;
        switch(msgType) {
            case MP_NEWS:
                wxMsgType = WxMsgType.MP_NEWS;
                break;
            case IMAGE:
                wxMsgType = WxMsgType.IMAGE;
                break;
            case TEXT:
                wxMsgType = WxMsgType.TEXT;
                break;
            default:
                throw new BusinessException(ErrorCode.OPERATION_FAILED, "不允许的消息类型");
        }

        WxMsgManager.sendMassMsg(accessToken, tagId, wxMsgType, contentObj);

        // 存储消息
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();

        MassPO massPO = new MassPO();
        massPO.setWeixinAppid(weixinAppid);
        massPO.setResponseType((int)msgType.getValue());
        massPO.setResponseJson(JsonUtil.bean2String(contentObj));
        massPO.setGroupId(tagId);
       /* massPO.setIsTiming();
        massPO.setPublishTime(); */
        massPO.setCreateTime(new Date().getTime()/1000);
        massMapper.insert(massPO);
    }

    @Override
    public boolean checkSupportType(MsgType type){
        if(type == null || type != MsgType.TEXT || type != MsgType.IMAGE || type != MsgType.MP_NEWS) {
            return false;
        }
        return true;
    }

}
