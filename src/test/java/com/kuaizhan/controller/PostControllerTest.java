package com.kuaizhan.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;

/**
 * Created by liangjiateng on 2017/3/28.
 */
public class PostControllerTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void getPost() throws Exception {
        //正常场景
        given().param("weixinAppid", 601145633L).when().get("/v1/posts/1013250615").then().assertThat().body(matchesJsonSchemaInClasspath("json-schema/post/post-schema.json"));
        //异常场景
        //覆盖所有必选参数
        //组合可选参数
        //参数无或者null
        given().get("/v1/posts/1013250615").then().assertThat().body("code", equalTo(102002), "data", equalTo(null));

        //特殊字符

        //幂等

        //响应时间
        given().param("weixinAppid", 601145633L).when().get("/v1/posts/1013250615").then().time(lessThan(100L), TimeUnit.MILLISECONDS).assertThat().body(matchesJsonSchemaInClasspath("json-schema/post/post-schema.json"));
    }

    @Test
    public void getMultiPost() throws Exception {
        //正常场景
        given().param("weixinAppid", 601145633L).when().get("/v1/multi_posts/1013250615").then().assertThat().body(matchesJsonSchemaInClasspath("json-schema/post/post-multi-schema.json"));
        //异常场景
        //覆盖所有必选参数
        //组合可选参数
        //参数无或者null
        given().get("/v1/posts/1013250615").then().assertThat().body("code", equalTo(102002), "data", equalTo(null));

        //特殊字符

        //幂等

        //响应时间
        given().param("weixinAppid", 601145633L).when().get("/v1/multi_posts/1013250615").then().time(lessThan(100L), TimeUnit.MILLISECONDS).assertThat().body(matchesJsonSchemaInClasspath("json-schema/post/post-multi-schema.json"));
    }

    @Test
    public void insertPost() throws Exception {

    }

    @Test
    public void updatePost() throws Exception {

    }

    @Test
    public void deletePost() throws Exception {

    }

    @Test
    public void wxSyncsPost() throws Exception {

    }

    @Test
    public void kzSyncsPost() throws Exception {

    }


    @Test
    public void listPostByPagination() throws Exception {
        //正常场景
        given().param("weixinAppid", 601145633L,"page",1).when().get("/v1/posts").then().assertThat().body(matchesJsonSchemaInClasspath("json-schema/post/post-list-schema.json"));
        //异常场景
        //覆盖所有必选参数
        //组合可选参数
        given().param("weixinAppid", 601145633L).when().get("/v1/posts").then().assertThat().body("code", equalTo(101001), "data", equalTo(null));
        given().param("page", -1).when().get("/v1/posts").then().assertThat().body("code", equalTo(102002), "data", equalTo(null));
        //参数无或者null
        given().get("/v1/posts").then().assertThat().body("code", equalTo(102002), "data", equalTo(null));

        //特殊字符

        //幂等

        //响应时间
        given().param("weixinAppid", 601145633L,"page",1).when().get("/v1/posts").then().time(lessThan(100L), TimeUnit.MILLISECONDS).assertThat().body(matchesJsonSchemaInClasspath("json-schema/post/post-list-schema.json"));
    }

}