package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.dao.mapper.auto.CustomMassMapper;
import com.kuaizhan.kzweixin.dao.mapper.auto.MassMapper;
import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.mass.MassPostDTO;
import com.kuaizhan.kzweixin.entity.mass.ResponseJson;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.entity.msg.MassMsg;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.enums.WxMsgType;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.common.JsonConvertException;
import com.kuaizhan.kzweixin.manager.WxInternalManager;
import com.kuaizhan.kzweixin.manager.WxMsgManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MassService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Chen on 17/7/11.
 */
@Service("massService")
public class MassServiceImpl implements MassService {

    @Resource
    protected MassMapper massMapper;

    @Resource
    protected CustomMassMapper customMassMapper;

    @Resource
    protected AccountService accountService;

    @Resource
    protected PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(MassServiceImpl.class);

    @Override
    public MassPO getMassById(long id) {
        MassPOExample example = new MassPOExample();
        example.createCriteria()
                .andMassIdEqualTo(id)
                .andStatusNotEqualTo(0);
        List<MassPO> massList = massMapper.selectByExample(example);
        if (massList.size() == 0) {
            throw new BusinessException(ErrorCode.MASS_NOT_EXIST);
        }
        return massList.get(0);
    }


    @Override
    public CustomMassPO getCustomMassById(long id) {
        CustomMassPOExample example = new CustomMassPOExample();
        example.createCriteria()
                .andCustomMassIdEqualTo(id);
        List<CustomMassPO> massList = customMassMapper.selectByExample(example);
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
        List<CustomMassPO> customMassList = customMassMapper.selectByExample(example);
        if (customMassList.size() == 0) {
            throw new BusinessException(ErrorCode.CUSTOM_MASS_NOT_EXIST);
        }
        return customMassList;
    }

    @Override
    public String formatMassResponseJson(String json, MassPO.RespType type) {
        String responseJson = json;
        if(type == MassPO.RespType.ARTICLES) {
            List<Long> postIds = JsonUtil.string2List(json, Long.class);
            if(postIds == null) {
                throw new BusinessException(ErrorCode.MASS_POSTID_INVALID);
            }
            ResponseJson.Articles articles = new ResponseJson.Articles();
            List<MassPostDTO> postDTOList = new ArrayList<>();
            for(Long id : postIds) {
                PostPO post = postService.getPostByPageId(id);
                if(post != null) {
                    MassPostDTO postDTO = new MassPostDTO();
                    postDTO.setPostId(post.getPageId());
                    postDTO.setPostTitle(post.getTitle());
                    postDTO.setPostDescription(null);
                    postDTO.setPostPicUrl(post.getThumbUrl());
                    postDTO.setPostType(post.getType());
                    postDTO.setPostUrl(post.getPostUrl());
                    postDTOList.add(postDTO);
                }
            }
            articles.setPostList(postDTOList);
            return JsonUtil.bean2String(articles);
        }
        return responseJson;
    }

    @Override
    public String wrapPreviewMsg(long weixinAppid, MassPO.RespType type, String json, int isMulti) {
        String msg = json;
        if(type == MassPO.RespType.ARTICLES) {
            List<Long> postIds = JsonUtil.string2List(json, Long.class);
            if(postIds == null) {
                throw new BusinessException(ErrorCode.MASS_POSTID_INVALID);
            }
            CustomMsg.MpNews mpNews = new CustomMsg.MpNews(postIds);
            return JsonUtil.bean2String(mpNews);
        }
        return msg;
    }

    @Override
    public Object wrapMassMsg(long wxAppId, MassPO.RespType type, String json, int isMulti) {
        Object msg = null;
        if(type == MassPO.RespType.ARTICLES) {
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
        } else if(type == MassPO.RespType.IMAGE) {
            msg = JsonUtil.string2Bean(json, MassMsg.Image.class);
        } else if(type == MassPO.RespType.TEXT) {
            msg = JsonUtil.string2Bean(json, CustomMsg.Text.class);
        } else {
            throw new BusinessException(ErrorCode.MASS_TYPE_INVALID);
        }
        return msg;
    }

    @Override
    public MsgType convert2WxMsgType(MassPO.RespType respType) {
        MsgType type = null;
        switch (respType) {
            case ARTICLES:
                type = MsgType.MP_NEWS;
                break;
            case IMAGE:
                type = MsgType.IMAGE;
                break;
            case TEXT:
                type = MsgType.TEXT;
                break;
            default:
                throw new BusinessException(ErrorCode.OPERATION_FAILED, "不允许的消息类型");
        }
        return type;
    }

    @Override
    public String sendMassMsg(long weixinAppid, int tagId, MassPO.RespType type, Object contentObj) {
        String accessToken = accountService.getAccessToken(weixinAppid);

        WxMsgType wxMsgType;
        switch(type) {
            case ARTICLES:
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

        String msgId = WxMsgManager.sendMassMsg(accessToken, tagId, wxMsgType, contentObj);

        return msgId;

    }

    @Override
    public JsonResponse deleteTimingJob(long pubTime, long massId) throws BusinessException {
        String jobName = "mass-" + massId + "-" + pubTime;
        try {
            return WxInternalManager.deleteTimingJob(jobName);
        } catch (BusinessException e) {
            throw e;
        }
    }

    @Override
    public JsonResponse CreateTimingJob(long pubTime, long massId) throws BusinessException {
        String jobName =  "mass-" + massId + "-" + pubTime;
        String jobUrl = "http://service.kuaizhan.sohuno.com/weixin/service-mass-msg-timing-publish?mass_id=" + massId;
        try {
            return WxInternalManager.createTimingJob(jobName, jobUrl, pubTime);
        } catch (BusinessException e) {
            throw e;
        }
    }

    @Override
    public long genMassId() {
        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long massId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (massMapper.isMassIdExist(massId)) {
            massId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }

        if (count >= 3) {
            logger.warn("[genPageId] gen times reach {}", count);
        }
        return massId;
    }

    @Override
    public void updateMass(MassPO mass) {
        massMapper.updateByPrimaryKey(mass);
    }

    @Override
    public void insertMass(MassPO mass) {
        massMapper.insert(mass);
    }

    @Override
    public boolean checkSupportType(MassPO.RespType type){
        if(type == null || (type != MassPO.RespType.TEXT && type != MassPO.RespType.IMAGE && type != MassPO.RespType.ARTICLES)) {
            return false;
        }
        return true;
    }

}
