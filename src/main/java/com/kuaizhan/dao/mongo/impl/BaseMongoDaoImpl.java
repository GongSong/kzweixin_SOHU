package com.kuaizhan.dao.mongo.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/23.
 */
public abstract class BaseMongoDaoImpl<T> {

    @Resource
    private MongoTemplate mongoTemplate;

    private String collectionName;

    public BaseMongoDaoImpl(String collectionName) {
        this.collectionName = collectionName;
    }

    //添加
    protected void insert(T object) {
        mongoTemplate.insert(object, collectionName);
    }

    //根据条件查找
    protected Object get(Map<String, Object> params, Class<T> cls) {
        Query query = new Query();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            query.addCriteria(Criteria.where(key).is(value));
        }
        return mongoTemplate.findOne(query, cls, collectionName);
    }

    //查找所有
    protected List<T> list(Map<String, Object> params, Class<T> cls) {
        Query query = new Query();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            query.addCriteria(Criteria.where(key).is(value));
        }
        return mongoTemplate.find(query, cls, collectionName);
    }

    //修改
    protected void update(Map<String, Object> params, Class<T> cls) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(params.get("id")));
        Update update = new Update();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if ("id".equals(key)) {
                continue;
            }
            Object value = entry.getValue();
            update.set(key, value);
        }
        mongoTemplate.updateMulti(query, update, cls,collectionName);
    }

    //修改，不存在则新增
    protected void upsert(Map<String, Object> params, Class<T> cls) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(params.get("id")));
        Update update = new Update();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if ("id".equals(key)) {
                continue;
            }
            Object value = entry.getValue();
            update.set(key, value);
        }
        mongoTemplate.upsert(query, update, cls,collectionName);
    }


    //根据条件删除
    protected void delete(Map<String, Object> params, Class<T> cls) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(params.get("id")));
        mongoTemplate.remove(query, cls, collectionName);
    }
}
