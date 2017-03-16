package com.kuaizhan.aspect;

import com.kuaizhan.exception.business.ParamException;
import com.kuaizhan.exception.system.*;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 拦截service 记录日志
 * Created by liangjiateng on 2017/3/6.
 */
@Component
@Aspect
public class ServiceAspect {

    private static Logger logger = Logger.getLogger(ServiceAspect.class);

    @Pointcut("execution(* cn.liangjiateng.service.impl.*.*(..))")
    private void serviceMethod() {

    }


    /**
     * 日志输出
     *
     * @param ex
     */
    @AfterThrowing(value = "serviceMethod()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        //系统级
        if (ex instanceof DaoException) {
            logger.error(output(((DaoException) ex).getDate(), ((DaoException) ex).getMsg(), ((DaoException) ex).getErrorStack()));
        } else if (ex instanceof RedisException) {
            logger.error(output(((RedisException) ex).getDate(), ((RedisException) ex).getMsg(), ((RedisException) ex).getErrorStack()));
        } else if (ex instanceof XMLParseException) {
            logger.error(output(((XMLParseException) ex).getDate(), ((XMLParseException) ex).getMsg(), ((XMLParseException) ex).getErrorStack()));
        } else if (ex instanceof JsonParseException) {
            logger.error(output(((JsonParseException) ex).getDate(), ((JsonParseException) ex).getMsg(), ((JsonParseException) ex).getErrorStack()));
        } else if (ex instanceof DecryptException) {
            logger.error(output(((DecryptException) ex).getDate(), ((DecryptException) ex).getMsg(), ((DecryptException) ex).getErrorStack()));
        } else if (ex instanceof EncryptException) {
            logger.error(output(((EncryptException) ex).getDate(), ((EncryptException) ex).getMsg(), ((EncryptException) ex).getErrorStack()));
        }
        //业务级
        else if (ex instanceof ParamException) {
            logger.error(output(((ParamException) ex).getDate(), ((ParamException) ex).getMsg(), ((ParamException) ex).getErrorStack()));
        }
    }

    private String output(long time, String msg, String errorStack) {
        return "Time:" + new Date(time).toString() + ";\nMessage:" + msg + "\nOriginal info:" + errorStack + ";";
    }
}
