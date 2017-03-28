package com.kuaizhan.service;


import com.kuaizhan.exception.business.MaterialDeleteException;

/**
 * 微信素材服务
 * Created by liangjiateng on 2017/3/28.
 */
public interface WeixinPostService {

    /**
     * 微信删除图文
     * @param mediaId
     * @param accessToken
     */
    void deletePost(String mediaId,String accessToken) throws MaterialDeleteException;
}
