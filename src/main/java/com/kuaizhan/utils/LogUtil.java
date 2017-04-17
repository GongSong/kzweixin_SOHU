package com.kuaizhan.utils;


import com.kuaizhan.exception.business.BusinessException;
import com.kuaizhan.exception.system.SystemException;
import org.apache.log4j.Logger;

import java.util.Date;


/**
 * 异常监控工具类
 * Created by liangjiateng on 2017/3/27.
 */
public class LogUtil {

    private static Logger logger = Logger.getLogger(LogUtil.class);


    public static void logMsg(Exception e) {

        if (e instanceof SystemException) {
            logger.fatal(output(((SystemException) e).getDate(), ((SystemException) e).getMsg(), ((SystemException) e).getErrorStack()));
        } else if (e instanceof BusinessException) {
            logger.warn(output(((BusinessException) e).getDate(), ((BusinessException) e).getMsg(), ((BusinessException) e).getErrorStack()));
        } else {
            logger.error("[未知错误]", e);
        }
    }

    private static String output(long time, String msg, String errorStack) {
        return "Message:" + msg + "\nTime:" + new Date(time).toString() + "\nOriginal info:" + errorStack + ";";
    }
}
