package com.kuaizhan.kzweixin.cache;

/**
 * Created by zixiong on 2017/4/19.
 */
public interface PostCache {
    /**
     * 用户是否可以同步微信文章 (频率控制)
     * @return 是否可以
     */
    boolean couldSyncWxPost(long weixinAppid);

    /**
     * 引导关注图文链接
     */
    void setGuideFollowPostUrl(long weixinAppid, String url);
    String getGuideFollowPostUrl(long weixinAppid);
    void deleteGuideFollowPostUrl(long weixinAppid);
}
