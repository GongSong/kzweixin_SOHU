package com.kuaizhan.common;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.DirectExchange;

/**
 * 绑定queue和exchange的类
 * 把rabbitMQ的queue, 以queue的name为routing key绑定到某个exchange
 * Created by zixiong on 2017/3/22.
 */
public class BindingProxy {

    private Queue queue;

    private DirectExchange exchange;

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void setExchange(DirectExchange exchange) {
        this.exchange = exchange;
    }

    public Binding getBinding(){
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }
}