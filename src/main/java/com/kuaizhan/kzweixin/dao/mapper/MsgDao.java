package com.kuaizhan.kzweixin.dao.mapper;

import com.kuaizhan.kzweixin.dao.po.MsgPO;
import com.kuaizhan.kzweixin.entity.common.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息数据接口 mysql数据源
 * Created by Mr.Jadyn on 2017/1/10.
 */
@Repository
public interface MsgDao {


    /**
     * 查询消息数目
     *
     * @param tableName 分表的表名
     * @param openId 如果指定openId，则只筛选相关openId
     * @param sendType 如果指定sendType, 则只筛选相关sendType
     * @param queryStr 内容查询条件，有此参数，则限定了文字消息
     * @param filterKeywords 是否过滤关键词消息
     * @param endTime createTime的最大时间
     */
    long countMsgs(@Param("appId") String appId,
                   @Param("tableName") String tableName,
                   @Param("openId") String openId,
                   @Param("sendType") Integer sendType,
                   @Param("queryStr") String queryStr,
                   @Param("filterKeywords") Boolean filterKeywords,
                   @Param("startTime") Long startTime,
                   @Param("endTime") Long endTime);

    /**
     * 分页查询消息列表
     * @param tableName 分表的表名
     * @param openId 如果指定openId，则只筛选相关openId
     * @param sendType 如果指定sendType, 则只筛选相关sendType
     * @param queryStr 内容查询条件，有此参数，则限定了文字消息
     * @param filterKeywords 是否过滤关键词消息
     * @param endTime createTime的最大时间
     */
    List<MsgPO> listMsgsByPagination(@Param("appId") String appId,
                                     @Param("tableName") String tableName,
                                     @Param("openId") String openId,
                                     @Param("sendType") Integer sendType,
                                     @Param("queryStr") String queryStr,
                                     @Param("filterKeywords") Boolean filterKeywords, @Param("endTime") Long endTime,
                                     @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 获取用户已发消息列表
     *
     * @param tables 表列表
     * @return
     */
    List<MsgPO> listMsgsByOpenId(@Param("pageEntity") Page pageEntity, @Param("tables") List<String> tables);

    /**
     * 添加一条消息
     *
     * @param tableName 表名
     * @param msg       消息
     * @return
     */
    int insertMsg(@Param("tableName") String tableName, @Param("msg") MsgPO msg);
}
