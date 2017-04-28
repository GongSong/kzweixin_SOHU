package com.kuaizhan.service;

import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.PostDTO;
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
    void deletePost(String mediaId, String accessToken) throws MaterialDeleteException;

    /**
     * 上传图片到微信永久素材
     * @param accessToken
     * @param imgUrl
     */
    HashMap<String, String> uploadImage(String accessToken, String imgUrl) throws AddMaterialException, DownloadFileFailedException;

    /**
     * 上传图文中的图片到微信服务器
     * @param imgUrl 原始的url
     * @return 在微信服务器的url
     */
    String uploadImgForPost(String accessToken, String imgUrl) throws AddMaterialException, RedisException, DownloadFileFailedException;

    /**
     * 上传多图文到微信
     * @param posts 多图文对象
     * @return
     */
    String uploadPosts(String accessToken, List<PostDO> posts) throws UploadPostsException, ThumbMediaIdNotExistException;

    /**
     * 更新微信图文的某一篇
     * @throws UploadPostsException
     */
    void updatePost(String accessToken, String mediaId, PostDO postDO) throws UploadPostsException, MediaIdNotExistException;

    /**
     * 根据偏移获取微信图文列表
     * @param accessToken
     * @param offset
     * @param count
     * @return
     * @throws MaterialGetException
     */
    PostDTO getPostDTOByOffset(String accessToken, int offset, int count) throws MaterialGetException;

    /**
     * 获取所有微信图文消息
     * @param accessToken
     * @return
     * @throws MaterialGetException
     */
    List<PostDTO> listAllPosts(String accessToken) throws MaterialGetException;


    /**
     * 根据mediaId获取微信图文
     */
    List<WxPostDTO> getWxPost(String mediaId, String accessToken) throws WxPostDeletedException;
}
