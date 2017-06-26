package com.kuaizhan.kzweixin.service;

/**
 * 公共service
 * Created by zixiong on 2017/6/26.
 */
public interface CommonService {
    /**
     * 统计+1
     * @param traceId 业务id
     * @param traceKey 业务key
     */
    void kzStat(String traceId, String traceKey);
}
