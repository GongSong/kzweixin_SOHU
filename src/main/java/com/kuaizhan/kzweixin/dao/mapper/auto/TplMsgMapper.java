package com.kuaizhan.kzweixin.dao.mapper.auto;

import com.kuaizhan.kzweixin.dao.po.auto.TplMsgPO;
import com.kuaizhan.kzweixin.dao.po.auto.TplMsgPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TplMsgMapper {
    long countByExample(TplMsgPOExample example);

    int deleteByExample(TplMsgPOExample example);

    int deleteByPrimaryKey(Long msgId);

    int insert(TplMsgPO record);

    int insertSelective(TplMsgPO record);

    List<TplMsgPO> selectByExampleWithBLOBsWithRowbounds(TplMsgPOExample example, RowBounds rowBounds);

    List<TplMsgPO> selectByExampleWithBLOBs(TplMsgPOExample example);

    List<TplMsgPO> selectByExampleWithRowbounds(TplMsgPOExample example, RowBounds rowBounds);

    List<TplMsgPO> selectByExample(TplMsgPOExample example);

    TplMsgPO selectByPrimaryKey(Long msgId);

    int updateByExampleSelective(@Param("record") TplMsgPO record, @Param("example") TplMsgPOExample example);

    int updateByExampleWithBLOBs(@Param("record") TplMsgPO record, @Param("example") TplMsgPOExample example);

    int updateByExample(@Param("record") TplMsgPO record, @Param("example") TplMsgPOExample example);

    int updateByPrimaryKeySelective(TplMsgPO record);

    int updateByPrimaryKeyWithBLOBs(TplMsgPO record);

    int updateByPrimaryKey(TplMsgPO record);
}