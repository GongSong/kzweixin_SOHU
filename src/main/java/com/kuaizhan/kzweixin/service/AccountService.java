package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.auto.AccountPO;
import com.kuaizhan.kzweixin.entity.common.PageV2;
import com.kuaizhan.kzweixin.exception.account.AccountNotExistException;

import java.util.List;

/**
 * 账号服务
 * Created by liangjiateng on 2017/3/15.
 */
public interface AccountService {

    /**
     * 获取绑定公众号的url
     * @param userId 绑定的userId
     * @param siteId 绑定的siteId
     * @param redirectUrl 授权成功后，跳转的url
     */
    String getBindUrl(Long userId, Long siteId, String redirectUrl);

    /**
     * 新增绑定公众号
     * @param userId 绑定的用户id
     * @param authCode 微信授权码
     * @param siteId 绑定的siteId, 允许为空
     */
    void bindAccount(Long userId, String authCode, Long siteId);

    /**
     * 获取公众号列表
     */
    // TODO: 需要加索引
    List<AccountPO> getAccounts(long userId);

    /**
     * 分页获取用户公众号列表
     */
    PageV2<AccountPO> listAccountByPage(long userId, int offset, int limit);

    /**
     * 获取accessToken
     */
    String getAccessToken(long weixinAppId);

    /**
     * 根据siteId获取账号信息
     */
    AccountPO getAccountBySiteId(long siteId);

    /**
     * 根据appId获取账号信息
     * @throws AccountNotExistException 找不到对应的账号
     */
    AccountPO getAccountByAppId(String appId) throws AccountNotExistException;

    /**
     * 根据long型Id获取账号信息
     */
    AccountPO getAccountByWeixinAppId(long weixinAppid);

    /**
     * 修改appSecret
     */
    void updateAppSecret(long weixinAppId, String appSecret);

    /**
     * 修改用户自定义分享开启／关闭状态
     * */
    void updateCustomizeShare(long weixinAppId, Integer openShare);

    /**
     * 修改服务号授权登录开启／关闭状态
     * */
    void updateAuthLogin(long weixinAppId, Integer openLogin);

    /**
     * 上传公众号QrcodeUrl为快站链接
     * (使用微信链接，页面上可能会出现无法显示)
     */
    void uploadQrcode2Kz(long weixinAppid);
}
