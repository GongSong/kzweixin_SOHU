package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.entity.fan.TagDTO;
import org.json.JSONObject;
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
     * @param newTag 新标签
     * */
    void updateTag(long weixinAppid, int tagId, String newTag);

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

}
