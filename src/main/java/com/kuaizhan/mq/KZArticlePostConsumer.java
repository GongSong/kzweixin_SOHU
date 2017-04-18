package com.kuaizhan.mq;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void onMessage(Map msgMap) throws Exception {

        //TODO:畅言
        long weixinAppid = (long) msgMap.get("weixinAppid");
        List<Long> pageIds = (List<Long>) msgMap.get("pageIds");
        int i = 0;
        for (Long pageId : pageIds) {
            List<PostDO> postDOList = new ArrayList<>();

            // 调用接口
            ArticleDTO articleDTO = postService.getKzArticle(pageId);
            if (articleDTO != null) {

                PostDO postDO = new PostDO();
                postDO.setWeixinAppid(weixinAppid);
                postDO.setTitle(articleDTO.getTitle());
                postDO.setDigest("");
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : articleDTO.getContent()) {
                    stringBuilder.append(str);
                }
                postDO.setContent(stringBuilder.toString());

                if (articleDTO.getCoverUrl() != null && !"".equals(articleDTO.getCoverUrl())) {
                    postDO.setThumbUrl(articleDTO.getCoverUrl());
                } else {
                    String picUrl = (i == 0) ? getFirstPostDefaultThumbUrl() : getCommonPostDefaultThumbUrl();
                    postDO.setThumbUrl(picUrl);
                }


                postDOList.add(postDO);
                i++;
            }
            // 新增文章
            postService.insertMultiPosts(weixinAppid, postDOList);
        }

    }

    /**
     * 多图文第一篇图文的默认封面图
     *
     * @return
     */
    private String getFirstPostDefaultThumbUrl() {
        return ApplicationConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
    }

    /**
     * 多图文非第一篇图文的默认封面图
     *
     * @return
     */
    private String getCommonPostDefaultThumbUrl() {
        return ApplicationConfig.getResUrl("/res/weixin/images/post-default-cover-200-200.png");
    }
}
