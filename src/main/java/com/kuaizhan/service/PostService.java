package com.kuaizhan.service;


import com.kuaizhan.exception.business.*;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.WxPostDTO;

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
    Page<PostDO> listPostsByPagination(long weixinAppid, String title, Integer page, Boolean flat);

    /**
     * 根据mediaId获取所有的多图文
     *
     * @param withContent 是否获取content字段
     */
    List<PostDO> listMultiPosts(long weixinAppid, String mediaId, Boolean withContent);

    /**
     * 删除图文
     * 根据pageId，删除多图文下面的所有图文, 同时删除在微信后台的图文
     * @param pageId
     */
    void deletePost(long weixinAppid, long pageId, String accessToken);

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
     */
    String getPostContent(long pageId);

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
    void export2KzArticle(long pageId,long categoryId,long siteId);

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
    String getPostWxUrl(long weixinAppid, long pageId) throws AccountNotExistException;

    /**
     * 同步微信消息(异步)
     * @param weixinAppid
     * @param userId 用户id，用于上传图片
     * @return 是否可以同步
     */
    void syncWeixinPosts(long weixinAppid, long userId);

    /**
     * 计算应该同步的微信图文
     */
    void calSyncWeixinPosts(long weixinAppid, long userId) throws AccountNotExistException;

    /**
     * 由微信导入图文
     */
    void importWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception;

    /**
     * 更新微信图文
     */
    void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs) throws Exception;
}
