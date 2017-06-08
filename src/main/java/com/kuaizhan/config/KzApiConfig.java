package com.kuaizhan.config;

import com.kuaizhan.constant.AppConstant;

/**
 * Created by zixiong on 2017/4/25.
 */
public class KzApiConfig {
    public static final String KZ_APPLY_PUSH_TOKEN = "http://push.kuaizhan.sohuno.com/api/v1/tokens/token";

    // 上传图片到快站用户空间
    public static final String KZ_UPLOAD_PIC_URL = "http://" + ApplicationConfig.KZ_SERVICE_IP + "/pic/service-upload-pic-by-url";
    // 新增快文
    public static final String KZ_POST_ARTICLE_URL = "http://"+ ApplicationConfig.KZ_SERVICE_HOST + "/post/service-sync-to-kz-post";


    // 获取快文详情
    public static String getKzArticleUrl(long pageId) {
        return "http://"+ ApplicationConfig.KZ_SERVICE_IP + "/post/service-get-post?page_id=" + pageId;
    }

    // 获取数据迁移前快文内容, 临时接口
    public static String getKzArticleContentUrl(long pageId) {
        return "http://" + ApplicationConfig.KZ_SERVICE_IP + "/weixin/service-get-post?page_id=" + pageId;
    }

    // 快站资源路径
    public static String getResUrl(String url) {
        return ApplicationConfig.KZ_DOMAIN_RES + url + "?v=" + AppConstant.PHP_APP_VERSION;
    }
}

