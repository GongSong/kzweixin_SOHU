package com.kuaizhan.kzweixin.dao.mongo.impl;

import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.dao.mongo.MongoPostDao;
import com.kuaizhan.kzweixin.dao.po.MongoPostPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/23.
 */
@Repository("mongoPostDao")
public class MongoPostDaoImpl extends BaseMongoDaoImpl<MongoPostPO> implements MongoPostDao {

    public static final Logger logger = LoggerFactory.getLogger(MongoPostDaoImpl.class);

    public MongoPostDaoImpl() {
        //表名 wx-dev-是前缀
        super(ApplicationConfig.MONGO_PREFIX + "post");
    }

    @Override
    public String getContentById(long pageId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", pageId);
        Object obj = get(param, MongoPostPO.class);
        if (obj == null) {
            return null;
        }
        MongoPostPO mongoPostPO = (MongoPostPO) obj;
        return mongoPostPO.getContent();
    }

    @Override
    public void upsertPost(long pageId, String content) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", pageId);
        param.put("content", content);
        upsert(param, MongoPostPO.class);
    }

    @Override
    public void deletePost(long pageId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", pageId);
        delete(param, MongoPostPO.class);
    }
}
