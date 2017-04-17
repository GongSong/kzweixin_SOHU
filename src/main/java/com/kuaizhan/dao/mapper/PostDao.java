package com.kuaizhan.dao.mapper;

import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zixiong on 2017/3/20.
 */

public interface PostDao {

    /**
     * 查询图文消息总数
     *
     * @param weixinAppid 微信appid
     * @return
     */
    Long count(@Param("weixinAppid") long weixinAppid, @Param("title") String title);

    /**
     * 查询图文消息列表
     *
     * @param weixinAppid 微信appid
     * @param page
     * @return
     */
    List<PostDO> listPostsByPagination(@Param("weixinAppid") long weixinAppid, @Param("title") String title, @Param("pageEntity") Page page);

    /**
     * 获取图文消息的多图文
     *
     * @param mediaId 图文消息的mediaId
     * @return
     */
    List<PostDO> listMultiPosts(String mediaId);

    /**
     * 根据mediaId删除图文, 逻辑删除
     *
     * @param weixinAppid  为了利用索引, 传此参数
     * @param mediaId
     * @return
     */
    // TODO: 考虑更改索引结构，按weixinAppId mediaId分别单独建索引
    int deletePost(@Param("weixinAppId") long weixinAppid, @Param("mediaId") String mediaId);

    /**
     * 根据mediaId删除图文, 物理删除
     */
    int deletePostReal(@Param("weixinAppId") long weixinAppid, @Param("mediaId") String mediaId);

    /**
     * 根据pageId获取图文
     *
     * @param pageId
     * @return
     */
    PostDO getPost(long pageId);

    /**
     * 新增单个图文
     */
    void insertPost(@Param("post") PostDO post);

    /**
     * 修改单个图文
     */
    void updatePost(@Param("post") PostDO post, @Param("pageId") long pageId);

    /**
     * 根据weixinAppid获取mediaId列表
     * @param weixinAppid
     * @return
     */
    List<String> listMediaIdsByWeixinAppid(long weixinAppid);
}

