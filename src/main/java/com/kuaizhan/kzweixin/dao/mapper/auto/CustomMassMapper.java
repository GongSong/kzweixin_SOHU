package com.kuaizhan.kzweixin.dao.mapper.auto;

import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPO;
import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CustomMassMapper {
    long countByExample(CustomMassPOExample example);

    int deleteByExample(CustomMassPOExample example);

    int deleteByPrimaryKey(Long customMassId);

    int insert(CustomMassPO record);

    int insertSelective(CustomMassPO record);

    List<CustomMassPO> selectByExampleWithBLOBsWithRowbounds(CustomMassPOExample example, RowBounds rowBounds);

    List<CustomMassPO> selectByExampleWithBLOBs(CustomMassPOExample example);

    List<CustomMassPO> selectByExampleWithRowbounds(CustomMassPOExample example, RowBounds rowBounds);

    List<CustomMassPO> selectByExample(CustomMassPOExample example);

    CustomMassPO selectByPrimaryKey(Long customMassId);

    int updateByExampleSelective(@Param("record") CustomMassPO record, @Param("example") CustomMassPOExample example);

    int updateByExampleWithBLOBs(@Param("record") CustomMassPO record, @Param("example") CustomMassPOExample example);

    int updateByExample(@Param("record") CustomMassPO record, @Param("example") CustomMassPOExample example);

    int updateByPrimaryKeySelective(CustomMassPO record);

    int updateByPrimaryKeyWithBLOBs(CustomMassPO record);

    int updateByPrimaryKey(CustomMassPO record);
}