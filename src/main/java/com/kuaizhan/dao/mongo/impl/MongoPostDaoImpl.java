package com.kuaizhan.dao.mongo.impl;

import com.kuaizhan.dao.mongo.MongoPostDao;
import com.kuaizhan.pojo.DO.ArticleDO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/23.
 */
@Repository("mongoPostDao")
public class MongoPostDaoImpl extends BaseMongoDaoImpl<ArticleDO> implements MongoPostDao {

    //TODO:Mongo使用样例 随后删除

    public MongoPostDaoImpl() {
        //表名 wx-dev-是前缀
        super("wx-dev-" + "test");
    }

    @Override
    public ArticleDO getArticleById(long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return (ArticleDO) get(param, ArticleDO.class);
    }

    @Override
    public void updateArticle(ArticleDO articleDO) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", articleDO.getId());
        param.put("title", articleDO.getTitle());
        param.put("content", articleDO.getContent());
        update(param, ArticleDO.class);
    }

    @Override
    public void deleteArticle(long id) {
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        delete(param, ArticleDO.class);
    }

    @Override
    public void insertArticle(ArticleDO articleDO) {
        insert(articleDO);
    }

    @Override
    public List<ArticleDO> listArticlesByTitleAndContent(String title, String content) {
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("content", content);
        return list(param, ArticleDO.class);
    }

    @Override
    public List<ArticleDO> listArticles() {
        Map<String, Object> param = new HashMap<>();
        return list(param, ArticleDO.class);
    }
}
