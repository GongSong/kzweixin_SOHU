package com.kuaizhan.kzweixin.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 模板消息mapper
 * Created by zixiong on 2017/5/13.
 */
@Repository
public interface TplDao {
    /**
     * 根据tplIdShort获取用户的tplId
     */
    String getTplId(@Param("weixinAppid") long weixinAppid, @Param("tplIdShort") String tplIdShort);

    /**
     * 设置用户的tplId
     * @param tplIdShort 模板的编号
     * @param tplId 微信后台为用户生成的模板唯一id
     */
    void addTplId(@Param("weixinAppid") long weixinAppid, @Param("tplIdShort") String tplIdShort, @Param("tplId") String tplId);

    /**
     * 删除用户tplId
     */
    boolean deleteTpl(@Param("weixinAppid") long weixinAppid, @Param("tplIdShort") String tplIdShort);
}
