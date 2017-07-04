package com.kuaizhan.kzweixin.utils;

import java.io.*;
import java.util.UUID;
import java.util.Base64;
import java.util.regex.*;

/**
 * Created by fangtianyu on 7/3/17.
 */
public class FileUtil {

    /**
     * 将Base64格式的图片解码后的二进制内容写入文件
     * @throws IllegalArgumentException 非法的Base64格式图片
     * @return filename
     * */
    public static String getImgFromBase64(String str) throws IllegalArgumentException {
        String strRegex = "^data:image/\\w+;base64,(?<str>.*)$";
        Pattern pattern = Pattern.compile(strRegex);
        Matcher matcher = pattern.matcher(str);

        if (!matcher.find()) {
            throw new IllegalArgumentException("[getImgFromBase64] failed, str:" + str);
        }
        String contentStr = matcher.group("str");

        // 随机名
        String filename = UUID.randomUUID().toString();
        byte[] imgContent = Base64.getDecoder().decode(contentStr);

        try (
                OutputStream outputStream = new FileOutputStream(filename);
                InputStream inputStream = new ByteArrayInputStream(imgContent)
        ) {
            int inByte;
            while ((inByte = inputStream.read()) != -1) {
                outputStream.write(inByte);
            }
        } catch (IOException e) {
            throw new RuntimeException("[getImgFromBase64] failed, str:" + str);
        }

        return filename;
    }


}
