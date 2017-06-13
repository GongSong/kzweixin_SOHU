package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.config.KzApiConfig;
import com.kuaizhan.kzweixin.mq.dto.ArticleImportDTO;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.pojo.dto.ArticleDTO;
import com.kuaizhan.kzweixin.service.PostService;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 从快站文章导入，消费者
 * Created by liangjiateng on 2017/3/28.
 */

public class ImportKzArticleConsumer extends BaseConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ImportKzArticleConsumer.class);

    @Resource
    private PostService postService;

    @Override
    public void onMessage(String message) {

        ArticleImportDTO dto = JsonUtil.string2Bean(message, ArticleImportDTO.class);
        long weixinAppid = dto.getWeixinAppid();
        List<Long> pageIds = dto.getPageIds();

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

                PostPO postPO = new PostPO();
                postPO.setWeixinAppid(weixinAppid);
                postPO.setTitle(articleDTO.getTitle());
                postPO.setDigest("");
                // 这些字段都不应该为空
                postPO.setAuthor("");
                StringBuilder stringBuilder = new StringBuilder();
                for (String str : articleDTO.getContent()) {
                    stringBuilder.append(str);
                }
                String content = stringBuilder.toString();
                if (Objects.equals(content, "")){
                    logger.info("[KZArticleImport] 快站文章内容为空, 忽略新增, pageId:{}", pageId);
                    return ;
                }
                postPO.setContent(content);

                if (articleDTO.getCoverUrl() != null && ! "".equals(articleDTO.getCoverUrl())) {
                    postPO.setThumbUrl(articleDTO.getCoverUrl());
                } else {
                    String picUrl = KzApiConfig.getResUrl("/res/weixin/images/post-default-cover-900-500.png");
                    postPO.setThumbUrl(picUrl);
                }


                List<PostPO> postPOList = new ArrayList<>();
                postPOList.add(postPO);
                // 新增文章
                try {
                    postService.insertMultiPosts(weixinAppid, postPOList);
                } catch (Exception e) {
                    throw new RuntimeException("[mq] insertMultiPosts error", e);
                }
            }
        }
    }
}

