package com.kuaizhan.common;

import com.kuaizhan.utils.ReplaceCallbackMatcher;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.net.URI;
import java.util.regex.Matcher;

/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    @Test
    public void test() throws Exception {
        String content = "url('http://mmbiz.qpic.cn/mmbiz_png/Ipanel6KSKO6yRR3CwbickSMZarcdTzP1t7M0JlB1ZkhoCnzLiaS2OXFw9m8x6NseL08JxCPgqAxCic8ZeVuHrFEQ/0?wx_fmt=png&quot;\')";
        // 背景图中的微信图片转成快站链接
        ReplaceCallbackMatcher.Callback callback = new ReplaceCallbackMatcher.Callback() {
            @Override
            public String getReplacement(Matcher matcher) throws Exception {
                return "(" + "被替换的url" + ")";
            }
        };
        String regex = "\\((\"?'?https?:\\/\\/mmbiz[^)]+)\"?'?\\)";
        ReplaceCallbackMatcher replaceCallbackMatcher = new ReplaceCallbackMatcher(regex);
        content = replaceCallbackMatcher.replaceMatches(content, callback);
        System.out.println("---->" + content);
    }
}
