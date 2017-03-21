package com.kuaizhan.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by liangjiateng on 2017/3/21.
 */
public class ParamUtilTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkUnbindPostData() throws Exception {
        String postData="{\n" +
                "\t\"type\":1,\n" +
                "\t\"text\":\"哈哈哈哈\"\n" +
                "}";
        ParamUtil.checkUnbindPostData(postData);
    }

}