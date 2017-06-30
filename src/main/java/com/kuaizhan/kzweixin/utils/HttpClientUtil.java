package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.exception.common.DownloadFileFailedException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * http请求工具类
 * Created by Mr.Jadyn on 2016/12/28.
 */
public final class HttpClientUtil {


    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * 发送get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, Map<String, String> headers) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(10 * 1000)
                    .build();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, CHARSET_UTF_8);
        } catch (IOException e) {
            logger.error("[HttpClientUtil.get] get failed, url:{} headers:{}", url, headers , e);
        } finally {
            doClose(response);
            doClose(httpClient);
        }
        return null;
    }

    /**
     * 使用unirest下载文件
     * @throws DownloadFileFailedException 下载文件失败
     */
    public static String downloadFile(String url) throws DownloadFileFailedException {

        // 随机名
        String filename = UUID.randomUUID().toString();

        HttpResponse<InputStream> response;
        try {
            response = Unirest.get(url).asBinary();
        } catch (UnirestException e) {
            throw new DownloadFileFailedException("[downloadFile] failed", e);
        }

        OutputStream outputStream = null;
        InputStream inputStream = response.getRawBody();
        try {
            outputStream = new FileOutputStream(filename);
            int inByte;
            while ((inByte = inputStream.read()) != -1) {
                outputStream.write(inByte);
            }
        } catch (IOException e) {
            throw new DownloadFileFailedException("[downloadFile] failed", e);
        } finally {
            doClose(inputStream);
            doClose(outputStream);
        }
        return filename;
    }

    /**
     * 下载文件
     * @param url url
     * @param headers 自定义header
     * @return 文件地址
     */
    public static String getFile(String url, Map<String, String> headers) throws DownloadFileFailedException {

        String filename = UUID.randomUUID().toString() + ".jpg";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(10 * 1000)
                    .build();
            httpGet.setConfig(requestConfig);

            response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            outputStream = new FileOutputStream(filename);

            int inByte;
            while ((inByte = inputStream.read()) != -1) {
                outputStream.write(inByte);
            }
        } catch (IOException e) {
            throw new DownloadFileFailedException("[下载文件失败], url: " + url + " headers:" + headers, e);
        } finally {
            doClose(outputStream);
            doClose(inputStream);
            doClose(response);
            doClose(httpClient);
        }

        return filename;
    }


    /**
     * 发送post请求
     *
     * @param url    post url
     * @param params post参数
     * @return
     */
    public static String post(String url, Map<String, Object> params) {
        return post(url, params, null);
    }

    /**
     * 发送post请求
     * @param url post url
     * @param params post参数
     * @param headers post headers
     * @return
     */
    public static String post(String url, Map<String, Object> params, Map<String, String> headers){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {

            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CHARSET_UTF_8));
            } catch (UnsupportedEncodingException e) {
                logger.error("[HttpClientUtil.post] post failed, url:{} headers:{}", url, headers , e);
            }

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(10 * 1000)
                    .build();
            httpPost.setConfig(requestConfig);

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, CHARSET_UTF_8);

        }catch (IOException e) {
            logger.error("[HttpClientUtil.post] post failed, url:{} headers:{}", url, headers , e);
        } finally {
            doClose(response);
            doClose(httpClient);
        }
        return null;
    }


    /**
     * post json数据
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String postJson(String url, String jsonStr) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", CONTENT_TYPE_JSON);
            try {
                StringEntity stringEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
                httpPost.setEntity(stringEntity);
            } catch (Exception e) {
                logger.error("[HttpClientUtil.postJson] utf-8解码失败, jsonStr:{}", jsonStr, e);
                return null;
            }

            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, CHARSET_UTF_8);
        } catch (IOException e) {
            logger.error("[HttpClientUtil.postJson] post failed, url:{} jsonStr:{}", url, jsonStr , e);
        } finally {
            doClose(response);
            doClose(httpClient);
        }
        return null;
    }

    /**
     * put json数据
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String putJson(String url, String jsonStr) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        try {
            HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader("Content-Type", CONTENT_TYPE_JSON);
            if (jsonStr != null) {
                try {
                    StringEntity stringEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
                    httpPut.setEntity(stringEntity);
                } catch (Exception e) {
                    logger.error("[HttpClientUtil.postJson] utf-8解码失败, jsonStr:{}", jsonStr, e);
                    return null;
                }
            }

            response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, CHARSET_UTF_8);
        } catch (IOException e) {
            logger.error("[HttpClientUtil.postJson] post failed, url:{} jsonStr:{}", url, jsonStr , e);
        } finally {
            doClose(response);
            doClose(httpClient);
        }
        return null;
    }

    /**
     * 上传文件，携带特定的host下载文件
     * @param url
     * @param fileUrl
     * @return
     * @throws DownloadFileFailedException
     */
    public static String postFile(String url, String fileUrl, String fileHost) throws DownloadFileFailedException {
        Map<String ,String> headers = new HashMap<>();
        if (fileHost != null) {
            headers.put("Host", fileHost);
        }
        String filepath = getFile(fileUrl, headers);

        return postFile(url, filepath);
    }


    /**
     * 上传文件, 后删除
     *
     * @return
     */
    public static String postFile(String url, String filepath) {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        File file = new File(filepath);

        try{
            HttpPost httpPost = new HttpPost(url);

            FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // TODO: media字段是针对微信，此库需要改得更加通用
            builder.addPart("media", fileBody);
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);

            httpPost.setHeader("Connection", "Keep-Alive");
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(5000)
                    .setConnectTimeout(5000)
                    .setSocketTimeout(10 * 1000)
                    .build();
            httpPost.setConfig(requestConfig);

            response = httpClient.execute(httpPost);
            return EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);

        } catch (IOException e) {
            logger.error("[HttpClientUtil.postFile] post file failed, url:{}", url, e);
        } finally {
            doClose(response);
            doClose(httpClient);
            // 最后删除文件
            file.delete();
        }
        return null;
    }

    private static void doClose(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            } 
        } catch (IOException e) {
            logger.warn("[HttpClientUtil.close] close failed, closeable:{}", closeable);
        }
    }
}
