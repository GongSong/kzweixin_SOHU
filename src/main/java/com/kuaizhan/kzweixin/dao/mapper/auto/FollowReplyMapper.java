package com.kuaizhan.kzweixin.dao.mapper.auto;

import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPO;
import com.kuaizhan.kzweixin.dao.po.auto.FollowReplyPOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowReplyMapper {
    long countByExample(FollowReplyPOExample example);

    int deleteByExample(FollowReplyPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FollowReplyPO record);

    int insertSelective(FollowReplyPO record);

    List<FollowReplyPO> selectByExampleWithBLOBsWithRowbounds(FollowReplyPOExample example, RowBounds rowBounds);

    List<FollowReplyPO> selectByExampleWithBLOBs(FollowReplyPOExample example);

    List<FollowReplyPO> selectByExampleWithRowbounds(FollowReplyPOExample example, RowBounds rowBounds);

    List<FollowReplyPO> selectByExample(FollowReplyPOExample example);

    FollowReplyPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FollowReplyPO record, @Param("example") FollowReplyPOExample example);

    int updateByExampleWithBLOBs(@Param("record") FollowReplyPO record, @Param("example") FollowReplyPOExample example);

    int updateByExample(@Param("record") FollowReplyPO record, @Param("example") FollowReplyPOExample example);

    int updateByPrimaryKeySelective(FollowReplyPO record);

    int updateByPrimaryKeyWithBLOBs(FollowReplyPO record);

    int updateByPrimaryKey(FollowReplyPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_follow
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int upsert(FollowReplyPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_follow
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int upsertSelective(FollowReplyPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_follow
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int upsertWithBLOBs(FollowReplyPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_follow
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int upsertSelectiveWithBLOBs(FollowReplyPO record);
}