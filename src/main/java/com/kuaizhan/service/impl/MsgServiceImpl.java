package com.kuaizhan.service.impl;


import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.constant.ErrorCode;
import com.kuaizhan.constant.MsgType;
import com.kuaizhan.constant.WxMsgType;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.dao.mapper.auto.WeixinMsgConfigMapper;
import com.kuaizhan.exception.BusinessException;
import com.kuaizhan.exception.common.DownloadFileFailedException;
import com.kuaizhan.exception.deprecated.business.SendCustomMsgException;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.manager.WxMsgManager;
import com.kuaizhan.manager.WxPostManager;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.po.auto.WeixinMsgConfig;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.MsgService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinMsgService;
import com.kuaizhan.utils.DBTableUtil;
import com.kuaizhan.utils.JsonUtil;
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
    private WeixinMsgConfigMapper msgConfigMapper;
    @Resource
    private AccountService accountService;
    @Resource
    private FanDao fanDao;
    @Resource
    private PostService postService;
    @Resource
    private WeixinMsgService weixinMsgService;

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
        WeixinMsgConfig config = new WeixinMsgConfig();
        config.setWeixinAppid(weixinAppid);
        config.setLastReadTime((int) (System.currentTimeMillis() / 1000));
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
        WeixinMsgConfig config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        return JsonUtil.string2List(config.getQuickReplyJson(), String.class);
    }

    @Override
    public void updateQuickReplies(long weixinAppid, List<String> replies) {
        WeixinMsgConfig config = new WeixinMsgConfig();
        config.setWeixinAppid(weixinAppid);
        config.setQuickReplyJson(JsonUtil.list2Str(replies));
        msgConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public void sendCustomMsg(long weixinAppid, String openId, MsgType msgType, Map<String, Object> dataMap) {
        String accessToken = accountService.getAccessToken(weixinAppid);

        WxMsgType wxMsgType;
        // TODO: 用dataMap传输json数据，实在是不是好方法。--- 不方便做数据校验, 经常面临强制转换，数据结构不那么"显而易见"。
        switch (msgType) {
            case TEXT:
                wxMsgType = WxMsgType.TEXT;
                String content = (String) dataMap.getOrDefault("content", null);
                if (content == null) {
                    throw new BusinessException(ErrorCode.PARAM_ERROR, "content不能为空");
                }
                dataMap.put("content", EmojiParser.removeAllEmojis(content));
                break;
            case IMAGE:
                wxMsgType = WxMsgType.IMAGE;
                String mediaId = (String) dataMap.getOrDefault("media_id", null);
                String picUrl = (String) dataMap.getOrDefault("pic_url", null);

                if (mediaId == null) {
                    if (picUrl == null) {
                        throw new BusinessException(ErrorCode.PARAM_ERROR, "pic_url不能为空");
                    }
                    try {
                        Map<String, String> resultMap = WxPostManager.uploadImage(accessToken, picUrl);
                        dataMap.put("media_id", resultMap.get("mediaId"));
                    } catch (DownloadFileFailedException e) {
                        throw new BusinessException(ErrorCode.OPERATION_FAILED, "下载文件失败，请稍后重试");
                    }
                }
                break;
            case NEWS:
                wxMsgType = WxMsgType.NEWS;
                List<Map> articles = (List) dataMap.get("articles");
                for (Map articleMap: articles) {
                    Long pageId = (Long) articleMap.getOrDefault("pageId", null);
                    if (pageId == null) {
                        // TODO: 这样的代码完全看不懂，因为维护者不知道这里的结构
                        String url = (String) articleMap.getOrDefault("url", null);
                        if (url == null) {
                            throw new BusinessException(ErrorCode.PARAM_ERROR, "url不能为空");
                        }
                    } else {
                        String wxUrl = postService.getPostWxUrl(weixinAppid, pageId);
                        articleMap.put("url", wxUrl);
                    }
                }
                break;
            default:
                throw new BusinessException(ErrorCode.OPERATION_FAILED, "不允许的消息类型");
        }

        // 发送消息
        WxMsgManager.sendCustomMsg(accessToken, openId, wxMsgType, dataMap);
        // 存储消息
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();

        MsgPO msgPO = new MsgPO();
        msgPO.setAppId(appId);
        msgPO.setContent(JsonUtil.bean2String(dataMap));
        msgPO.setOpenId(openId);
        msgPO.setSendType(2);
        msgPO.setType((int) msgType.getValue());
        insertMsg(appId, msgPO);
    }


    /** 存储消息 **/
    private void insertMsg(String appid, MsgPO msgPO) {
        String tableName = DBTableUtil.getMsgTableName(appid);
        msgDao.insertMsg(tableName, msgPO);
    }

    /** 获取消息的最后阅读时间，不存在则新建 **/
    private int getLastReadTime(long weixinAppid) {
        WeixinMsgConfig config = msgConfigMapper.selectByPrimaryKey(weixinAppid);
        // 不存在则新建
        if (config == null) {
            config = new WeixinMsgConfig();
            config.setWeixinAppid(weixinAppid);
            config.setQuickReplyJson("[]");
            int now = (int) (System.currentTimeMillis() / 1000);
            config.setLastReadTime(now);
            config.setCreateTime(now);
            config.setCreateTime(now);
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
