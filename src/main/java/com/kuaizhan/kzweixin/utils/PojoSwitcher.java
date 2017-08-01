package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.controller.vo.AccountSettingVO;
import com.kuaizhan.kzweixin.controller.vo.AccountVO;
import com.kuaizhan.kzweixin.controller.vo.MsgVO;
import com.kuaizhan.kzweixin.controller.vo.PostVO;
import com.kuaizhan.kzweixin.controller.vo.FanVO;
import com.kuaizhan.kzweixin.enums.MsgSendType;
import com.kuaizhan.kzweixin.enums.MsgType;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * pojo的转换组件
 * Created by zixiong on 2017/3/30.
 */
public class PojoSwitcher {

    private static final Logger logger = LoggerFactory.getLogger(PojoSwitcher.class);

    /**
     * 图文
     */
    public static PostVO postPOToVO(PostPO postPO) {

        if (postPO == null) {
            return null;
        }
        PostVO postVO = new PostVO();

        postVO.setPageId(postPO.getPageId());
        postVO.setMediaId(postPO.getMediaId());
        postVO.setTitle(postPO.getTitle());
        postVO.setAuthor(postPO.getAuthor());
        postVO.setDigest(postPO.getDigest());
        postVO.setContent(postPO.getContent());
        postVO.setThumbUrl(postPO.getThumbUrl());
        postVO.setThumbMediaId(postPO.getThumbMediaId());
        postVO.setContentSourceUrl(postPO.getContentSourceUrl());
        postVO.setShowCoverPic(postPO.getShowCoverPic());
        postVO.setUpdateTime(postPO.getUpdateTime());
        return postVO;
    }

    /**
     * 账号
     */
    public static AccountVO accountPOToVO(AccountPO accountPO) {
        if (accountPO == null) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        accountVO.setWeixinAppid(accountPO.getWeixinAppid());
        accountVO.setAppId(accountPO.getAppId());

        // 对appSecret进行打码处理
        String appSecret = accountPO.getAppSecret();
        if (appSecret != null && !"".equals(appSecret)) {
            int length = appSecret.length();
            int firstIdx = length > 5? 5: length;
            int lastIdx = length > 5? length - 5: length;
            accountVO.setAppSecret(
                    appSecret.substring(0, firstIdx)
                    + "************************"
                    + appSecret.substring(lastIdx, length)
            );
        }
        // 优先选择快站的qrcode链接
        String qrcode = accountPO.getQrcodeUrlKz();
        if (qrcode == null || "".equals(qrcode)) {
            qrcode = accountPO.getQrcodeUrl();
        }
        accountVO.setQrcode(qrcode);

        accountVO.setHeadImg(accountPO.getHeadImg());
        accountVO.setName(accountPO.getNickName());
        accountVO.setServiceType(accountPO.getServiceType());
        accountVO.setVerifyType(accountPO.getVerifyType());
        return accountVO;
    }

    /**
     * 公众号的部分设置信息
     */
    public static AccountSettingVO toAccountSettingVO(AccountPO accountPO) {
        AccountSettingVO settingVO = new AccountSettingVO();

        String interestJson = accountPO.getInterestJson();
        // 到处都是这些不稳定的设计
        if ("".equals(interestJson)) {
            interestJson = "[]";
        }
        settingVO.setInterest(JsonUtil.string2List(interestJson, String.class));

        String advancedFuncJson = accountPO.getAdvancedFuncInfoJson();
        if ("".equals(advancedFuncJson)) {
            advancedFuncJson = "{}";
        }
        JSONObject jsonObject = new JSONObject(advancedFuncJson);
        settingVO.setOpenLogin(jsonObject.optInt("open_login") == 1);
        settingVO.setOpenShare(jsonObject.optInt("open_share") == 1);

        return settingVO;
    }

    public static MsgVO msgPOToVO(MsgPO msgPO) {
        if (msgPO == null) {
            return null;
        }
        MsgVO msgVO = new MsgVO();
        msgVO.setMsgType(MsgType.fromValue(msgPO.getType().shortValue()));
        msgVO.setSendType(MsgSendType.fromCode(msgPO.getSendType()));
        msgVO.setOpenId(msgPO.getOpenId());
        msgVO.setHeadImgUrl(msgPO.getHeadImgUrl());
        msgVO.setNickname(msgPO.getNickName());
        msgVO.setCreateTime(msgPO.getCreateTime());

        int msgType = msgPO.getType();
        // 文本消息、关键词消息、外链消息、图文消息都直接返回
        if (msgType == 1 || msgType == 12 || msgType == 2 || msgType == 10) {
            JSONObject contentJson;
            try {
               contentJson = new JSONObject(msgPO.getContent());
            } catch (JSONException e) {
                contentJson = new JSONObject();
                contentJson.put("content", "*************");
                logger.warn("[msgPOToVo] 垃圾数据, msgType:{} content:{}", msgType, msgPO.getContent(), e);
            }
            msgVO.setContent(contentJson.toMap());
        } else {
            // 不支持的消息
            JSONObject contentJson = new JSONObject();
            contentJson.put("content", "[收到暂不支持的消息类型，请在微信公众平台上查看]");
            msgVO.setContent(contentJson.toMap());
        }
        return msgVO;
    }

    /**
     * 粉丝管理
     * */
    public static FanVO fanPOToVO(FanPO fanPO) {
        if (fanPO == null) {
            return null;
        }

        FanVO fanVO = new FanVO();
        fanVO.setId(fanPO.getFanId());
        fanVO.setName(fanPO.getNickName());
        fanVO.setHeadImgUrl(fanPO.getHeadImgUrl());
        fanVO.setSex(fanPO.getSex());
        fanVO.setOpenId(fanPO.getOpenId());
        fanVO.setAddress(fanPO.getCountry() + " " + fanPO.getProvince());
        fanVO.setSubscribeTime(fanPO.getSubscribeTime());

        if ("".equals(fanPO.getTagIdsJson())) {
            fanVO.setTagIds(new ArrayList<>());
        } else {
            fanVO.setTagIds(JsonUtil.string2List(fanPO.getTagIdsJson(), Integer.class));
        }

        return fanVO;
    }
}
