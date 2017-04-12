package com.kuaizhan.config;

/**
 * 第三方api
 * Created by liangjiateng on 2017/3/1.
 */
public class ApiConfig {

    /**
     * 智能回复接口
     */

    //新闻接口
    public static final String NEWS = "http://route.showapi.com/109-35";
    //笑话接口
    public static final String JOKE = "http://route.showapi.com/341-1";
    //天气接口
    public static final String WEATHER = "http://route.showapi.com/9-2";
    //音乐接口(qq音乐)
    public static final String MUSIC = "http://route.showapi.com/213-1";
    //股票接口(名称)
    public static final String STOCK_NAME = "http://route.showapi.com/131-43";
    //股票接口(代码)
    public static final String STOCK_CODE = "http://route.showapi.com/131-44";
    //百度翻译
    public static final String TRANSLATE = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    /**
     * 快站接口
     */
    //获取单个文章的信息
    public static final String KZ_ARTICLE = "/post/service-get-post?page_id=";
    public static final String KZ_POST_ARTICLE = "/post/service-sync-to-kz-post";

    /**
     * 获取单个文章的信息
     *
     * @param pageId
     * @return
     */
    public static String kzArticleUrl(long pageId) {
        return DOMAIN_KZ_API + KZ_ARTICLE + pageId;
    }

    /**
     * 新增快站文章
     *
     * @return
     */
    public static String kzPostArticleUrl() {
        return DOMAIN_KZ_API + KZ_POST_ARTICLE;
    }


    /**
     * 微信接口
     */

    //主域名
    private static final String DOMAIN_WEIXIN_API = "https://api.weixin.qq.com";
    private static final String DOMAIN_WEIXIN_MP = "https://mp.weixin.qq.com";
    private static final String DOMAIN_KZ_API = "http://service.kuaizhan.sohuno.com";
    //账户

    //获取access_token
    private static final String WEIXIN_GET_ACCESS_TOKEN = "/cgi-bin/token?grant_type=client_credential&appid=";
    //获取第三方平台component_access_token
    private static final String WEIXIN_GET_COMPONENT_ACCESS_TOKEN = "/cgi-bin/component/api_component_token";
    //获取预授权码
    private static final String WEIXIN_GET_PRE_AUTH_CODE = "/cgi-bin/component/api_create_preauthcode?component_access_token=";
    //使用授权码换取公众号的接口调用凭据和授权信息
    private static final String WEIXIN_GET_AUTHORIZATION_INFO = "/cgi-bin/component/api_query_auth?component_access_token=";
    //授权页入口
    private static final String WEIXIN_GET_AUTHORIZATION_ENTRANCE = "/cgi-bin/componentloginpage?component_appid=";
    //使用授权码换取公众号的接口调用凭据和授权信息
    private static final String WEIXIN_GET_AUTH = "/cgi-bin/component/api_query_auth?component_access_token=";
    //获取（刷新）授权公众号的接口调用凭据（令牌）
    private static final String WEIXIN_REFRESH_AUTH = "/cgi-bin/component/api_authorizer_token?component_access_token=";
    //获取授权方的公众号帐号基本信息
    private static final String WEIXIN_GET_AUTHORIZER_INFO = "/cgi-bin/component/api_get_authorizer_info?component_access_token=";

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     *
     * @param componentAccessToken
     * @return
     */
    public static String getQueryAuthUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_AUTH + componentAccessToken;
    }

    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）
     *
     * @param componentAccessToken
     * @return
     */
    public static String getRefreshAuthUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_REFRESH_AUTH + componentAccessToken;
    }

    /**
     * 获取授权方的公众号帐号基本信息
     *
     * @param componentAccessToken
     * @return
     */
    public static String getAuthorizerInfoUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_AUTHORIZER_INFO + componentAccessToken;
    }


    /**
     * 获取accessToken
     *
     * @param appId
     * @param appSecrect
     * @return
     */
    public static String getAccessTokenUrl(String appId, String appSecrect) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_ACCESS_TOKEN + appId + "&secret=" + appSecrect;
    }

    /**
     * 获取第三方平台component_access_token
     *
     * @return
     */
    public static String getComponentAccessTokenUrl() {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_COMPONENT_ACCESS_TOKEN;
    }

    /**
     * 获取预授权码
     *
     * @param componentAccessToken
     * @return
     */
    public static String getPreAuthCodeUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_PRE_AUTH_CODE + componentAccessToken;
    }

    /**
     * 获取授权页入口
     *
     * @param preAuthCode
     * @param redirectUrl
     * @return
     */
    public static String getAuthEntranceUrl(String preAuthCode, String redirectUrl) {
        return DOMAIN_WEIXIN_MP + WEIXIN_GET_AUTHORIZATION_ENTRANCE + ApplicationConfig.WEIXIN_APPID_THIRD + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + redirectUrl;
    }

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     *
     * @param componentAccessToken
     * @return
     */
    public static String getAuthorizationInfo(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_AUTHORIZATION_INFO + componentAccessToken;
    }

    //粉丝

    //获取公众号已创建的标签
    private static final String WEIXIN_GET_TAGS = "/cgi-bin/tags/get?access_token=";
    //创建新的标签
    private static final String WEIXIN_CREATE_TAGS = "/cgi-bin/tags/create?access_token=";
    //批量为用户打标签
    private static final String WEIXIN_SET_TAGS = "/cgi-bin/tags/members/batchtagging?access_token=";
    //删除标签
    private static final String WEIXIN_DELETE_TAGS = "/cgi-bin/tags/delete?access_token=";
    //重命名标签
    private static final String WEIXIN_UPDATE_TAGS = "/cgi-bin/tags/update?access_token=";
    //拉黑用户
    private static final String WEIXIN_ADD_BALCK = "/cgi-bin/tags/members/batchblacklist?access_token=";
    //移除黑名单
    private static final String WEIXIN_REMOVE_BLACK = "/cgi-bin/tags/members/batchunblacklist?access_token=";
    //获取用户基本信息
    private static final String WEIXIN_GET_FAN_INFO = "/cgi-bin/user/info?access_token=";


    /**
     * 获取公众号已创建的标签
     *
     * @param accessToken
     * @return
     */
    public static String getTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_TAGS + accessToken;
    }

    /**
     * 创建新的标签
     *
     * @param accessToken
     * @return
     */
    public static String getCreateTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_CREATE_TAGS + accessToken;
    }

    /**
     * 批量为用户打标签
     *
     * @param accessToken
     * @return
     */
    public static String setTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_SET_TAGS + accessToken;
    }

    /**
     * 删除标签
     *
     * @param accessToken
     * @return
     */
    public static String deleteTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_DELETE_TAGS + accessToken;
    }

    /**
     * 重命名标签
     *
     * @param accessToken
     * @return
     */
    public static String updateTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_UPDATE_TAGS + accessToken;
    }

    /**
     * 拉黑用户
     *
     * @param accessToken
     * @return
     */
    public static String insertBlackUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_BALCK + accessToken;
    }

    /**
     * 移除黑名单
     *
     * @param accessToken
     * @return
     */
    public static String deleteBlackUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_REMOVE_BLACK + accessToken;
    }

    /**
     * 获取用户基本信息
     *
     * @param accessToken
     * @param openId      openId
     * @return
     */
    public static String getFanInfoUrl(String accessToken, String openId) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_FAN_INFO + accessToken + "&openId=" + openId + "&lang=zh_CN";
    }

    //消息

    //客服消息发送
    private static final String WEIXIN_SEND_BY_OPENID = "/cgi-bin/message/custom/send?access_token=";

    /**
     * 根据openid发送客服消息
     *
     * @param accessToken
     * @return
     */
    public static String sendByOpenIdUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_SEND_BY_OPENID + accessToken;
    }

    //素材

    //新增永久素材
    private static final String WEIXIN_ADD_MATERIAL = "/cgi-bin/material/add_material?access_token=";
    //删除永久素材
    private static final String WEIXIN_DELETE_MATERIAL = "/cgi-bin/material/del_material?access_token=";
    //获取素材总数
    private static final String WEIXIN_GET_MATERIAL_COUNT = "/cgi-bin/material/get_materialcount?access_token=";
    //获取素材列表
    private static final String WEIXIN_GET_MATERIAL = "/cgi-bin/material/batchget_material?access_token=";

    // 图文
    // 上传多图文
    private static final String WEIXIN_CREATE_POSTS = "/cgi-bin/material/add_news?access_token=";

    // 修改图文
    public static final String WEIXIN_UPDATE_POST = "/cgi-bin/material/update_news?access_token=";

    // 上传图文中的图片素材
    public static final String WEIXIN_ADD_POST_IMAGE = "/cgi-bin/media/uploadimg?access_token=";


    /**
     * 新增永久素材
     * <<<<<<< HEAD
     *
     * @param accessToken
     * @param type
     * @return =======
     * >>>>>>> d77f9d46f42d48eb6e914065c18879bfafb75f85
     */
    public static String addMaterialUrl(String accessToken, String type) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_MATERIAL + accessToken + "&type=" + type;
    }

    /**
     * 删除永久素材
     *
     * @param accessToken
     * @return
     */
    public static String deleteMaterialUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_DELETE_MATERIAL + accessToken;
    }

    /**
     * 获取素材总数
     *
     * @param accessToken
     * @return
     */
    public static String getMaterialCount(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_MATERIAL_COUNT + accessToken;
    }

    /**
     * 获取素材列表
     *
     * @param accessToken
     * @return
     */
    public static String getMaterial(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_MATERIAL + accessToken;
    }

    public static String getCreatePostsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_CREATE_POSTS + accessToken;
    }

    public static String getUpdatePostUrl(String accessToken){
        return DOMAIN_WEIXIN_API + WEIXIN_UPDATE_POST + accessToken;
    }

    public static String getAddPostImageUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_POST_IMAGE + accessToken;
    }
}
