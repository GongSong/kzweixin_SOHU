package com.kuaizhan.kzweixin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 枚举类型的typeHandler
 * Created by zixiong on 2017/07/30.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnumTypeHandler {

    Class target();

    int jdbcType();
}
