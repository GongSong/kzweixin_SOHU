package com.kuaizhan.mq;

import java.util.HashMap;

/**
 * Mq消费者接口
 * Created by zixiong on 2017/3/22.
 */

public interface BaseMqConsumer {

    /**
     * Mq消费者实现此接口
     * @param msgMap MQ消息的HashMap参数
     */
    public void handleMessage(HashMap msgMap) ;
}
