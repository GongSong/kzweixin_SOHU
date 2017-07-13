package com.kuaizhan.kzweixin.dao.mapper.auto;

import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface MassMapper {
    long countByExample(MassPOExample example);

    int deleteByExample(MassPOExample example);

    int deleteByPrimaryKey(Long massId);

    int insert(MassPO record);

    int insertSelective(MassPO record);

    List<MassPO> selectByExampleWithBLOBsWithRowbounds(MassPOExample example, RowBounds rowBounds);

    List<MassPO> selectByExampleWithBLOBs(MassPOExample example);

    List<MassPO> selectByExampleWithRowbounds(MassPOExample example, RowBounds rowBounds);

    List<MassPO> selectByExample(MassPOExample example);

    MassPO selectByPrimaryKey(Long massId);

    int updateByExampleSelective(@Param("record") MassPO record, @Param("example") MassPOExample example);

    int updateByExampleWithBLOBs(@Param("record") MassPO record, @Param("example") MassPOExample example);

    int updateByExample(@Param("record") MassPO record, @Param("example") MassPOExample example);

    int updateByPrimaryKeySelective(MassPO record);

    int updateByPrimaryKeyWithBLOBs(MassPO record);

    int updateByPrimaryKey(MassPO record);
}