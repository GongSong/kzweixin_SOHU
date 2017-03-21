package com.kuaizhan.dao.mapper;

import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zixiong on 2017/3/20.
 */

public interface PostDao {

    /**
     * 查询图文消息总数
     * @param weixinAppid 微信appid
     * @return
     */
    Long count(long weixinAppid);

    /**
     * 查询图文消息列表
     * @param weixinAppid 微信appid
     * @param page
     * @return
     */
    List<PostDO> listPostsByPagination(@Param("weixinAppid") long weixinAppid, @Param("pageEntity") Page page);

    /**
     * 获取图文消息的多图文
     * @param mediaId 图文消息的mediaId
     * @return
     */
    List<PostDO> listMultiPosts(String mediaId);
}

