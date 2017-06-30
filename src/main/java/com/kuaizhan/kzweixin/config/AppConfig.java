package com.kuaizhan.kzweixin.config;

import com.mashape.unirest.http.Unirest;

import javax.annotation.PostConstruct;

/**
 * 项目初始化工作
 * Created by zixiong on 2017/6/30.
 */
public class AppConfig {

    @PostConstruct
    public void init() {
        Unirest.setTimeouts(1000, 6 * 1000);
    }

}
