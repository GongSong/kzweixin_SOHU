package com.kuaizhan.kzweixin.enums;

/**
 * 公众号授权给开发者的权限集列表，ID为1到15时分别代表：
 * 消息管理权限
 * 用户管理权限
 * 帐号服务权限
 * 网页服务权限
 * 微信小店权限
 * 微信多客服权限
 * 群发与通知权限
 * 微信卡券权限
 * 微信扫一扫权限
 * 微信连WIFI权限
 * 素材管理权限
 * 微信摇周边权限
 * 微信门店权限
 * 微信支付权限
 * 自定义菜单权限
 * 请注意：该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。
 * 文档：https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1453779503&token=&lang=zh_CN
 * Created by zixiong on 2017/07/24.
 */
public enum WxAuthority {
    MSG_MANAGEMENT(1), // 消息管理
    USER_MANAGEMENT(2), // 用户管理
    ACCOUNT_SERVICE(3), // 账号服务
    PAGE_SERVICE(4), // 网页服务
    WX_SHOP(5), // 微信小店
    MULTI_CUSTOMER(6), // 多客服
    MASS_NOTIFY(7), // 群发与通知权限
    MATERIAL_MANAGEMENT(11), // 素材管理
    MENU_MANAGEMENT(15);  // 自定义菜单权限

    private int value;

    WxAuthority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
