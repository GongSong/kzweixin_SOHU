package com.kuaizhan.controller;

import com.kuaizhan.exception.BaseException;

import com.kuaizhan.exception.business.ParamException;
import com.kuaizhan.exception.system.ServerException;
import com.kuaizhan.pojo.VO.JsonResponse;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
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
        if (ex instanceof BaseException) {
            return new JsonResponse(((BaseException) ex).getCode(), ((BaseException) ex).getMsg(), null);
        }
        //自定义spring @RequestParam 异常
        else if (ex instanceof ServletRequestBindingException) {
            ParamException paramException = new ParamException();
            return new JsonResponse(paramException.getCode(), paramException.getMsg(), null);
        } else {
            // TODO: 此处可能忽略掉异常
            ServerException serverException = new ServerException();
            return new JsonResponse(serverException.getCode(), serverException.getMsg(), null);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonResponse handleValidationException(MethodArgumentNotValidException e) {

        String msg = "";
        for (ObjectError er : e.getBindingResult().getAllErrors()) {
            msg = er.getDefaultMessage();
        }
        ParamException paramException = new ParamException();
        return new JsonResponse(paramException.getCode(), msg, null);

    }

}
