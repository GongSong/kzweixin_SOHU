package com.kuaizhan.kzweixin.service;


import com.kuaizhan.kzweixin.exception.kuaizhan.GetKzArticleException;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.entity.post.ArticleDTO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.entity.post.WxPostDTO;
import com.kuaizhan.kzweixin.exception.weixin.WxPostUsedException;

import java.util.HashMap;
import java.util.List;


/**
 * 图文消息service
 * Created by zixiong on 2017/3/20.
 */
public interface PostService {

    /**
     * 生成pageId
     */
    long genPageId();

    /**
     * 获取图文消息列表
     * @param pageNum 页码
     */
    Page<PostPO> listPostsByPage(long weixinAppid, String title, Integer pageNum, Boolean flat);

    /**
     * 根据mediaId获取所有的多图文
     * @param withContent 是否获取content字段
     */
    List<PostPO> listMultiPosts(long weixinAppid, String mediaId, Boolean withContent);

    /**
     * 删除图文
     * 根据pageId，删除多图文下面的所有图文, 同时删除在微信后台的图文
     */
    void deletePost(long weixinAppid, long pageId, String accessToken) throws WxPostUsedException;

    /**
     * 是否存在微信图文
     * @return
     */
    Boolean existPost(long weixinAppid, String mediaId);

    /**
     * 获取图文
     */
    PostPO getPostByPageId(long pageId);

    /**
     * 根据mediaId获取单篇图文(单图文，或者多图文的第一篇)
     */
    PostPO getPostByMediaId(long weixinAppid, String mediaId);

    /**
     * 临时接口，根据pageId, 获取图文内容
     */
    String getPostContent(long pageId);

    /**
     * 获取快站文章
     */
    ArticleDTO getKzArticle(long pageId) throws GetKzArticleException;

    /**
     * 从快站文章导入
     */
    void importKzArticle(long weixinAppid, List<Long> pageIds);

    /**
     * 快站微信文章导入快站文章
     */
    void export2KzArticle(long pageId,long categoryId,long siteId);

    /**
     * 新增一条多图文消息，并同步到微信服务器
     * @param posts 新增的post数据列表
     */
    void insertMultiPosts(long weixinAppid, List<PostPO> posts);

    /**
     * 更新一条多图文消息，并同步到微信服务器
     */
    void updateMultiPosts(long weixinAppid, long pageId, List<PostPO> posts);


    /**
     * 获取post在微信的url
     */
    String getPostWxUrl(long weixinAppid, long pageId);

    /**
     * 同步微信消息(异步)
     * @param userId 用户id，用于上传图片
     * @return 是否可以同步
     */
    void syncWeixinPosts(long weixinAppid, long userId);

    /**
     * 计算应该同步的微信图文
     */
    void calSyncWeixinPosts(long weixinAppid, long userId);

    /**
     * 由微信导入图文
     */
    void importWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs);

    /**
     * 更新微信图文
     */
    void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, long userId, List<WxPostDTO> wxPostDTOs);

    /**
     * 上传微信永久素材，如缩略图
     * @return map: mediaId,素材的id url,素材的url
     */
    HashMap<String, String> uploadWxMaterial(long weixinAppid, String imgUrl);

    /**
     * 上传图文中的图片
     * @return  图片url
     */
    String uploadWxImage(long weixinAppid, String imgUrl);
}
