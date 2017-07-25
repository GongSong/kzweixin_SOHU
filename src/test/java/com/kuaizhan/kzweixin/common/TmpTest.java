package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.entity.action.NewsResponse;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.kuaizhan.kzweixin.utils.ReplaceCallbackMatcher;
import com.kuaizhan.kzweixin.utils.StrUtil;
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
    public void testXml() throws Exception {
        String str = "da\uD83E\uDD17fa";
        System.out.println("---->" + StrUtil.removeEmojis(str));
    }

    @Test
    public void testInt() throws Exception {
        String str = "{\"news\":[{\"title\":\"我的投票\",\"description\":\"null\",\"picUrl\":\"http://www.baidu.com\",\"url\":\"https://vote15d39e4b0f5.kuaizhan.com/mobile.html#/list\"}]}\n";
        System.out.println("---->" + JsonUtil.string2Bean(str, NewsResponse.class));
    }
}
