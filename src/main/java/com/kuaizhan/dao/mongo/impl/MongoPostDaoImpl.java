package com.kuaizhan.dao.mongo.impl;

import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.pojo.DO.MongoPostDo;
import com.kuaizhan.utils.IdGeneratorUtil;
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
    public MongoPostDo getPostById(long articleId) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", articleId);
        return (MongoPostDo) get(param, MongoPostDo.class);
    }

    @Override
    public void updatePost(MongoPostDo postDo) {
        Map<String, Object> param = new HashMap<>();
        param.put("pageId", postDo.getPageId());
        param.put("content", postDo.getContent());
        update(param, MongoPostDo.class);
    }

    @Override
    public void deletePost(long pageId) {
        Map<String, Object> param = new HashMap<>();
        param.put("pageId", pageId);
        delete(param, MongoPostDo.class);
    }

    @Override
    public long insertPost(String content) {
        MongoPostDo postDo = new MongoPostDo();
        long id = IdGeneratorUtil.getID();
        postDo.setPageId(id);
        insert(postDo);
        return id;
    }
}
