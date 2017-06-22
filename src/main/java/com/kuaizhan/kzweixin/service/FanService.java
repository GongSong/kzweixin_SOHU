package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;

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
}
