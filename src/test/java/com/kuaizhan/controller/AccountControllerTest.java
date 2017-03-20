package com.kuaizhan.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


/**
 * Created by liangjiateng on 2017/3/20.
 */

public class AccountControllerTest {
    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getAccountInfo() throws Exception {
        //正常场景
        given().param("siteId", 123456L).when().get("/v1/account").then().assertThat().body(matchesJsonSchemaInClasspath("json-schema/account-schema.json"));

        //边界测试

        //覆盖所有必选参数

        //组合可选参数

        //参数有无或者null

        //参数顺序个数和类型

        //参数数值范围

        //参数字符串长短 null max max+1

        //特殊字符

        //异常测试
        //幂等
        //并发
        //响应时间
        //吞吐数
        //并发量

    }

    @Test
    public void unbind() throws Exception {

    }

    @Test
    public void modifyAppSecret() throws Exception {

    }

}