package com.kuaizhan.dao.mongo;


import com.kuaizhan.pojo.DO.MongoPostDo;

/**
 * 文章mongo数据接口
 * Created by liangjiateng on 2017/3/23.
 */
public interface MongoPostDao {


    /**
     * 根据articleId获取post content
     * @param articleId
     * @return
     */
    MongoPostDo getPostById(long articleId);

    /**
     * 更新post
     */
    void updatePost(MongoPostDo postDo);

    /**
     * 更新post, 不存在则新建
     */
    void upsertPost(MongoPostDo postDo);

    /**
     * 删除post content
     * @param articleId
     */
    void deletePost(long articleId);

    /**
     * 新增post content
     */
    void insertPost(MongoPostDo mongoPostDo);
}
