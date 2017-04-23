package com.kuaizhan.utils;

import com.kuaizhan.exception.business.DownloadFileFailedException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 * Created by Mr.Jadyn on 2016/12/28.
 */
public final class HttpClientUtil {


    private static final Logger logger = Logger.getLogger(HttpClientUtil.class);
    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_FILE = "multipart/form-data";
    private static final String BOUNDARY = "---------------------------123821742118716";

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
        String res;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            if (headers != null){
                for (Map.Entry<String, String> entry: headers.entrySet()){
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            res = execute(httpClient, httpGet);
        } finally {
            doHttpClientClose(httpClient);
        }
        return res;
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
        String res;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = httpPostHandler(url, params);
            if (headers != null){
                for (Map.Entry<String, String> entry: headers.entrySet()){
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            res = execute(httpClient, httpPost);
        } finally {
            doHttpClientClose(httpClient);
        }
        return res;
    }


    /**
     * post json数据
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static String postJson(String url, String jsonStr) {
        String res;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity stringEntity;
            try {
                stringEntity = new StringEntity(jsonStr, "UTF-8");
            } catch (Exception e) {
                logger.error("[HttpClientUtil.postJson] utf-8解码失败", e);
                return null;
            }
            httpPost.setHeader("Content-Type", CONTENT_TYPE_JSON);
            httpPost.setEntity(stringEntity);
            res = execute(httpClient, httpPost);
        } finally {
            doHttpClientClose(httpClient);
        }
        return res;
    }

    /**
     * 上传文件
     *
     * @param url
     * @param fileUrl
     * @return
     */
    public static String postMedia(String url, String fileUrl) throws DownloadFileFailedException {
        File file = FileUtil.download(fileUrl, null);
        return postMedia(url, file);
    }

    /**
     * 上传文件，携带特定的host下载文件
     * @param url
     * @param fileUrl
     * @return
     * @throws DownloadFileFailedException
     */
    public static String postMedia(String url, String fileUrl, String fileHost) throws DownloadFileFailedException {
        File file = FileUtil.download(fileUrl, fileHost);
        return postMedia(url, file);
    }


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String postMedia(String postUrl, File file) {
        String res = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(postUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());

            String str = "\r\n" + "--" + BOUNDARY + "\r\n" +
                    "Content-Disposition: form-data;name=\"media\";filelength=\"" + file.length() + "\";filename=\""
                    + file.getName() + "\"\r\n" +
                    "Content-Type:application/octet-stream\r\n\r\n";

            out.write(str.getBytes());

            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            file.delete();

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            res = stringBuilder.toString();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return res;
    }

    private static HttpPost httpPostHandler(String url, Map<String, Object> params) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, CHARSET_UTF_8));
        } catch (UnsupportedEncodingException e) {

        }
        return httpPost;
    }

    private static String execute(CloseableHttpClient httpClient, HttpUriRequest httpGetOrPost) {
        String res = null;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGetOrPost);
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity, CHARSET_UTF_8);
        } catch (IOException e) {

        } finally {
            doResponseClose(response);
        }
        return res;
    }


    private static void doHttpClientClose(CloseableHttpClient httpClient) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {

            }
        }
    }

    private static void doResponseClose(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
