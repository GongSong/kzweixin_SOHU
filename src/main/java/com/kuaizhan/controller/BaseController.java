package com.kuaizhan.controller;

import com.kuaizhan.exception.BaseException;
import com.kuaizhan.exception.business.AccountNotExistException;
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
    public JsonResponse handleException(BaseException ex) {
        return new JsonResponse(ex.getCode(), ex.getErrorStack(), null);
    }

}
