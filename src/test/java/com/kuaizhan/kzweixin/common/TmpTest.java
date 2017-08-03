package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.entity.wxresponse.NewsResponse;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.ReplaceCallbackMatcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    private static final Logger logger = LoggerFactory.getLogger(TmpTest.class);

    @Test
    public void testXml() throws Exception {
    }

    @Test
    public void testInt() throws Exception {
        String str = "{\"news\":[{\"oldTitle\":\"我的投票\",\"oldDescription\":\"null\",\"oldPicUrl\":\"http://www.baidu.com\",\"oldUrl\":\"https://vote15d39e4b0f5.kuaizhan.com/mobile.html#/list\"}]}\n";
        System.out.println("---->" + JsonUtil.string2Bean(str, NewsResponse.class));
    }
}
