package com.kuaizhan.controller;

import com.kuaizhan.exception.business.ParamException;
import com.kuaizhan.exception.system.*;
import com.kuaizhan.pojo.VO.JsonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 做统一的controller输出处理
 * Created by liangjiateng on 2017/3/6.
 */
public abstract class BaseController {

    @ExceptionHandler
    @ResponseBody
    public JsonResponse handleException(Exception ex) {
        //系统级
        if (ex instanceof DaoException) {
            return new JsonResponse(((DaoException) ex).getCode(), ((DaoException) ex).getMsg(), null);
        } else if (ex instanceof RedisException) {
            return new JsonResponse(((RedisException) ex).getCode(), ((RedisException) ex).getMsg(), null);
        } else if (ex instanceof XMLParseException) {
            return new JsonResponse(((XMLParseException) ex).getCode(), ((XMLParseException) ex).getMsg(), null);
        } else if (ex instanceof JsonParseException) {
            return new JsonResponse(((JsonParseException) ex).getCode(), ((JsonParseException) ex).getMsg(), null);
        } else if (ex instanceof DecryptException) {
            return new JsonResponse(((DecryptException) ex).getCode(), ((DecryptException) ex).getMsg(), null);
        } else if (ex instanceof EncryptException) {
            return new JsonResponse(((EncryptException) ex).getCode(), ((EncryptException) ex).getMsg(), null);
        }
        //业务级
        else if (ex instanceof ParamException) {
            return new JsonResponse(((ParamException) ex).getCode(), ((ParamException) ex).getMsg(), null);
        }
        return null;
    }

}
