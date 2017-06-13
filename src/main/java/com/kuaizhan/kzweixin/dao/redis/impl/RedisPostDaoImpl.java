package com.kuaizhan.kzweixin.dao.redis.impl;

import com.kuaizhan.kzweixin.constant.RedisConstant;
import com.kuaizhan.kzweixin.dao.redis.RedisPostDao;
import org.springframework.stereotype.Repository;

/**
 * Created by zixiong on 2017/4/19.
 */
@Repository("redisPostDao")
public class RedisPostDaoImpl extends RedisBaseDaoImpl implements RedisPostDao {

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
