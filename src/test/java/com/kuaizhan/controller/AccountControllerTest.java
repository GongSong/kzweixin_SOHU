package com.kuaizhan.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;


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
        //异常场景
        given().param("siteId", 32231231L).when().get("/v1/account").then().assertThat().body("code", equalTo(101002), "data", equalTo(null));
        //覆盖所有必选参数

        //组合可选参数

        //参数有无或者null
        given().when().get("/v1/account").then().assertThat().statusCode(400);
        //参数顺序个数和类型
        given().param("siteId", "123456").when().get("/v1/account").then().assertThat().statusCode(200);
        //参数数值范围
        given().param("siteId", 111111111111111111L).when().get("/v1/account").then().assertThat().body("code", equalTo(101002), "data", equalTo(null));
        given().param("siteId", -111111111111111111L).when().get("/v1/account").then().assertThat().body("code", equalTo(101002), "data", equalTo(null));
        //参数字符串长短 null max max+1

        //特殊字符

        //幂等

        //响应时间

    }

    @Test
    public void unbind() throws Exception {

    }

    @Test
    public void modifyAppSecret() throws Exception {

    }

}