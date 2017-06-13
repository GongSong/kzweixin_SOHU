package com.kuaizhan.kzweixin.cache;
import com.kuaizhan.kzweixin.pojo.po.AccountPO;
import com.kuaizhan.kzweixin.pojo.dto.AuthorizationInfoDTO;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface AccountCache {


    AccountPO getAccountInfoByWeixinAppId(long weixinAppId);

    void setAccountInfo(AccountPO account);

    void deleteAccountInfo(long weixinAppId);

    /**
     * 获取accessToken
     * @param weixinAppId
     * @return
     */
    String getAccessToken(long weixinAppId);

    void setAccessToken(long weixinAppId,AuthorizationInfoDTO authorizationInfoDTO);
}
