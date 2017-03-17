package com.kuaizhan.service;


import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.TagDTO;

import java.io.IOException;
import java.util.List;

/**
 * 微信粉丝服务
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface WeixinFanService {
    /**
     * 从微信端获取标签
     *
     * @param accessToken 公众号accessToken
     * @return
     */

    List<TagDTO> listTags(String accessToken) throws Exception;

    /**
     * 创建一个新标签
     *
     * @param accessToken 公众号accessToken
     * @param tagName     标签名
     * @return
     */
    int insertTag(String accessToken, String tagName);

    /**
     * 为用户设置标签
     *
     * @param accessToken 公众号accessToken
     * @param openIdList  用户列表
     * @param tagId       标签Id
     * @return
     */
    int updateTag(String accessToken, List<String> openIdList, int tagId);

    /**
     * 删除标签
     *
     * @return
     */
    int deleteTag(String accessToken, int tagId);

    /**
     * 重命名标签
     *
     * @return
     */
    int renameTag(String accessToken, int tagId, String newName);

    /**
     * 将粉丝加入黑名单
     *
     * @return
     */
    int insertBlack(String accessToken, List<FanDO> fanDOList);

    /**
     * 将粉丝移除黑名单
     *
     * @param accessToken
     * @return
     */
    int removeBlack(String accessToken, List<FanDO> fanDOList);
}
