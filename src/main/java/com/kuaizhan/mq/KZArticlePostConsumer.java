package com.kuaizhan.mq;

import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public void onMessage(HashMap msgMap) throws Exception {

        //TODO:畅言
        long weixinAppid = (long) msgMap.get("weixinAppid");
        List<Long> pageIds = (List<Long>) msgMap.get("pageIds");
        List<PostDO> postDOList = new ArrayList<>();
        //调用接口
        for (Long pageId : pageIds) {
            ArticleDTO articleDTO = postService.getKzArticle(pageId);
            if (articleDTO != null) {
                //微信上传封面图
                PostDO postDO = new PostDO();
                postDO.setKuaizhanPostId(pageId);
                postDO.setWeixinAppid(weixinAppid);
                postDO.setTitle(articleDTO.getTitle());
                postDO.setDigest("");
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : articleDTO.getContent()) {
                    stringBuilder.append(str);
                }
                postDO.setContent(stringBuilder.toString());
                postDO.setThumbUrl(articleDTO.getCoverUrl());
                postDOList.add(postDO);

            }
            postService.insertMultiPosts(weixinAppid, postDOList);
        }

    }
}
