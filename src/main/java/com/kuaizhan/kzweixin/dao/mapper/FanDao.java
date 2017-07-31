package com.kuaizhan.kzweixin.dao.mapper;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.kuaizhan.kzweixin.dao.po.auto.FanPO;

import java.util.List;

/**
 * 粉丝数据接口
 * Created by Mr.Jadyn on 2017/1/4.
 */
@Repository
public interface FanDao {
    /**
     * 查询粉丝数量
     *
     * @param baseInteractTime  当前时间前48小时的基准时间
     * @param isBlacklist       0白1黑
     * @param tagIds            标签id列表
     * @param queryStr          搜索关键词
     * @param tableName         表名
     * @return 粉丝数量
     */

    Long countFan(@Param("appId") String appId,
                  @Param("baseInteractTime") Integer baseInteractTime,
                  @Param("isBlacklist") int isBlacklist,
                  @Param("tagIds") List<Integer> tagIds,
                  @Param("queryStr") String queryStr,
                  @Param("tableName") String tableName);

    /**
     * 分页查询
     *
     * @param offset            查询偏移量
     * @param limit             最大查询数量
     * @param baseInteractTime  当前时间前48小时的基准时间
     * @param isBlacklist       0白1黑
     * @param tagIds            标签id列表
     * @param queryStr          搜索关键词
     * @param tableName         表名
     * @return 粉丝列表
     */
    List<FanPO> listFansByPage(@Param("appId") String appId,
                               @Param("offset") int offset,
                               @Param("limit") int limit,
                               @Param("baseInteractTime") Integer baseInteractTime,
                               @Param("isBlacklist") int isBlacklist,
                               @Param("tagIds") List<Integer> tagIds,
                               @Param("queryStr") String queryStr,
                               @Param("tableName") String tableName);

    /**
     * 根据openId获取粉丝列表
     *
     * @param appId   公众号appId
     * @param openIds openId列表
     * @param tableName 分表表名
     */
    List<FanPO> listFansByOpenIds(@Param("appId") String appId, @Param("openIds") List<String> openIds, @Param("tableName") String tableName);
}
