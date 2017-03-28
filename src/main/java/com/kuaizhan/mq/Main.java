package com.kuaizhan.mq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * MQ消费者的main函数入口
 * Created by zixiong on 2017/3/27.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/*.xml",
                "classpath:mq-consumer/applicationContext-mq.xml");
    }
}
