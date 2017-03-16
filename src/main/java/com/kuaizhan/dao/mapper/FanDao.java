package com.kuaizhan.dao.mapper;


import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 粉丝数据接口
 * Created by Mr.Jadyn on 2017/1/4.
 */
public interface FanDao {
    /**
     * 查询数量
     *
     * @param appId   公众号appId
     * @param isBlack 0白1黑
     * @param tagIds  标签id列表
     * @param keyword 搜索关键词
     * @param tables  表列表
     * @return
     */

    List<Long> count(@Param("appId") String appId, @Param("inBlackList") Integer isBlack, @Param("tagIds") List<Integer> tagIds, @Param("keyword") String keyword, @Param("tables") List<String> tables);

    /**
     * 分页查询
     *
     * @param page 分页封装类
     * @param tables     表列表
     * @return
     */
    List<FanDO> listFansByPagination(@Param("pageEntity") Page page, @Param("tables") List<String> tables);

    /**
     * 批量更新粉丝信息
     *
     * @param fanses 粉丝列表
     * @param tables 表列表
     * @return
     */
    int updateFansBatch(@Param("fanses") List<FanDO> fanses, @Param("tables") List<String> tables);

    /**
     * 根据openId获取粉丝列表
     *
     * @param appId   公众号appId
     * @param openIds 用户openId
     * @param tables  表列表
     * @return
     */
    List<FanDO> listFansByOpenIds(@Param("appId") String appId, @Param("openIds") List<String> openIds, @Param("tables") List<String> tables);

    /**
     * 根据参数查找
     *
     * @param param  参数
     * @param tables 表列表
     * @return
     */
    List<FanDO> listFansByParam(@Param("param") Map param, @Param("tables") List<String> tables);

    /**
     * 获取被删除的粉丝
     *
     * @param openId
     * @param tables
     * @return
     */
    FanDO getDeleteFanByOpenId(@Param("openId") String openId, @Param("appId") String appId, @Param("tables") List<String> tables);

    /**
     * 插入新粉丝
     *
     * @param fan      粉丝实体
     * @param tableName 表列表
     * @return
     */
    int insertFan(@Param("fans") FanDO fan, @Param("tableName") String tableName);

    /**
     * 更新粉丝
     *
     * @param fan   粉丝实体
     * @param tables 表列表
     * @return
     */
    int updateFan(@Param("fans") FanDO fan, @Param("tables") List<String> tables);

}
