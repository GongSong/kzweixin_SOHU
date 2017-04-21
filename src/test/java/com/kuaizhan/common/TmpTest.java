package com.kuaizhan.common;

import com.kuaizhan.utils.ReplaceCallbackMatcher;
import org.junit.Test;

import java.util.regex.Matcher;

/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    @Test
    public void test() throws Exception {
        String content = "我是多余的内容你听过url('&quot;http://mmbiz.qpic.cn/&quot;mmbiz_png/Ipanel6KSKO6yRR3CwbickSMZarcdTzP1t7M0JlB1ZkhoCnzLiaS2OXFw9m8x6NseL08JxCPgqAxCic8ZeVuHrFEQ/0?wx_fmt=png&quot;\")hahdfa";
        // 背景图中的微信图片转成快站链接
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {

                System.out.println("----> goupCount:" + matcher.groupCount());
                String result = "url(" + "被替换的url" + ")";
                System.out.println("----> origin: " + matcher.group(0) + " result:" + result);
                return result;
            }
        };
        String regex = "url\\(\"?'?(?:&quot;)?(https?:\\/\\/mmbiz[^)]+?)(?:&quot;)?\"?'?\\)";
        ReplaceCallbackMatcher replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content, callback);
        System.out.println("---->" + content);
    }
}
