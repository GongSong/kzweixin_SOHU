package com.kuaizhan.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.utils.Crc32Util;
import com.kuaizhan.utils.ReplaceCallbackMatcher;
import org.junit.Test;

import java.io.InputStream;
import java.util.Map;
import java.util.zip.CRC32;


/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    @Test
    public void test() throws Exception {
        String content = "我是各种奇怪的内容，啊啊啊，bbbb. wx_src=\"www.baidu.com\" 又是奇怪的内容 wx_scr=fafda, wx_src=\"还是有点\"";

        // 对wx_src垃圾数据进行清理，即wx_src标签下的内容，不是微信链接
        String wxSrcRegex = "(wx_src=)[\"'](?<wxSrc>[^\"']+?)[\"']";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(wxSrcRegex);
        content = callbackMatcher.replaceMatches(content,
                matcher -> {
                    String wxSrc = "替换后的内容";
                    return "wx_src=\"" + wxSrc + "\"";
                }
        );

        System.out.println("---->" + content);
    }

    @Test
    public void testInt() throws Exception {
    int tableNum = (int) (Crc32Util.getValue("wx6b4c894f8ddbb51f") % 128);
    System.out.println("---->" + tableNum);
    }

    @Test
    public void testJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream is = TmpTest.class.getResourceAsStream("/json/wx-sys-templates.json");
        Map map = objectMapper.readValue(is, Map.class);
        System.out.println("---->" + map);
        System.out.println("---->" + map.containsKey("OPENTM213512088"));
        System.out.println("---->" + map.get("title"));
        System.out.println("---->" + map.get("keywords"));
    }
}
