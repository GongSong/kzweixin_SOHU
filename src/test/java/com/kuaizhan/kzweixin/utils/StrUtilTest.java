package com.kuaizhan.kzweixin.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StrUtilTest {
    @Test
    public void replaceUtf8mb4() throws Exception {
        System.out.println("---->" + StrUtil.replaceUtf8mb4("a中文都健康\uD87E\uDC25b", "\uFFFD"));
    }

}