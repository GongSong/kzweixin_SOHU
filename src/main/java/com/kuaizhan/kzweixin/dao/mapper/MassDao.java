package com.kuaizhan.kzweixin.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by fangtianyu on 8/14/17.
 */
@Repository
public interface MassDao {
    /**
     * 对微信群发数据库发送成功字段进行增量更新
     * */
    void incSentCount(@Param("weixinAppid") long weixinAppid,
                         @Param("massId") long massId);

    /**
     * 对微信群发数据库发送失败字段进行增量更新
     * */
    void incErrorCount(@Param("weixinAppid") long weixinAppid,
                          @Param("massId") long massId);
}
