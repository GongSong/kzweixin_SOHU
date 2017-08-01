package com.kuaizhan.kzweixin.service;


import com.kuaizhan.kzweixin.entity.responsejson.PostResponseJson;
import com.kuaizhan.kzweixin.exception.kuaizhan.GetKzArticleException;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.entity.post.ArticleDTO;
import com.kuaizhan.kzweixin.entity.common.Page;
import com.kuaizhan.kzweixin.entity.post.WxPostDTO;
import com.kuaizhan.kzweixin.exception.post.GuideFollowPostNotFoundException;
import com.kuaizhan.kzweixin.exception.weixin.WxPostUsedException;

import java.util.HashMap;
import java.util.List;


/**
 * 图文消息service
 * Created by zixiong on 2017/3/20.
 */
public interface PostService {

    /**
     * 获取图文消息列表
     * @param pageNum 页码
     */
    Page<PostPO> listPostsByPage(long weixinAppid, String title, Integer pageNum, Boolean flat);

    /**
     * 根据mediaId获取多图文的所有图文
     * @param withContent 是否获取content字段
     */
    List<PostPO> getPostsByMediaId(long weixinAppid, String mediaId, Boolean withContent);

    /**
     * 根据mediaId获取单篇图文(单图文，或者多图文的总图文)
     * @param weixinAppid: 需要weixinAppid才能查询，否则因为老数据的存在(同样的mediaId在不同的weixinAppid下)会出现重复数据
     */
    PostPO getOnePostByMediaId(long weixinAppid, String mediaId);

    /**
     * 删除图文
     * 根据pageId，删除多图文下面的所有图文, 同时删除在微信后台的图文
     */
    void deletePost(long weixinAppid, long pageId) throws WxPostUsedException;

    /**
     * 是否存在微信图文
     */
    Boolean existPost(long weixinAppid, String mediaId);

    /**
     * 获取图文
     */
    PostPO getPostByPageId(long pageId);

    /**
     * 临时接口，根据pageId, 获取图文内容
     */
    String getPostContent(long pageId);

    /**
     * 获取快站文章
     */
    ArticleDTO getKzArticle(long pageId) throws GetKzArticleException;

    /**
     * 异步导入快文
     */
    void asyncImportKzArticles(long weixinAppid, List<Long> pageIds);

    /**
     * 快站微信文章导入快站文章
     */
    void export2KzArticle(long pageId,long categoryId,long siteId);

    /**
     * 新增一条多图文消息
     * @param posts 新增的post数据列表
     * @return 图文的pageId
     */
    long addMultiPosts(long weixinAppid, List<PostPO> posts);

    /**
     * 更新多图文
     */
    void updateMultiPosts(long weixinAppid, long pageId, List<PostPO> posts);

    /**
     * 获取图文的微信链接
     */
    String getPostWxUrl(long pageId);

    /**
     * 同步微信消息(异步)
     */
    void syncWeixinPosts(long weixinAppid);

    /**
     * 计算应该同步的微信图文
     */
    void calSyncWeixinPosts(long weixinAppid);

    /**
     * 由微信导入图文
     */
    void importWeixinPost(long weixinAppid, String mediaId, long updateTime, List<WxPostDTO> wxPostDTOs);

    /**
     * 更新微信图文
     */
    void updateWeixinPost(long weixinAppid, String mediaId, long updateTime, List<WxPostDTO> wxPostDTOs);

    /**
     * 上传微信永久素材，如缩略图
     * @return map: oldMediaId,素材的id oldUrl,素材的url
     */
    HashMap<String, String> uploadWxMaterial(long weixinAppid, String imgUrl);

    /**
     * 上传图文中的图片
     * @return  图片url
     */
    String uploadWxImage(long weixinAppid, String imgUrl);

    /**
     * 获取引导关注图文
     * @throws GuideFollowPostNotFoundException 引导关注图文不存在
     */
    String getGuideFollowPost(long weixinAppid) throws GuideFollowPostNotFoundException;

    /**
     * 新增引导关注图文
     * 幂等操作，存在则不新增
     * @return 新增的引导关注图文url
     */
    String addGuideFollowPost(long weixinAppid);

    /**
     * 根据mediaId列表，组装PostResponse对象
     */
    PostResponseJson getPostResponseJson(long weixinAppid, List<String> mediaIds);
}
