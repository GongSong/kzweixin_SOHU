package com.kuaizhan.kzweixin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 异常监控工具类
 * Created by liangjiateng on 2017/3/27.
 */
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);


    public static void logMsg(Throwable e) {

        if (e instanceof com.kuaizhan.kzweixin.exception.BusinessException) {
            // 简单记录业务异常
            logger.info(e.toString());
        } else {
            logger.error("[Unknown Exception]", e);
        }
    }
}
