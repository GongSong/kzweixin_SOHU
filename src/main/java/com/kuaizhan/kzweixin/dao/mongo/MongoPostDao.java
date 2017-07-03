package com.kuaizhan.kzweixin.dao.mongo;


/**
 * 文章mongo数据接口
 * Created by liangjiateng on 2017/3/23.
 */
public interface MongoPostDao {


    /**
     * 根据pageId获取content
     */
    String getContentById(long pageId);


    /**
     * 新建或者更新post
     */
    void upsertPost(long pageId, String content);

    /**
     * 删除post
     */
    void deletePost(long pageId);

}
