package com.kuaizhan.service;


import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.KZPostAddException;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.exception.business.MaterialGetException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.PostDTO;

import java.io.IOException;
import java.util.List;


/**
 * 图文消息service
 * Created by zixiong on 2017/3/20.
 */
public interface PostService {

    /**
     * 获取图文消息列表
     *
     * @param weixinAppid 微信appid
     * @param page  页码
     * @param title 按title模糊搜索
     * @return
     */
    Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page) throws DaoException, MongoException;

    /**
     * 根据mediaId获取所有的多图文
     *
     * @param mediaId
     * @return
     * @throws DaoException
     */
    List<PostDO> listMultiPosts(String mediaId) throws DaoException, MongoException;

    /**
     * 删除图文
     *
     * @param pageId
     */
    void deletePost(long weixinAppid, long pageId, String accessToken) throws DaoException, MaterialDeleteException, MongoException;

    /**
     * 获取图文
     *
     * @param pageId
     * @return
     */
    PostDO getPostByPageId(long pageId) throws DaoException, MongoException;

    /**
     * 获取快站文章
     *
     * @param pageId
     * @return
     */
    ArticleDTO getKzArticle(long pageId) throws IOException;

    /**
     * 从快站文章导入
     *
     * @param weixinAppid
     * @param pageIds
     */
    void importKzArticle(long weixinAppid, List<Long> pageIds);

    /**
     * 快站微信文章导入快站文章
     *
     * @param weixinAppid
     */
    void export2KzArticle(long weixinAppid,long pageId,long categoryId,long siteId) throws DaoException, KZPostAddException, MongoException;

    /**
     * 新增一条多图文消息，并同步到微信服务器
     *
     * @param posts 新增的post数据列表
     */
    void insertMultiPosts(long weixinAppid, List<PostDO> posts) throws Exception;

    /**
     * 更新一条多图文消息，并同步到微信服务器
     */
    void updateMultiPosts(long weixinAppid, long pageId, List<PostDO> posts) throws Exception;

    /**
     * 根据weixinAppid获取mediaId列表
     *
     * @param weixinAppid
     * @return
     * @throws DaoException
     */
    List<String> listMediaIdsByWeixinAppid(long weixinAppid) throws DaoException;

    /**
     * 同步微信消息(异步)
     * @param weixinAppid
     * @param uid 用户id，用于上传图片
     */
    void syncWeixinPosts(long weixinAppid, long uid);

    /**
     * 由微信导入图文
     *
     * @param postItem
     */
    void importWeixinPost(PostDTO.PostItem postItem, long userId) throws Exception;

    /**
     * 本地不存在的微信图文mediaId列表
     *
     * @param weixinAppid
     * @return
     */
    List<PostDTO.PostItem> listNonExistsPostItemsFromWeixin(long weixinAppid) throws DaoException, AccountNotExistException, RedisException, JsonParseException, MaterialGetException;
}
