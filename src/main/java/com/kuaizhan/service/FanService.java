package com.kuaizhan.service;

import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.exception.system.ServerException;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.TagDTO;

import java.io.IOException;
import java.util.List;

/**
 * 粉丝模块服务接口
 * Created by Mr.Jadyn on 2017/1/19.
 */
public interface FanService {
    /**
     * 查询数据
     *
     * @param appId   公众号的appId
     * @param isBlack 0白1黑
     * @param tagIds  标签ids
     * @param keyword 搜索关键词
     * @return
     */
    long countFan(String appId, int isBlack, List<Integer> tagIds, String keyword) throws DaoException;

    FanDO getFanByOpenId(String appId, String openId) throws DaoException;

    /**
     * 分页查询粉丝列表
     *
     * @param appId   公众号appId
     * @param page    页码
     * @param tagIds  标签号
     * @param isBlack 0白1黑
     * @param keyword 搜索关键词
     * @return
     * @throws IOException
     */

    Page<FanDO> listFanByPagination(long siteId, String appId, Integer page, Integer isBlack, List<Integer> tagIds, String keyword) throws DaoException, RedisException;

    /**
     * 获取所有标签
     *
     * @param accessToken 公众号accessToken
     * @return
     * @throws IOException
     */

    List<TagDTO> listTags(long siteId, String accessToken) throws RedisException, TagGetException;


    /**
     * 创建新标签
     *
     * @param tagName 标签名
     * @return -2标签名长度超过30个字节  1为成功 40013无效appId 40001无效accessToken 45157标签名非法，请注意不能和其他标签重名 45056创建的标签数过多，请注意不能超过100个
     */

    void insertTag(long siteId, String tagName, String accessToken) throws ServerException, TagNameLengthException, TagDuplicateNameException, TagNumberException, RedisException;

    /**
     * 更新用户标签
     *
     * @param appId   公众号appId
     * @param openIds 用户openId列表
     * @param tagIds  更新的标签id列表
     * @return 1为成功 40013无效appId -1传参错误 40001无效accessToken 40032 每次传入的openid列表个数不能超过50个 45159 非法的标签
     * 45059 有粉丝身上的标签数已经超过限制 40003 传入非法的openid 49003 传入的openid不属于此AppID
     */

    void updateUserTag(long siteId, String appId, List<String> openIds, List<Integer> tagIds, String accessToken) throws ServerException, OpenIdNumberException, TagException, FanTagNumberException, OpenIdException, DaoException;


    /**
     * 删除标签
     *
     * @param appId 公众号appId
     * @param tagId 标签Id
     * @return 1成功 40013无效appId -1传参错误 40001无效accessToken 45058不能修改0/1/2这三个系统默认保留的标签 45057该标签下粉丝数超过10w，不允许直接删除
     */

    void deleteTag(long siteId, String appId, Integer tagId, String accessToken) throws ServerException, TagDeleteFansNumberException, TagModifyException, DaoException, RedisException;


    /**
     * 重命名标签
     *
     * @param newTag      新标签
     * @param accessToken 公众号accessToken
     * @return -2标签名过长 1成功 40013无效appId -1传参错误 40001无效accessToken 45058无法修改该标签 45159无效标签id 45157标签名重复
     */
    void renameTag(long siteId, TagDTO newTag, String accessToken) throws TagDuplicateNameException, ServerException, TagNameLengthException, TagModifyException, RedisException;

    /**
     * 将用户移入黑名单
     *
     * @param accessToken 公众号accessToken
     * @param fanDOList   粉丝列表
     * @return -1传参错误 40003 传入非法的openid 49003传入的openid不属于此AppID -2一次只能拉黑20个用户 40001无效accessToken
     */
    void insertBlack(long siteId, String accessToken, List<FanDO> fanDOList) throws ServerException, OpenIdException, BlackAddNumberException, DaoException, RedisException;

    /**
     * 移除黑名单
     *
     * @param siteId      公众号appId
     * @param fanDOList   粉丝列表
     * @param accessToken 公众号accessToken
     * @return 40003    传入非法的openid 49003传入的openid不属于此AppID 	-2一次只能拉黑20个用户
     */
    void deleteBlack(long siteId, List<FanDO> fanDOList, String accessToken) throws ServerException, OpenIdException, BlackAddNumberException, DaoException, RedisException;

}
