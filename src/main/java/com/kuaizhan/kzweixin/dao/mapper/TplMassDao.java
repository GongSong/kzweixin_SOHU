package com.kuaizhan.kzweixin.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by fangtianyu on 8/14/17.
 */
@Repository
public interface TplMassDao {
    /**
     * 对微信群发数据库发送成功字段进行增量更新
     * */
    void incSuccessCount(@Param("weixinAppid") long weixinAppid,
                         @Param("tplMassId") long tplMassId);

    /**
     * 对微信群发数据库发送失败（用户拒绝）字段进行增量更新
     * */
    void incRejectCount(@Param("weixinAppid") long weixinAppid,
                        @Param("tplMassId") long tplMassId);

    /**
     * 对微信群发数据库发送失败（其他原因）字段进行增量更新
     * */
    void incOtherFailedCount(@Param("weixinAppid") long weixinAppid,
                             @Param("tplMassId") long tplMassId);
}
