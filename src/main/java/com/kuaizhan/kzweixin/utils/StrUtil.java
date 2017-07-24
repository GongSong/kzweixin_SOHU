package com.kuaizhan.kzweixin.utils;

import com.vdurmont.emoji.EmojiParser;

/**
 * Created by zixiong on 2017/6/27.
 */
public class StrUtil {
    /**
     * 删除字符串的Emoji
     */
    public static String removeEmojis(String str) {
        if (str == null) {
            return null;
        }
        return EmojiParser.removeAllEmojis(str);
    }

    /**
     * 截取str的长度
     */
    public static String chopStr(String str, int max) {
        if (str == null) {
            return null;
        }
        return str.length() > max ? str.substring(0, max): str;
    }

    /**
     * 把需要4个字节编码的utf8字符用'�'代替
     */
    public static String replaceUtf8mb4(String str) {
        return replaceUtf8mb4(str, "\uFFFD");
    }

    /**
     * 把需要4个字节编码的utf8字符用指定字符替换
     */
    public static String replaceUtf8mb4(String str, String replacement) {
        if (str == null) {
            return null;
        }
        return str.replaceAll("[^\\u0000-\\uFFFF]", replacement);
    }
}
