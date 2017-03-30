package com.kuaizhan.mq;

import com.kuaizhan.exception.business.AccountNotExistException;
import com.kuaizhan.exception.business.AddMaterialException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.JsonParseException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 从快站文章导入，消费者
 * Created by liangjiateng on 2017/3/28.
 */

public class KZArticlePostConsumer extends BaseMqConsumer {

    @Resource
    PostService postService;
    @Resource
    WeixinPostService weixinPostService;
    @Resource
    AccountService accountService;

    @Override
    public void onMessage(HashMap msgMap) throws IOException, RedisException, JsonParseException, DaoException, AccountNotExistException, AddMaterialException {
        //TODO:畅言
        long weixinAppid = (long) msgMap.get("weixinAppid");
        List<Long> pageIds = (List<Long>) msgMap.get("pageIds");
        //调用接口
        for (Long pageId : pageIds) {
            ArticleDTO articleDTO = postService.getKzArticle(pageId);
            if (articleDTO != null) {
                //微信上传封面图
//                String[] ret = weixinPostService.uploadImage(accountService.getAccountByWeixinAppId(weixinAppid).getAccessToken(), articleDTO.getCoverUrl());
//                //封面图的media_id
//                String thumbMediaId = ret[0];
//                //封面图的微信url
//                String thumbUrl = ret[1];

            }
        }


        //上传内容里面的图片到微信

        //微信新增图文

        //数据库insert

        //content存mongo

    }
}
