package com.kuaizhan.service;

import com.kuaizhan.exception.common.DownloadFileFailedException;
import com.kuaizhan.exception.common.MediaIdNotExistException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.WxPostListDTO;
import com.kuaizhan.pojo.DTO.WxPostDTO;

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
    void deletePost(String mediaId, String accessToken);

    /**
     * 上传图片到微信永久素材
     * @param accessToken
     * @param imgUrl
     */
    HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws DownloadFileFailedException;

    /**
     * 上传图文中的图片到微信服务器
     * @param imgUrl 原始的url
     * @return 在微信服务器的url
     */
    String uploadImgForPost(String accessToken, String imgUrl) throws RedisException, DownloadFileFailedException;

    /**
     * 上传多图文到微信
     * @param posts 多图文对象
     * @return
     */
    String uploadPosts(String accessToken, List<PostDO> posts);

    /**
     * 更新微信图文的某一篇
     */
    void updatePost(String accessToken, String mediaId, PostDO postDO) throws MediaIdNotExistException;

    /**
     * 根据偏移获取微信图文列表
     */
    WxPostListDTO getWxPostList(String accessToken, int offset, int count);

    /**
     * 根据mediaId获取微信图文
     */
    List<WxPostDTO> getWxPost(String mediaId, String accessToken);
}
