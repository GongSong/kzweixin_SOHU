package com.kuaizhan.dao.mongo.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.pojo.DO.MongoPostDo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/23.
 */
@Repository("mongoPostDao")
public class MongoPostDaoImpl extends BaseMongoDaoImpl<MongoPostDo> implements MongoPostDao {

    public static final Logger logger = Logger.getLogger(MongoPostDaoImpl.class);

    public MongoPostDaoImpl() {
        //表名 wx-dev-是前缀
        super(ApplicationConfig.getMongoCollectionPrefix() + "post");
    }

    @Override
    public String getContentById(long pageId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", pageId);
        Object obj = get(param, MongoPostDo.class);
        if (obj == null) {
            return "";
        }
        MongoPostDo mongoPostDo = (MongoPostDo) obj;
        return mongoPostDo.getContent();
    }

    @Override
    public void upsertPost(long pageId, String content) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", pageId);
        param.put("content", content);
        upsert(param, MongoPostDo.class);
    }

    @Override
    public void deletePost(long pageId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", pageId);
        delete(param, MongoPostDo.class);
    }
}
