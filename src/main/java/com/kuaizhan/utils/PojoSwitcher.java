package com.kuaizhan.utils;

import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.PostPO;
import com.kuaizhan.pojo.vo.AccountVO;
import com.kuaizhan.pojo.vo.PostVO;

/**
 * pojo的转换组件
 * Created by zixiong on 2017/3/30.
 */
public class PojoSwitcher {

    public static PostVO postPOToVO(PostPO postPO) {

        if (postPO == null) {
            return null;
        }
        PostVO postVO = new PostVO();

        postVO.setPageId(postPO.getPageId());
        postVO.setMediaId(postPO.getMediaId());
        postVO.setTitle(postPO.getTitle());
        postVO.setAuthor(postPO.getAuthor());
        postVO.setDigest(postPO.getDigest());
        postVO.setContent(postPO.getContent());
        postVO.setThumbUrl(postPO.getThumbUrl());
        postVO.setThumbMediaId(postPO.getThumbMediaId());
        postVO.setContentSourceUrl(postPO.getContentSourceUrl());
        postVO.setShowCoverPic(postPO.getShowCoverPic());
        postVO.setUpdateTime(postPO.getUpdateTime());
        return postVO;
    }

    public static AccountVO accountPOToVO(AccountPO accountPO) {
        if (accountPO == null) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        accountVO.setWeixinAppid(accountPO.getWeixinAppId());
        // 对appSecret进行打码处理
        String appSecret = accountPO.getAppSecret();
        if (appSecret != null && !"".equals(appSecret)) {
            int length = appSecret.length();
            int firstIdx = length > 5? 5: length;
            int lastIdx = length > 5? length - 5: length;
            accountVO.setAppSecret(
                    appSecret.substring(0, firstIdx)
                    + "************************"
                    + appSecret.substring(lastIdx, length)
            );
        }
        accountVO.setHeadImg(accountPO.getHeadImg());
        accountVO.setInterest(JsonUtil.string2List(accountPO.getInterestJson(), String.class));
        accountVO.setName(accountPO.getNickName());
        accountVO.setQrcode(accountPO.getQrcodeUrl());
        accountVO.setServiceType(accountPO.getServiceType());
        accountVO.setVerifyType(accountPO.getVerifyType());
        return accountVO;
    }
}
