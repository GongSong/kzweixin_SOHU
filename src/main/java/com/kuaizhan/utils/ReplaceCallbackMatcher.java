package com.kuaizhan.utils;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类似于php的preg_replace_callback正则匹配函数
 * 定义callback，对所有匹配的部分，调用callback实现替换
 *
 * Created by zixiong on 2017/3/29.
 */
public class ReplaceCallbackMatcher {

    /**
     * 回调Callback实现
     */
    public static interface Callback {
        /**
         * 根据matchResult获取要替换的字符串
         */
        public String getReplacement(Matcher matcher) throws Exception;
    }

    private final Pattern pattern;

    public ReplaceCallbackMatcher(String regex) {
        pattern = Pattern.compile(regex);
    }

    public String replaceMatches(String content, Callback callback) throws Exception {
        Matcher matcher = pattern.matcher(content);

        // 实现对原content的逐渐替换过程
        StringBuffer resultBuffer = new StringBuffer();

        while (matcher.find()){
            // 获取本次匹配的替换内容
            String replacement = callback.getReplacement(matcher);
            // 拼接替换后的字符串
            matcher.appendReplacement(resultBuffer, replacement);
        }

        // 拼接结尾
        matcher.appendTail(resultBuffer);

        return resultBuffer.toString();
    }
}
