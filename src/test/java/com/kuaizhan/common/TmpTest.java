package com.kuaizhan.common;

import com.kuaizhan.utils.JsonUtil;
import com.kuaizhan.utils.ReplaceCallbackMatcher;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.*;
import java.util.regex.Matcher;


/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    @Test
    public void test() throws Exception {
        String content = "我是各种奇怪的内容，啊啊啊，bbbb. wx_src=\"www.baidu.com\" 又是奇怪的内容 wx_scr=fafda, wx_src=\"还是有点\"";

        // 对wx_src垃圾数据进行清理，即wx_src标签下的内容，不是微信链接
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                String wxSrc = matcher.group("wxSrc");
                wxSrc = "替换后的内容";
                return "wx_src=\"" + wxSrc + "\"";
            }
        };

        String wxSrcRegex = "(wx_src=)[\"'](?<wxSrc>[^\"']+?)[\"']";
        ReplaceCallbackMatcher callbackMatcher = new ReplaceCallbackMatcher(wxSrcRegex);
        content = callbackMatcher.replaceMatches(content, callback);

        System.out.println("---->" + content);
    }
}
