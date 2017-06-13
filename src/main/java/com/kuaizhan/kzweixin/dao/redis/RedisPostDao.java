package com.kuaizhan.kzweixin.dao.redis;

/**
 * Created by zixiong on 2017/4/19.
 */
public interface RedisPostDao {
    /**
     * 用户是否可以同步微信文章 (频率控制)
     * @return 是否可以
     */
    boolean couldSyncWxPost(long weixinAppid);
}
