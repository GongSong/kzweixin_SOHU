package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BaseException;

import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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
            return new JsonResponse(((BaseException) ex).getCode(), ((BaseException) ex).getMsg(), ImmutableMap.of());
        }
        //自定义spring @RequestParam 异常
        else if (ex instanceof ServletRequestBindingException) {
            return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(), ErrorCode.PARAM_ERROR.getMessage(), ImmutableMap.of());
        } else {
            ex.printStackTrace();
            return new JsonResponse(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage(), ImmutableMap.of());
        }
    }

    /**
     * 业务异常返回异常码和错误信息
     */
    @ExceptionHandler
    @ResponseBody
    public JsonResponse handleBusinessException(BusinessException e){
        return new JsonResponse(e.getCode(), e.getMessage(), ImmutableMap.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public JsonResponse handleValidationException(MethodArgumentNotValidException e) {

        String msg = "";
        for (ObjectError er : e.getBindingResult().getAllErrors()) {
            msg = er.getDefaultMessage();
        }
        return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(), msg, ImmutableMap.of());

    }

    /**
     * Json body 不符合要求
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public JsonResponse handleMessageBodyMissing(Exception e) {
        return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(), "request body can not be null and only accept application/json", ImmutableMap.of());
    }

}
