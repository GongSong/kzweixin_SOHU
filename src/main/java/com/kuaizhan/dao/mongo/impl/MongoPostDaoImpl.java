package com.kuaizhan.dao.mongo.impl;

import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.pojo.DO.MongoPostDo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/23.
 */
@Repository("mongoPostDao")
public class MongoPostDaoImpl extends BaseMongoDaoImpl<MongoPostDo> implements MongoPostDao {


    public MongoPostDaoImpl() {
        //表名 wx-dev-是前缀
        super("wx-dev-" + "test");
    }

    @Override
    public MongoPostDo getPostById(long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        Object obj = get(param, MongoPostDo.class);
        if (obj == null) {
            return null;
        }
        return (MongoPostDo) obj;
    }

    @Override
    public void updatePost(MongoPostDo postDo) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", postDo.getId());
        param.put("content", postDo.getContent());
        update(param, MongoPostDo.class);
    }

    @Override
    public void upsertPost(MongoPostDo postDo) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", postDo.getId());
        param.put("content", postDo.getContent());
        upsert(param, MongoPostDo.class);
    }

    @Override
    public void deletePost(long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        delete(param, MongoPostDo.class);
    }

    @Override
    public void insertPost(MongoPostDo mongoPostDo) {
        insert(mongoPostDo);
    }
}
