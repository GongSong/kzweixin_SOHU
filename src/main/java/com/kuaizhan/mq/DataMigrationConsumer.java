package com.kuaizhan.mq;

import com.kuaizhan.controller.BaseController;
import com.kuaizhan.exception.system.MongoException;
import com.kuaizhan.service.PostService;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zixiong on 2017/4/24.
 */
public class DataMigrationConsumer extends BaseMqConsumer {

    @Resource
    PostService postService;

    private static final Logger logger = Logger.getLogger(DataMigrationConsumer.class);

    @Override
    public void onMessage(Map msgMap) {
        Long pageId = (Long) msgMap.get("pageId");
        try {
            String content = postService.getPostContent(pageId);
            if (content != null) {
                logger.info("[migration] migrate content succeed, pageId: " + pageId);
            } else {
                logger.warn("[migration] migrate content failed, pageId: " + pageId);
            }
        } catch (Exception e){
            logger.error("[migration] get content failed, pageId: " + pageId, e);
        }
    }
}
