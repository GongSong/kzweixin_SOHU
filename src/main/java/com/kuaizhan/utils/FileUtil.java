package com.kuaizhan.utils;

import com.kuaizhan.exception.business.DownloadFileFailedException;
import org.apache.log4j.Logger;

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

    private static final Logger logger = Logger.getLogger(FileUtil.class);


    static File download(String url, String urlHost) throws DownloadFileFailedException {
        InputStream inputStream = getInputStream(url, urlHost);
        if (inputStream == null){
            throw new DownloadFileFailedException("url: " + url);
        }
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
                if (fileOutputStream != null){
                    fileOutputStream.close();
                }
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
    private static InputStream getInputStream(String fileUrl, String fileHost) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(fileUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(10 * 1000);
            httpURLConnection.setRequestMethod("GET");
            if (fileHost != null){
                httpURLConnection.setRequestProperty("Host", fileHost);
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            logger.info("[getInputStream] get input stream failed, e:", e);
        }
        return inputStream;
    }
}
