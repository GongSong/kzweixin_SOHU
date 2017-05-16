package com.kuaizhan.dao.mapper;

import com.kuaizhan.pojo.po.MsgPO;
import com.kuaizhan.pojo.dto.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息数据接口 mysql数据源
 * Created by Mr.Jadyn on 2017/1/10.
 */
public interface MsgDao {


    /**
     * 查询消息数目
     *
     * @param appId   公众号appId
     * @param status  0删除1未读2已读3已回复
     * @param keyword 关键词
     * @param isHide  是否隐藏关键词查询
     * @param tables  表集合
     * @return
     */
    List<Long> count(@Param("appId") String appId, @Param("sendType") int sendType, @Param("status") int status, @Param("keyword") String keyword, @Param("isHide") Integer isHide, @Param("tables") List<String> tables);

    /**
     * 分页查询
     *
     * @param tables     表集合
     * @param pageEntity 分页实体
     * @return
     */
    List<MsgPO> listMsgsByPagination(@Param("tables") List<String> tables, @Param("pageEntity") Page pageEntity);

    /**
     * 获取新的消息
     *
     * @param appId  公众号appId
     * @param tables 表列表
     * @return
     */
    List<MsgPO> listNewMsgs(@Param("appId") String appId, @Param("tables") List<String> tables);

    /**
     * 获取用户已发消息列表
     *
     * @param tables 表列表
     * @return
     */
    List<MsgPO> listMsgsByOpenId(@Param("pageEntity") Page pageEntity, @Param("tables") List<String> tables);

    /**
     * 对消息进行批量更新
     *
     * @param tables 表列表
     * @param msgs   消息列表
     * @return
     */
    int updateMsgBatch(@Param("tables") List<String> tables, @Param("msgs") List<MsgPO> msgs);

    /**
     * 添加一条消息
     *
     * @param tableName 表名
     * @param msg       消息
     * @return
     */
    int insertMsg(@Param("tableName") String tableName, @Param("msg") MsgPO msg);


}
