package com.kuaizhan.utils;

import com.kuaizhan.pojo.DO.AccountDO;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.VO.AccountVO;
import com.kuaizhan.pojo.VO.PostVO;

/**
 * pojo的转换组件
 * Created by zixiong on 2017/3/30.
 */
public class PojoSwitcher {

    public static PostVO postDOToVO(PostDO postDO) {

        if (postDO == null) {
            return null;
        }
        PostVO postVO = new PostVO();

        postVO.setPageId(postDO.getPageId());
        postVO.setMediaId(postDO.getMediaId());
        postVO.setTitle(postDO.getTitle());
        postVO.setAuthor(postDO.getAuthor());
        postVO.setDigest(postDO.getDigest());
        postVO.setContent(postDO.getContent());
        postVO.setThumbUrl(postDO.getThumbUrl());
        postVO.setThumbMediaId(postDO.getThumbMediaId());
        postVO.setContentSourceUrl(postDO.getContentSourceUrl());
        postVO.setShowCoverPic(postDO.getShowCoverPic());
        postVO.setUpdateTime(postDO.getUpdateTime());
        return postVO;
    }

    public static AccountVO accountDOToVO(AccountDO accountDO) {
        if (accountDO == null) {
            return null;
        }
        AccountVO accountVO = new AccountVO();
        accountVO.setWeixinAppid(accountDO.getWeixinAppId());
        accountVO.setAppSecret(accountDO.getAppSecret());
        accountVO.setHeadImg(accountDO.getHeadImg());
        accountVO.setInterest(JsonUtil.string2List(accountDO.getInterestJson(), String.class));
        accountVO.setName(accountDO.getNickName());
        accountVO.setQrcode(accountDO.getQrcodeUrl());
        accountVO.setServiceType(accountDO.getServiceType());
        accountVO.setVerifyType(accountDO.getVerifyType());
        return accountVO;
    }
}
