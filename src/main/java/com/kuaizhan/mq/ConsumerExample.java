package com.kuaizhan.mq;

import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.PostService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;


/**
 * MQ消费者示例，之后删掉此类
 * Created by zixiong on 2017/3/22.
 */
public class ConsumerExample extends BaseMqConsumer {

    private static Logger logger = Logger.getLogger(ConsumerExample.class);

    @Resource
    PostService postService;

    @Override
    public void onMessage(HashMap map) {
        // 从HashMap中取数据
        long weixinAppid = (long) map.get("weixinAppid");
        try {
            // 调用service
            Page page = postService.listPostsByPagination(weixinAppid, 1);
            logger.info("######################################################################");
            logger.info("########################### Page： " + page);
            logger.info("######################################################################");
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

}
