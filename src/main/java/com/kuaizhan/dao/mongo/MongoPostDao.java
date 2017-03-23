package com.kuaizhan.dao.mongo;


import com.kuaizhan.pojo.DO.ArticleDO;

import java.util.List;

/**
 * 文章mongo数据接口
 * Created by liangjiateng on 2017/3/23.
 */
public interface MongoPostDao {

    //TODO:Mongo使用样例 随后删除

    ArticleDO getArticleById(long id);

    void updateArticle(ArticleDO articleDO);

    void deleteArticle(long id);

    void insertArticle(ArticleDO articleDO);

    List<ArticleDO> listArticlesByTitleAndContent(String title,String content);

    List<ArticleDO> listArticles();
}
