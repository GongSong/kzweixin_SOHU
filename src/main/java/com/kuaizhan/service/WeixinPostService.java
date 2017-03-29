package com.kuaizhan.service;


import com.kuaizhan.exception.business.AddMaterialException;
import com.kuaizhan.exception.business.MaterialDeleteException;

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
     * 给微信上传图片
     * 0返回meida_id 1返回url
     * @param accessToken
     * @param imgUrl
     */
    String[] uploadImage(String accessToken, String imgUrl) throws AddMaterialException;
}
