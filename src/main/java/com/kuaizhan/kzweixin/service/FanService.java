package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;
import com.kuaizhan.kzweixin.entity.common.Page;
import java.util.List;

/**
 * 粉丝模块服务接口
 * Created by fangtianyu on 6/15/17.
 */
public interface FanService {

    /**
     * 创建标签
     * @param tagName 新标签
     * @return 新标签的Id
     * */
    int createTag(long weixinAppid, String tagName);

    /**
     * 获取已创建的标签列表
     * @return 标签列表
     * */
    List<TagDTO> getTags(long weixinAppid);

    /**
     * 编辑（重命名）标签
     * @param tagId 标签Id
     * @param tagName 新标签
     * */
    void updateTag(long weixinAppid, int tagId, String tagName);

    /**
     * 删除标签
     * @param tagId 标签Id
     * */
    void deleteTag(long weixinAppid, int tagId);

    /**
     * 根据OpenId获取粉丝列表
     * @param openId 粉丝对公众号唯一ID
     * @return 粉丝信息
     * */
    FanPO getFanByOpenId(long weixinAppid, String openId);

    /**
     * 增加粉丝标签
     * @param fansOpenId 待修改标签的粉丝OpenId
     * @param newTagsId 新增标签Id
     */
    void addFanTag(long weixinAppid, List<String> fansOpenId, List<Integer> newTagsId);

    /**
     * 删除粉丝标签
     * @param fansOpenId 待修改标签的粉丝OpenId
     * @param deleteTagsId 删除标签Id
     */
    void deleteFanTag(long weixinAppid, List<String> fansOpenId, List<Integer> deleteTagsId);

    /**
     * 拉黑用户
     * @param fansOpenId 粉丝Id列表
     */
    void addFanBlacklist(long weixinAppid, List<String> fansOpenId);

    /**
     * 取消拉黑用户
     * @param fansOpenId 粉丝Id列表
     */
    void removeFanBlacklist(long weixinAppid, List<String> fansOpenId);

    /**
     * 按标签搜索粉丝
     * @param pageNum 当前页页码
     * @param pageSize 每页显示结果条数
     * @param tagIds 粉丝所在的标签组
     * @param queryStr 关键字搜索字符串
     * @param isBlacklist 是否在黑名单里查找
     * @return 粉丝信息列表
     * */
    Page<FanPO> listFansByPage(long weixinAppid, int pageNum, int pageSize, List<Integer> tagIds, String queryStr, int isBlacklist);

    /**
     * 用户订阅
     * @param appId 公众号Id
     * @param openId 粉丝openId
     * */
    void userSubscribe(String appId, String openId);

    /**
     * 用户取消订阅
     * @param appId 公众号Id
     * @param openId 粉丝openId
     * */
    void userUnsubscribe(String appId, String openId);

    /**
     * 更新用户个人信息
     * @param appId 公众号Id
     * @param openId 粉丝openId
     * @param hasInteract 是否有粉丝互动，1有0没有
     * @return 粉丝信息
     * */
    FanPO refreshUserInfo(long weixinAppid, String appId, String openId, boolean hasInteract);
}
