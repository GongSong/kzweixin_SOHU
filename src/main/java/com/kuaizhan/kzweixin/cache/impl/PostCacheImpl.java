package com.kuaizhan.kzweixin.cache.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.cache.PostCache;
import org.springframework.stereotype.Repository;

/**
 * Created by zixiong on 2017/4/19.
 */
// TODO: Repository里面的昵称，原来不是必须的...
@Repository("postCache")
public class PostCacheImpl extends RedisBaseDaoImpl implements PostCache {

    @Override
    public boolean couldSyncWxPost(long weixinAppid) {
        String key = RedisConstant.KEY_COULD_SYNC_WX_POST + weixinAppid;

        String val = getData(key);
        if (val != null && ! "".equals(val)){
            return false;
        } else {
            setData(key, "true", RedisConstant.EXPIRE_COULD_SYNC_WX_POST);
            return true;
        }
    }
}
