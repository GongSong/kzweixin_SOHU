package com.kuaizhan.service;


import com.kuaizhan.exception.business.AddMaterialException;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.exception.business.UploadPostsException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;

import java.util.List;


/**
 * 图文消息service
 * Created by zixiong on 2017/3/20.
 */
public interface PostService {

    /**
     * 获取图文消息列表
     * @param weixinAppid 微信appid
     * @param page 页码
     * @return
     */
    Page<PostDO> listPostsByPagination(long weixinAppid, Integer page) throws DaoException;

    /**
     * 根据mediaId获取所有的多图文
     * @param mediaId
     * @return
     * @throws DaoException
     */
    List<PostDO> listMultiPosts(String mediaId) throws DaoException;

    /**
     * 删除图文
     *
     * @param pageId
     */
    void deletePost(long weixinAppid,long pageId,String accessToken) throws DaoException, MaterialDeleteException;

    /**
     * 获取图文
     *
     * @param pageId
     * @return
     */
    PostDO getPostByPageId(long pageId) throws DaoException;


    /**
     * 新增一条多图文消息，并同步到微信服务器
     * @param posts 新增的post数据列表
     */
    void insertMultiPosts(long weixinAppid, List<PostDO> posts) throws Exception;
}
