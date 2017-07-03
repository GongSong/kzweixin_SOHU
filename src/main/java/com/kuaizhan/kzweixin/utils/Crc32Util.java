package com.kuaizhan.kzweixin.utils;

import java.util.zip.CRC32;

/**
 * Created by zixiong on 2017/5/16.
 */
public class Crc32Util {

    /**
     * 获取字符串的Crc32值
     */
    public static long getValue(String str) {
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        return crc32.getValue();
    }
}
