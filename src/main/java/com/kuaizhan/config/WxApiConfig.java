package com.kuaizhan.config;


/**
 * 微信api
 * Created by liangjiateng on 2017/3/1.
 */
public class WxApiConfig {

    //主域名
    private static final String DOMAIN_WEIXIN_API = "https://api.weixin.qq.com";
    private static final String DOMAIN_WEIXIN_MP = "https://mp.weixin.qq.com";

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

    //消息

    //客服消息发送
    private static final String WEIXIN_SEND_CUSTOM_MSG = "/cgi-bin/message/custom/send?access_token=";
    // 发送模板消息
    private static final String WEIXIN_SEND_TPL_MSG = "/cgi-bin/message/template/send?access_token=";
    // 添加模板id
    private static final String WEIXIN_ADD_TEMPLATE = "/cgi-bin/template/api_add_template?access_token=";


    //素材

    //新增永久素材
    private static final String WEIXIN_ADD_MATERIAL = "/cgi-bin/material/add_material?access_token=";
    //删除永久素材
    private static final String WEIXIN_DELETE_MATERIAL = "/cgi-bin/material/del_material?access_token=";
    //获取素材总数
    private static final String WEIXIN_GET_MATERIAL_COUNT = "/cgi-bin/material/get_materialcount?access_token=";
    //获取素材列表
    private static final String WEIXIN_BATCH_GET_MATERIAL = "/cgi-bin/material/batchget_material?access_token=";
    // 获取素材
    private static final String WEIXIN_GET_MATERIAL = "/cgi-bin/material/get_material?access_token=";

    // 图文
    // 上传多图文
    private static final String WEIXIN_CREATE_POSTS = "/cgi-bin/material/add_news?access_token=";
    // 修改图文
    private static final String WEIXIN_UPDATE_POST = "/cgi-bin/material/update_news?access_token=";
    // 上传图文中的图片素材
    private static final String WEIXIN_ADD_POST_IMAGE = "/cgi-bin/media/uploadimg?access_token=";

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     */
    public static String getQueryAuthUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_AUTH + componentAccessToken;
    }

    /**
     * 获取（刷新）授权公众号的接口调用凭据（令牌）
     */
    public static String getRefreshAuthUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_REFRESH_AUTH + componentAccessToken;
    }

    /**
     * 获取授权方的公众号帐号基本信息
     */
    public static String getAuthorizerInfoUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_AUTHORIZER_INFO + componentAccessToken;
    }

    /**
     * 获取accessToken
     */
    public static String getAccessTokenUrl(String appId, String appSecrect) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_ACCESS_TOKEN + appId + "&secret=" + appSecrect;
    }

    /**
     * 获取第三方平台component_access_token
     */
    public static String getComponentAccessTokenUrl() {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_COMPONENT_ACCESS_TOKEN;
    }

    /**
     * 获取预授权码
     */
    public static String getPreAuthCodeUrl(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_PRE_AUTH_CODE + componentAccessToken;
    }

    /**
     * 获取授权页入口
     */
    public static String getAuthEntranceUrl(String preAuthCode, String redirectUrl) {
        return DOMAIN_WEIXIN_MP + WEIXIN_GET_AUTHORIZATION_ENTRANCE + ApplicationConfig.WEIXIN_APPID_THIRD + "&pre_auth_code=" + preAuthCode + "&redirect_uri=" + redirectUrl;
    }

    /**
     * 使用授权码换取公众号的接口调用凭据和授权信息
     */
    public static String getAuthorizationInfo(String componentAccessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_AUTHORIZATION_INFO + componentAccessToken;
    }

    /**
     * 获取公众号已创建的标签
     */
    public static String getTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_TAGS + accessToken;
    }

    /**
     * 创建新的标签
     */
    public static String getCreateTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_CREATE_TAGS + accessToken;
    }

    /**
     * 批量为用户打标签
     */
    public static String setTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_SET_TAGS + accessToken;
    }

    /**
     * 删除标签
     */
    public static String deleteTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_DELETE_TAGS + accessToken;
    }

    /**
     * 重命名标签
     */
    public static String updateTagsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_UPDATE_TAGS + accessToken;
    }

    /**
     * 拉黑用户
     */
    public static String insertBlackUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_BALCK + accessToken;
    }

    /**
     * 移除黑名单
     */
    public static String deleteBlackUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_REMOVE_BLACK + accessToken;
    }

    /**
     * 获取用户基本信息
     */
    public static String getFanInfoUrl(String accessToken, String openId) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_FAN_INFO + accessToken + "&openId=" + openId + "&lang=zh_CN";
    }

    /**
     * 根据openid发送客服消息
     */
    public static String sendCustomMsgUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_SEND_CUSTOM_MSG + accessToken;
    }

    /**
     * 发送模板消息
     */
    public static String getSendTplMsgUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_SEND_TPL_MSG + accessToken;
    }

    /**
     * 新增模板id
     */
    public static String getAddTemplateUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_TEMPLATE + accessToken;
    }

    /**
     * 新增永久素材
     */
    public static String addMaterialUrl(String accessToken, String type) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_MATERIAL + accessToken + "&type=" + type;
    }

    /**
     * 删除永久素材
     */
    public static String deleteMaterialUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_DELETE_MATERIAL + accessToken;
    }

    /**
     * 获取素材总数
     */
    public static String getMaterialCount(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_MATERIAL_COUNT + accessToken;
    }

    /**
     * 获取素材列表
     */
    public static String getBatchMaterialUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_BATCH_GET_MATERIAL + accessToken;
    }

    /**
     * 获取多图文
     */
    public static String getMaterialUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_GET_MATERIAL + accessToken;
    }

    /**
     * 新增多图文
     */
    public static String getCreatePostsUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_CREATE_POSTS + accessToken;
    }

    /**
     * 修改素材列表
     */
    public static String getUpdatePostUrl(String accessToken){
        return DOMAIN_WEIXIN_API + WEIXIN_UPDATE_POST + accessToken;
    }

    /**
     * 上传图文中的图片
     */
    public static String getAddPostImageUrl(String accessToken) {
        return DOMAIN_WEIXIN_API + WEIXIN_ADD_POST_IMAGE + accessToken;
    }
}
