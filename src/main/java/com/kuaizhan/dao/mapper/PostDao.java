package com.kuaizhan.dao.mapper;

import com.kuaizhan.pojo.po.PostPO;
import com.kuaizhan.pojo.dto.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zixiong on 2017/3/20.
 */
@Repository
public interface PostDao {

    /**
     * 查询图文消息总数
     *
     * @param weixinAppid 微信appid
     * @return
     */
    Long count(@Param("weixinAppid") long weixinAppid, @Param("title") String title, @Param("flat") Boolean flat);

    /**
     * 查询图文消息列表
     *
     * @param weixinAppid 微信appid
     * @param page
     * @return
     */
    List<PostPO> listPostsByPagination(@Param("weixinAppid") long weixinAppid, @Param("title") String title, @Param("pageEntity") Page page, @Param("flat") Boolean flat);

    /**
     * 获取图文消息的多图文
     *
     * @param mediaId 图文消息的mediaId
     * @return
     */
    List<PostPO> listMultiPosts(@Param("weixinAppid") long weixinAppid, @Param("mediaId") String mediaId);


    /**
     * 判断是否存在微信图文
     */
    Boolean exist(@Param("weixinAppid") long weixinAppid, @Param("mediaId") String mediaId);

    /**
     * 根据mediaId删除图文, 逻辑删除
     *
     */
    // TODO: 考虑更改索引结构，按weixinAppid mediaId分别单独建索引
    int deletePost(@Param("weixinAppid") long weixinAppid, @Param("mediaId") String mediaId);

    /**
     * 根据pageId逻辑删除单篇图文
     */
    int deletePostByPageId(long pageId);

    /**
     * 根据pageId获取图文
     *
     * @param pageId
     * @return
     */
    PostPO getPost(long pageId);


    /**
     * 根据mediaId, 获取单图文或图文总记录
     */
    PostPO getPostByMediaId(@Param("weixinAppid") long weixinAppid, @Param("mediaId") String mediaId);

    /**
     * 新增单个图文
     */
    void insertPost(@Param("post") PostPO post);

    /**
     * 修改单个图文
     */
    void updatePost(@Param("post") PostPO post, @Param("pageId") long pageId);

    /**
     * 检测某个pageId是否存在
     * 用于id生成
     * @param pageId
     */
    Boolean isPageIdExist(long pageId);
}

