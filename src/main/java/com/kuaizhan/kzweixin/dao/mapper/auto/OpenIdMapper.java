package com.kuaizhan.kzweixin.dao.mapper.auto;

import com.kuaizhan.kzweixin.dao.po.auto.OpenIdPO;
import com.kuaizhan.kzweixin.dao.po.auto.OpenIdPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface OpenIdMapper {
    long countByExample(@Param("example") OpenIdPOExample example, @Param("tableName") String tableName);

    int deleteByExample(@Param("example") OpenIdPOExample example, @Param("tableName") String tableName);

    int deleteByPrimaryKey(@Param("id") Integer id, @Param("tableName") String tableName);

    int insert(@Param("record") OpenIdPO record, @Param("tableName") String tableName);

    int insertSelective(@Param("record") OpenIdPO record, @Param("tableName") String tableName);

    List<OpenIdPO> selectByExampleWithRowbounds(@Param("rowBounds") OpenIdPOExample example, RowBounds rowBounds, @Param("tableName") String tableName);

    List<OpenIdPO> selectByExample(@Param("example") OpenIdPOExample example, @Param("tableName") String tableName);

    OpenIdPO selectByPrimaryKey(@Param("id") Integer id, @Param("tableName") String tableName);

    int updateByExampleSelective(@Param("record") OpenIdPO record, @Param("example") OpenIdPOExample example, @Param("tableName") String tableName);

    int updateByExample(@Param("record") OpenIdPO record, @Param("example") OpenIdPOExample example, @Param("tableName") String tableName);

    int updateByPrimaryKeySelective(@Param("record") OpenIdPO record, @Param("tableName") String tableName);

    int updateByPrimaryKey(@Param("record") OpenIdPO record, @Param("tableName") String tableName);
}