package com.kuaizhan.kzweixin.utils;


import com.kuaizhan.kzweixin.exception.deprecated.business.BusinessException;
import com.kuaizhan.kzweixin.exception.deprecated.system.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * 异常监控工具类
 * Created by liangjiateng on 2017/3/27.
 */
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);


    public static void logMsg(Throwable e) {

        if (e instanceof SystemException) {
            logger.error(output(((SystemException) e).getDate(), ((SystemException) e).getMsg(), ((SystemException) e).getErrorStack()), e);
        } else if (e instanceof BusinessException) {
            logger.warn(output(((BusinessException) e).getDate(), ((BusinessException) e).getMsg(), ((BusinessException) e).getErrorStack()), e);
        } else if (e instanceof com.kuaizhan.kzweixin.exception.BusinessException) {
            // 简单记录业务异常
            logger.info(e.toString());
        } else {
            logger.error("[Unknown Exception]", e);
        }
    }

    private static String output(long time, String msg, String errorStack) {
        return "Message:" + msg + "\nTime:" + new Date(time).toString() + "\nOriginal info:" + errorStack + ";";
    }
}
