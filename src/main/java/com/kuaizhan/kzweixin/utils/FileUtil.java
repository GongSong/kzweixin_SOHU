package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.exception.common.InvalidBase64PicException;

import java.io.*;
import java.util.UUID;
import java.util.Base64;

/**
 * Created by fangtianyu on 7/3/17.
 */
public class FileUtil {

    /**
     * 将Base64格式的图片解码后的二进制内容写入文件
     * @throws InvalidBase64PicException 非法的Base64格式图片
     * @return filename
     * */
    public static String getFileFromeBase64(String url) throws InvalidBase64PicException {
        if(url == null || "".equals(url)) {
            return null;
        }
        if (url.indexOf("data:image/png;base64") != 0) {
            throw new InvalidBase64PicException("[getFileFromeBase64] failed, url:" + url);
        }
        String contentUrl = url.substring(22);

        // 随机名
        String filename = UUID.randomUUID().toString();
        byte[] imgContent = Base64.getDecoder().decode(contentUrl);
        ByteArrayInputStream imgContentIS = new ByteArrayInputStream(imgContent);

        try (
                OutputStream outputStream = new FileOutputStream(filename);
                InputStream inputStream = imgContentIS
        ) {
            int inByte;
            while ((inByte = inputStream.read()) != -1) {
                outputStream.write(inByte);
            }
        } catch (IOException e) {
            throw new InvalidBase64PicException("[getFileFromeBase64] failed, url:" + url);
        }

        return filename;
    }


}
