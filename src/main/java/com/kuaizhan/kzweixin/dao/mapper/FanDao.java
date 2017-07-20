package com.kuaizhan.kzweixin.dao.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;

import java.util.List;
import java.util.Map;

/**
 * 粉丝数据接口
 * Created by Mr.Jadyn on 2017/1/4.
 */
@Repository
public interface FanDao {
    /**
     * 批量更新粉丝信息
     *
     * @param fanses 粉丝列表
     * @param tables 表列表
     * @return
     */
    int updateFansBatch(@Param("fanses") List<FanPO> fanses, @Param("tables") List<String> tables);

    /**
     * 根据openId获取粉丝列表
     *
     * @param appId   公众号appId
     * @param openIds openId列表
     * @param tableName 分表表名
     * @return
     */
    List<FanPO> listFansByOpenIds(@Param("appId") String appId, @Param("openIds") List<String> openIds, @Param("tableName") String tableName);

    /**
     * 根据参数查找
     *
     * @param param  参数
     * @param tables 表列表
     * @return
     */
    List<FanPO> listFansByParam(@Param("param") Map param, @Param("tables") List<String> tables);

    /**
     * 获取单个粉丝信息
     */
    FanPO getFanByOpenId(@Param("openId") String openId, @Param("appId") String appId, @Param("tableName") String tableName);

    /**
     * 获取被删除的粉丝
     *
     * @param openId
     * @param tables
     * @return
     */
    FanPO getDeleteFanByOpenId(@Param("openId") String openId, @Param("appId") String appId, @Param("tables") List<String> tables);

    /**
     * 插入新粉丝
     *
     * @param fan      粉丝实体
     * @param tableName 表列表
     * @return
     */
    int insertFan(@Param("fans") FanPO fan, @Param("tableName") String tableName);

    /**
     * 更新粉丝
     *
     * @param fan   粉丝实体
     * @param tables 表列表
     * @return
     */
    int updateFan(@Param("fans") FanPO fan, @Param("tables") List<String> tables);

    /**
     * 查询粉丝数量
     *
     * @param isBlacklist 0白1黑
     * @param tagIds      标签id列表
     * @param queryStr    搜索关键词
     * @param tableName   表名
     * @return 粉丝数量
     */

    Long countFan(@Param("appId") String appId,
                  @Param("isBlacklist") Integer isBlacklist,
                  @Param("tagIds") List<Integer> tagIds,
                  @Param("queryStr") String queryStr,
                  @Param("tableName") String tableName);

    /**
     * 分页查询
     *
     * @param offset      查询偏移量
     * @param limit       最大查询数量
     * @param isBlacklist 0白1黑
     * @param tagIds      标签id列表
     * @param queryStr    搜索关键词
     * @param tableName   表名
     * @return 粉丝列表
     */
    List<FanPO> listFansByPage(@Param("appId") String appId,
                               @Param("offset") int offset,
                               @Param("limit") int limit,
                               @Param("isBlacklist") Integer isBlacklist,
                               @Param("tagIds") List<Integer> tagIds,
                               @Param("queryStr") String queryStr,
                               @Param("tableName") String tableName);

}
