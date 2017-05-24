package com.kuaizhan.utils;

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

    /**
     * 回调Callback实现
     */
    public interface Callback {
        /**
         * 根据matchResult获取要替换的字符串
         */
        String getReplacement(Matcher matcher) throws Exception;
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
            try {
                matcher.appendReplacement(resultBuffer, replacement);
            } catch (IllegalArgumentException e) {
                // TODO: IllegalArgumentException 被发现
                // 临时记录日志，追踪问题
                logger.error("[ReplaceCallbackMatcher] IllegalArgumentException found, pattern:{} replacement:{}", pattern, replacement);
                throw e;
            }
        }

        // 拼接结尾
        matcher.appendTail(resultBuffer);

        return resultBuffer.toString();
    }
}
