package com.kuaizhan.config;

import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 项目初始化工作
 * Created by zixiong on 2017/6/30.
 */
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @PostConstruct
    public void init() {
        Unirest.setTimeouts(1000, 6 * 1000);
    }

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event){
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            logger.error("[Unirest] shutdown failed", e);
        }
    }
}
