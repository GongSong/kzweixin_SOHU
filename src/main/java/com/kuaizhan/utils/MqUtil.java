package com.kuaizhan.utils;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbitMq操作工具类
 * Created by zixiong on 2017/3/23.
 */
@Component("mqUtil")
public class MqUtil {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布消息到rabbitMq
     * 默认的发布方法，使用配置的exchange
     * kzweixin使用一个exchange, 根据routing_key路由到不同key
     * @param routingKey 消息的routing_key, 与queue name一致
     * @param message  消息的数据，字符串数据
     */
    public void publish(String routingKey, String message) {
        rabbitTemplate.convertAndSend(routingKey, message);
    }

    /**
     * 发布消息到rabbitMq
     * 此方法可以指定exchange和routingKey
     */
    public void publish(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}

