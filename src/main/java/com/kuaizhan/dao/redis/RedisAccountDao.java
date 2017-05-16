package com.kuaizhan.dao.redis;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.dto.AuthorizationInfoDTO;

/**
 * 账号缓存
 * Created by Mr.Jadyn on 2017/2/9.
 */
public interface RedisAccountDao {


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
