package com.kuaizhan.kzweixin.mq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * mq消费者基类
 * Created by zixiong on 2017/6/7.
 */
public abstract class BaseConsumer implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(BaseConsumer.class);

    /**
     * 子类实现此接口处理逻辑
     */
    abstract void onMessage(String message);

    /**
     * 实现MessageListener，被MessageContainer调用
     */
    @Override
    public void onMessage(Message message) {
        if (message == null) {
            logger.error("[mq] message is null");
            return;
        }

        try {
            String msgStr = new String(message.getBody(), StandardCharsets.UTF_8);
            onMessage(msgStr);
        } catch (Exception e) {
            // 捕获所有未处理的异常，打印日志, 不继续外抛，mq消息将自动ack
            logger.error("[mq] unknown exception", e);
        }
    }
}
