package com.kuaizhan.kzweixin.service.impl;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.cache.MsgCache;
import com.kuaizhan.kzweixin.dao.mapper.auto.MsgMapper;
import com.kuaizhan.kzweixin.dao.po.auto.*;
import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.enums.MsgSendType;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.dao.mapper.auto.MsgConfigMapper;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.manager.WxCommonManager;
import com.kuaizhan.kzweixin.manager.WxMsgManager;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MsgService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.UrlUtil;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 消息管理服务
 * Created by Mr.Jadyn on 2017/1/10.
 */
@Service("msgService")
public class MsgServiceImpl implements MsgService {

    @Resource
    private MsgMapper msgMapper;
    @Resource
    private MsgConfigMapper msgConfigMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private PostService postService;
    @Resource
    private MsgCache msgCache;

    @Override
    public long getUnreadMsgCount(long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        int lastReadTime = getLastReadTime(weixinAppid);

        MsgPOExample example = new MsgPOExample();
        example.createCriteria()
                .andAppIdEqualTo(accountPO.getAppId())
                .andSendTypeEqualTo(MsgSendType.TO_ACCOUNT)
                .andCreateTimeGreaterThan(lastReadTime);

        return msgMapper.countByExample(example);
    }

    @Override
    public void markMsgRead(long weixinAppid) {
        MsgConfigPO config = new MsgConfigPO();
        config.setWeixinAppid(weixinAppid);
        config.setLastReadTime(DateUtil.curSeconds());
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public PageV2<MsgPO> listMsgsByPage(long weixinAppid, String queryStr, boolean filterKeywords, int offset, int limit) {

        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        int lastReadTime = getLastReadTime(weixinAppid);
        String appId = accountPO.getAppId();

        // 组装查询条件
        MsgPOExample example = new MsgPOExample();
        MsgPOExample.Criteria criteria = example.createCriteria();
        criteria.andAppIdEqualTo(appId);
        criteria.andSendTypeEqualTo(MsgSendType.TO_ACCOUNT);
        criteria.andCreateTimeLessThan(lastReadTime);
        // 模糊查询不为空
        if (!StringUtils.isBlank(queryStr)) {
            criteria.andContentLike("%" + queryStr + "%");
            criteria.andTypeIn(ImmutableList.of(MsgType.TEXT, MsgType.KEYWORD_TEXT));
        }
        // 过滤关键词
        if (filterKeywords) {
            criteria.andTypeNotEqualTo(MsgType.KEYWORD_TEXT);
        }
        example.setOrderByClause("create_time desc");

        List<MsgPO> msgPOS = msgMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
        long total = msgMapper.countByExample(example);

        return new PageV2<>(total, msgPOS);
    }

    @Override
    public PageV2<MsgPO> listMsgsByOpenId(long weixinAppid, String openId, int offset, int limit) {

        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        MsgPOExample example = new MsgPOExample();
        example.createCriteria()
                .andAppIdEqualTo(accountPO.getAppId())
                .andOpenIdEqualTo(openId);

        List<MsgPO> msgPOS = msgMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, limit));
        long total = msgMapper.countByExample(example);

        return new PageV2<>(total, msgPOS);
    }

    @Override
    public List<String> getQuickReplies(long weixinAppid) {
        MsgConfigPO config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        return JsonUtil.string2List(config.getQuickReplyJson(), String.class);
    }

    @Override
    public void updateQuickReplies(long weixinAppid, List<String> replies) {
        MsgConfigPO config = new MsgConfigPO();
        config.setWeixinAppid(weixinAppid);
        config.setQuickReplyJson(JsonUtil.list2Str(replies));
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public void sendCustomMsg(long weixinAppid, String openId, CustomMsg customMsg) throws IllegalArgumentException {

        String accessToken = accountService.getAccessToken(weixinAppid);

        // 数据清理
        customMsg = cleanCustomMsg(customMsg, accessToken);
        // 发送消息
        WxMsgManager.sendCustomMsg(accessToken, openId, customMsg);

        // 存储消息
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();

        MsgPO record = new MsgPO();
        record.setAppId(appId);
        record.setOpenId(openId);
        record.setContent(customMsg.getContentJsonStr());
        record.setSendType(MsgSendType.TO_FAN);
        record.setType(customMsg.getMsgType());
        record.setCreateTime(DateUtil.curSeconds());
        record.setUpdateTime(DateUtil.curSeconds());
        msgMapper.insertSelective(record);
    }

    /**
     * 清理客服消息数据
     * @throws IllegalArgumentException 客服消息数据有误
     */
    private CustomMsg cleanCustomMsg(CustomMsg customMsg, String accessToken) throws IllegalArgumentException {
        switch (customMsg.getMsgType()) {
            // 文本类型
            case TEXT:
                CustomMsg.Text text = customMsg.getText();
                if (text == null || text.getContent() == null) {
                    throw new IllegalArgumentException("content不能为空");
                }
                // 去除emoji
                text.setContent(EmojiParser.removeAllEmojis(text.getContent()));
                customMsg.setContentJsonStr(JsonUtil.bean2String(text));
                break;
            // 图片类型
            case IMAGE:
                CustomMsg.Image image = customMsg.getImage();

                if (image == null || image.getPicUrl() == null) {
                    throw new IllegalArgumentException("pic_url不能为空");
                }

                // 上传图片
                if (image.getMediaId() == null) {
                    String picUrl = UrlUtil.fixProtocol(image.getPicUrl());
                    String mediaId = WxCommonManager.uploadTmpImage(accessToken, picUrl);
                    image.setMediaId(mediaId);
                }
                customMsg.setContentJsonStr(JsonUtil.bean2String(image));
                break;
            // 多图文类型
            case MP_NEWS:

                CustomMsg.MpNews mpNews = customMsg.getMpNews();

                if (mpNews == null || mpNews.getPosts().size() == 0) {
                    throw new IllegalArgumentException("图文列表不能为空");
                }

                // 转换为链接组类型
                CustomMsg.News newsFromMp = new CustomMsg.News();
                for(Long pageId: mpNews.getPosts()) {

                    PostPO postPO = postService.getPostByPageId(pageId);
                    CustomMsg.Article article = new CustomMsg.Article();
                    article.setTitle(postPO.getTitle());
                    article.setDescription(postPO.getDigest());
                    article.setUrl(postService.getPostWxUrl(pageId));
                    article.setPicUrl(postPO.getThumbUrl());

                    newsFromMp.getArticles().add(article);
                }
                customMsg.setMsgType(MsgType.LINK_GROUP);
                customMsg.setNews(newsFromMp);
                customMsg.setContentJsonStr(JsonUtil.bean2String(newsFromMp));
                break;
            // 链接组类型
            case LINK_GROUP:
                // TODO: 校验下参数
                CustomMsg.News news = new CustomMsg.News();
                customMsg.setContentJsonStr(JsonUtil.bean2String(news));
                break;

            default:
                throw new IllegalArgumentException("不允许的消息类型:" + customMsg.getMsgType());
        }
        return customMsg;
    }

    @Override
    public String getPushToken(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();

        String cache = msgCache.getPushToken(appId, openId);
        if (cache != null) {
            JSONObject jsonObject = new JSONObject(cache);
            return jsonObject.getString("push_token");
        }

        Map<String, String> result = KzManager.applyPushToken();
        String token = result.get("token");
        String clientId = result.get("clientId");
        // 存到redis，微信回调时根据是否有token决定是否推送
        msgCache.setPushToken(appId, openId, JsonUtil.bean2String(ImmutableMap.of("push_client_id", clientId, "push_token", token)));

        return token;
    }

    @Override
    public void deletePushToken(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        msgCache.deletePushToken(accountPO.getAppId(), openId);
    }


    /** 获取消息的最后阅读时间，不存在则新建 **/
    private int getLastReadTime(long weixinAppid) {
        MsgConfigPO config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        // 不存在则新建
        if (config == null) {
            config = new MsgConfigPO();
            config.setWeixinAppid(weixinAppid);
            config.setQuickReplyJson("[]");
            int now = DateUtil.curSeconds();
            config.setLastReadTime(now);
            config.setCreateTime(now);
            config.setUpdateTime(now);
            msgConfigMapper.insert(config);
        }
        return config.getLastReadTime();
    }
}
