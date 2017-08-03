package com.kuaizhan.kzweixin.controller;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.constant.ErrorCode;

import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 做统一的controller输出处理
 * Created by liangjiateng on 2017/3/6.
 */
public abstract class BaseController {

    /**
     * 业务异常返回异常码和错误信息
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public JsonResponse handleBusinessException(BusinessException e){
        return new JsonResponse(e.getCode(), e.getMessage(), ImmutableMap.of());
    }

    /**
     * Hibernate Validate校验错误
     */
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
     * 处理RequestParam类型参数错误、ContentType错误
     */
    @ExceptionHandler({ServletRequestBindingException.class, HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public JsonResponse handleRequestParamException(Exception e) {
        return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(), e.getMessage(), ImmutableMap.of());
    }

    /**
     * 处理body参数错误、PathVariable类型参数错误
     * body为空 | 类型转换错误
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public JsonResponse handleMessageBodyMissing(Exception e) {
        String msg = e.getLocalizedMessage();
        if ("production".equals(ApplicationConfig.ENV_ALIAS)) {
            // 生产环境不向外暴露此异常信息
            msg = ErrorCode.PARAM_ERROR.getMessage();
        }
        return new JsonResponse(ErrorCode.PARAM_ERROR.getCode(), msg, ImmutableMap.of());
    }

    /**
     * 其他未知异常
     */
    @ExceptionHandler
    @ResponseBody
    public JsonResponse handleUnknownException(Exception e) {
        return new JsonResponse(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage(), ImmutableMap.of());
    }
}
