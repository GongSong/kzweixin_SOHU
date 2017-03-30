package com.kuaizhan.service;

import com.kuaizhan.exception.business.AddMaterialException;
import com.kuaizhan.exception.business.MaterialDeleteException;

import com.kuaizhan.exception.business.UploadPostsException;
import com.kuaizhan.pojo.DO.PostDO;

import java.util.HashMap;
import java.util.List;

/**
 * 微信素材服务
 * Created by liangjiateng on 2017/3/28.
 */
public interface WeixinPostService {

    /**
     * 微信删除图文
     *
     * @param mediaId
     * @param accessToken
     */
    void deletePost(String mediaId, String accessToken) throws MaterialDeleteException;

    /**
     * 上传图片到微信永久素材
     * @param accessToken
     * @param imgUrl
     */
    HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws AddMaterialException;

    /**
     * 上传图文中的图片到微信服务器
     * @param imgUrl 原始的url
     * @return 在微信服务器的url
     */
    String uploadImgForPost(String accessToken, String imgUrl) throws AddMaterialException;

    /**
     * 上传多图文到微信
     * @param posts 多图文对象
     * @return
     */
    String uploadPosts(String accessToken, List<PostDO> posts) throws UploadPostsException;

}
