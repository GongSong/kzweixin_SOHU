package com.kuaizhan.service;


import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.WxPostDTO;
import com.kuaizhan.pojo.DTO.WxPostListDTO;

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
     * @param flat 是否展开多图文
     * @return
     */
    Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page, Boolean flat) throws DaoException;

    /**
     * 根据mediaId获取所有的多图文
     *
     * @param withContent 是否获取content字段
     * @return
     * @throws DaoException
     */
    List<PostDO> listMultiPosts(long weixinAppid, String mediaId, Boolean withContent) throws DaoException;

    /**
     * 删除图文
     * 根据pageId，删除多图文下面的所有图文, 同时删除在微信后台的图文
     * @param pageId
     */
    void deletePost(long weixinAppid, long pageId, String accessToken) throws MaterialDeleteException;

    /**
     * 是否存在微信图文
     * @return
     */
    Boolean exist(long weixinAppid, String mediaId);

    /**
     * 获取图文
     *
     * @param pageId
     * @return
     */
    PostDO getPostByPageId(long pageId);

    /**
     * 根据mediaId获取单篇图文(单图文，或者多图文的第一篇)
     */
    PostDO getPostByMediaId(long weixinAppid, String mediaId);

    /**
     * 临时接口，根据pageId, 获取图文内容
     * @param pageId
     * @return
     * @throws MongoException
     */
    String getPostContent(long pageId) throws MongoException;

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
     */
    void export2KzArticle(long pageId,long categoryId,long siteId) throws DaoException, KZPostAddException, MongoException;

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
     * 获取post在微信的url
     * @return
     */
    String getPostWxUrl(long weixinAppid, long pageId) throws DaoException, AccountNotExistException, RedisException, WxPostDeletedException, WxPostLessThenPost;

    /**
     * 同步微信消息(异步)
     * @param weixinAppid
     * @param userId 用户id，用于上传图片
     * @return 是否可以同步
     */
    void syncWeixinPosts(long weixinAppid, long userId) throws SyncWXPostTooOftenException;

    /**
     * 计算应该同步的微信图文
     */
    void calSyncWeixinPosts(long weixinAppid, long userId) throws DaoException, AccountNotExistException, RedisException, MaterialGetException;

    /**
     * 由微信导入图文
     */
    void importWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception;

    /**
     * 更新微信图文
     */
    void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception;
}
