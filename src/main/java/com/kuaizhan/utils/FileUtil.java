package com.kuaizhan.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liangjiateng on 2017/3/29.
 */
public class FileUtil {


    public static File download(String url) {
        InputStream inputStream = getInputStream(url);
        FileOutputStream fileOutputStream = null;
        byte[] data = new byte[1024];
        int len;
        String fileName = System.currentTimeMillis() + ".jpg";

        try {
            fileOutputStream = new FileOutputStream(fileName);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new File(fileName);
    }

    /**
     * 下载文件url转换成流
     *
     * @param fileUrl
     * @return
     */
    private static InputStream getInputStream(String fileUrl) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(fileUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
