package com.kuaizhan.kzweixin.dao.mapper.auto;

import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.dao.po.auto.ActionPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ActionMapper {
    long countByExample(ActionPOExample example);

    int deleteByExample(ActionPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ActionPO record);

    int insertSelective(ActionPO record);

    List<ActionPO> selectByExampleWithBLOBsWithRowbounds(ActionPOExample example, RowBounds rowBounds);

    List<ActionPO> selectByExampleWithBLOBs(ActionPOExample example);

    List<ActionPO> selectByExampleWithRowbounds(ActionPOExample example, RowBounds rowBounds);

    List<ActionPO> selectByExample(ActionPOExample example);

    ActionPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ActionPO record, @Param("example") ActionPOExample example);

    int updateByExampleWithBLOBs(@Param("record") ActionPO record, @Param("example") ActionPOExample example);

    int updateByExample(@Param("record") ActionPO record, @Param("example") ActionPOExample example);

    int updateByPrimaryKeySelective(ActionPO record);

    int updateByPrimaryKeyWithBLOBs(ActionPO record);

    int updateByPrimaryKey(ActionPO record);
}