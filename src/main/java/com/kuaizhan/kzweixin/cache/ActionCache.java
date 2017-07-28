package com.kuaizhan.kzweixin.cache;

/**
 * Created by zixiong on 2017/07/27.
 */
public interface ActionCache {
    /**
     * 缓存openId
     * @param expireIn 指定过期时间
     * @return 换回获取缓存的凭证token
     */
    String setOpenId(String openId, int expireIn);

    /**
     * 根据token获取openId
     */
    String getOpenId(String token);


    /**
     * 删除缓存的openId
     */
    void deleteOpenId(String token);
}
