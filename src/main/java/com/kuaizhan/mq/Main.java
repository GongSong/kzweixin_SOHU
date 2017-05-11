package com.kuaizhan.mq;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * MQ消费者的main函数入口
 * Created by zixiong on 2017/3/27.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/applicationContext-dao.xml",
                "classpath:spring/applicationContext-mq.xml",
                "classpath:spring/applicationContext-redis.xml",
                "classpath:spring/applicationContext-service.xml",
                "classpath:spring/applicationContext-util.xml",
                "classpath:spring/springmvc.xml",
                "classpath:spring-consumer/applicationContext-mq.xml");
    }
}
