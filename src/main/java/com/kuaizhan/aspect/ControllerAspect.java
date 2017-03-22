package com.kuaizhan.aspect;

import com.kuaizhan.annotation.Validate;
import com.kuaizhan.exception.business.ParamException;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.utils.ParamUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


/**
 * 拦截controller 实现参数校验
 * Created by liangjiateng on 2017/3/21.
 */
@Component
@Aspect
public class ControllerAspect {

    @Pointcut("execution(* com.kuaizhan.controller.*.*(..))")
    private void controllerMethod() {

    }

    @Around("controllerMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //获取方法
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method m = methodSignature.getMethod();
        //获取参数
        Object[] args = pjp.getArgs();
        //获取参数注解
        Annotation[][] annotations = m.getParameterAnnotations();


        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof Validate) {
                    Validate validate = (Validate) annotations[i][j];
                    //获取参数名
                    String key = validate.key();
                    String pathToSchema = validate.path();
                    Object arg = args[i];
                    if ("".equals(pathToSchema)) {
                        //普通参数校验
                        try {
                            ParamUtil.validateRequestParam(pjp.getTarget().getClass().toString(), m.getName(), key, arg.toString());
                        } catch (ParamException e) {
                            return new JsonResponse(e.getCode(), e.getMsg(), null);
                        }
                    } else {
                        //post或者put参数校验
                        try {
                            ParamUtil.validatePostParam(arg.toString(), pathToSchema);
                        } catch (ParamException e) {
                            return new JsonResponse(e.getCode(), e.getMsg(), null);
                        }
                    }

                }
            }
        }

        Object re = pjp.proceed();
        return re;
    }


}
