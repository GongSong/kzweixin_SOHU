package com.kuaizhan.mq;

import com.kuaizhan.config.KzApiConfig;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.ArticleDTO;
import com.kuaizhan.service.PostService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 从快站文章导入，消费者
 * Created by liangjiateng on 2017/3/28.
 */

public class KZArticleImportConsumer extends BaseMqConsumer {

    private static final Logger logger = Logger.getLogger(KZArticleImportConsumer.class);

    @Resource
    PostService postService;

    @Override
    public void onMessage(Map msgMap) throws Exception {

        //TODO:畅言
        long weixinAppid = (long) msgMap.get("weixinAppid");
        List<Long> pageIds = (List<Long>) msgMap.get("pageIds");
        for (Long pageId : pageIds) {

            // 调用接口
            ArticleDTO articleDTO;
            try {
                articleDTO = postService.getKzArticle(pageId);
            } catch (Exception e) {
                logger.error("[KzArticleImport] get article failed", e);
                continue;
            }
            if (articleDTO != null) {

                PostDO postDO = new PostDO();
                postDO.setWeixinAppid(weixinAppid);
                postDO.setTitle(articleDTO.getTitle());
                postDO.setDigest("");
                // 这些字段都不应该为空
                postDO.setAuthor("");
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : articleDTO.getContent()) {
                    stringBuilder.append(str);
                }
                String content = stringBuilder.toString();
                if (Objects.equals(content, "")){
                    logger.info("快站文章内容为空, 忽略新增, pageId:" + pageId);
                    return ;
                }
                postDO.setContent(content);

                if (articleDTO.getCoverUrl() != null && ! "".equals(articleDTO.getCoverUrl())) {
                    postDO.setThumbUrl(articleDTO.getCoverUrl());
                } else {
                    String picUrl = getFirstPostDefaultThumbUrl();
                    postDO.setThumbUrl(picUrl);
                }


                List<PostDO> postDOList = new ArrayList<>();
                postDOList.add(postDO);
                postService.insertMultiPosts(weixinAppid, postDOList);
            }
            // 新增文章
        }

    }

    /**
     * 多图文第一篇图文的默认封面图
     *
     * @return
     */
    private String getFirstPostDefaultThumbUrl() {
        return KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
    }
}

