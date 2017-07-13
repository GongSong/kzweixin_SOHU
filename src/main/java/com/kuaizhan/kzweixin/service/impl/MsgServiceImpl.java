package com.kuaizhan.kzweixin.service.impl;


import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.cache.MsgCache;
import com.kuaizhan.kzweixin.config.KzApiConfig;
import com.kuaizhan.kzweixin.constant.AppConstant;
import com.kuaizhan.kzweixin.enums.MsgType;
import com.kuaizhan.kzweixin.dao.mapper.FanDao;
import com.kuaizhan.kzweixin.dao.mapper.MsgDao;
import com.kuaizhan.kzweixin.dao.mapper.auto.MsgConfigMapper;
import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.manager.WxCommonManager;
import com.kuaizhan.kzweixin.manager.WxMsgManager;
import com.kuaizhan.kzweixin.entity.msg.CustomMsg;
import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.dao.po.auto.MsgConfigPO;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.MsgService;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.DBTableUtil;
import com.kuaizhan.kzweixin.utils.DateUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.UrlUtil;
import com.vdurmont.emoji.EmojiParser;
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
    private MsgDao msgDao;
    @Resource
    private MsgConfigMapper msgConfigMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private FanDao fanDao;
    @Resource
    private PostService postService;
    @Resource
    private MsgCache msgCache;

    @Override
    public long getUnreadMsgCount(long weixinAppid) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();
        long lastReadTime = getLastReadTime(weixinAppid);

        String msgTableName = DBTableUtil.getMsgTableName(appId);
        return msgDao.countMsgs(appId, msgTableName, null, 1,null, false, lastReadTime, null);
    }

    @Override
    public void markMsgRead(long weixinAppid) {
        MsgConfigPO config = new MsgConfigPO();
        config.setWeixinAppid(weixinAppid);
        config.setLastReadTime(DateUtil.curSeconds());
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public Page<MsgPO> listMsgsByPagination(long weixinAppid, String queryStr, boolean filterKeywords, int pageNum) {

        Page<MsgPO> page = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        long lastReadTime = getLastReadTime(weixinAppid);
        String appId = accountPO.getAppId();
        String msgTableName = DBTableUtil.getMsgTableName(appId);

        // 查询消息列表
        List<MsgPO> msgPOS = msgDao.listMsgsByPagination(appId, msgTableName, null, 1,
                queryStr, filterKeywords,
                lastReadTime, page.getOffset(), page.getLimit());
        // 查询消息总数
        long count = msgDao.countMsgs(appId, msgTableName, null, 1,
                queryStr, filterKeywords, null, lastReadTime);
        page.setTotalCount(count);

        // 封装粉丝信息
        setMsgUserInfo(appId, msgPOS, accountPO);

        page.setResult(msgPOS);
        return page;
    }

    @Override
    public Page<MsgPO> listMsgsByOpenId(long weixinAppid, String openId, int pageNum) {
        Page<MsgPO> page = new Page<>(pageNum, AppConstant.PAGE_SIZE_LARGE);
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);

        String appId = accountPO.getAppId();
        String msgTableName = DBTableUtil.getMsgTableName(appId);

        // 查询消息列表
        List<MsgPO> msgPOS = msgDao.listMsgsByPagination(appId, msgTableName, openId,
                null, null, null, null,
                page.getOffset(), page.getLimit());
        // 查询消息总数
        long count = msgDao.countMsgs(appId, msgTableName, openId, 1,
                null, null, null, null);
        page.setTotalCount(count);

        // 封装粉丝信息
        setMsgUserInfo(appId, msgPOS, accountPO);

        page.setResult(msgPOS);
        return page;
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
        MsgPO msgPO = new MsgPO();
        msgPO.setAppId(appId);
        msgPO.setContent(customMsg.getContentJsonStr());
        msgPO.setOpenId(openId);
        msgPO.setSendType(2);
        msgPO.setType((int) customMsg.getMsgType().getValue());
        String tableName = DBTableUtil.getMsgTableName(appId);
        msgDao.insertMsg(tableName, msgPO);
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
                customMsg.setMsgType(MsgType.NEWS);
                customMsg.setNews(newsFromMp);
                customMsg.setContentJsonStr(JsonUtil.bean2String(newsFromMp));
                break;
            // 链接组类型
            case NEWS:
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

    /**
     * 封装消息列表的用户信息
     */
    private void setMsgUserInfo(String appId, List<MsgPO> msgPOS, AccountPO accountPO) {

        String fanTableName = DBTableUtil.getFanTableName(appId);

        Map<String, FanPO> openIdMap = new HashMap<>();
        List<String> openIds = new ArrayList<>();
        for(MsgPO msgPO: msgPOS) {
            openIds.add(msgPO.getOpenId());
        }

        // 查询粉丝信息
        if (accountPO.getServiceType() == 2 && openIds.size() > 0) {
            List<FanPO> fanPOS = fanDao.listFansByOpenIds(appId, openIds, fanTableName);
            for (FanPO fanPO: fanPOS) {
                openIdMap.put(fanPO.getOpenId(), fanPO);
            }
        }

        // 封装消息的头像和昵称信息
        for (MsgPO msgPO: msgPOS) {
            FanPO fanPO = openIdMap.getOrDefault(msgPO.getOpenId(), null);
            // 公众号发送
            if (msgPO.getSendType() == 2) {
                msgPO.setNickName(accountPO.getNickName());
                msgPO.setHeadImgUrl(accountPO.getHeadImg());

            } else if (fanPO != null) {
                msgPO.setNickName(fanPO.getNickName());
                msgPO.setHeadImgUrl(fanPO.getHeadImgUrl());

            } else {
                String openId = msgPO.getOpenId();
                msgPO.setNickName("粉丝" + openId.substring(openId.length() - 4, openId.length()));
                msgPO.setHeadImgUrl(KzApiConfig.getResUrl("/res/weixin/images/kuaizhan-logo.png"));
            }
        }
    }
}
