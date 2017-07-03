package com.kuaizhan.kzweixin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类似于php的preg_replace_callback正则匹配函数
 * 定义callback，对所有匹配的部分，调用callback实现替换
 *
 * Created by zixiong on 2017/3/29.
 */
public class ReplaceCallbackMatcher {

    private static final Logger logger = LoggerFactory.getLogger(ReplaceCallbackMatcher.class);

    private final Pattern pattern;

    public ReplaceCallbackMatcher(String regex) {
        pattern = Pattern.compile(regex);
    }

    public String replaceMatches(String content, Callback callback) {

        Matcher matcher = pattern.matcher(content);

        // 实现对原content的逐渐替换过程
        StringBuffer resultBuffer = new StringBuffer();
        while (matcher.find()){
            // 获取本次匹配的替换内容, 使用quoteReplacement使得'$'和'\'成为literal string。
            String replacement = Matcher.quoteReplacement(callback.getReplacement(matcher));
            // 拼接替换后的字符串
            matcher.appendReplacement(resultBuffer, replacement);
        }
        // 拼接结尾
        matcher.appendTail(resultBuffer);

        return resultBuffer.toString();
    }

    /**
     * 回调接口
     */
    public interface Callback {
        /**
         * 根据matchResult获取要替换的字符串
         */
        String getReplacement(Matcher matcher);
    }

}
