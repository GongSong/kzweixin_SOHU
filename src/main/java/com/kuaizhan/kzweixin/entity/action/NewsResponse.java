package com.kuaizhan.kzweixin.entity.action;

import lombok.Data;

import java.util.List;

/**
 * Created by zixiong on 2017/6/26.
 */
@Data
public class NewsResponse {

    private List<News> news;

    @Data
    public class News {
        private String title;
        private String description;
        private String picUrl;
        private String url;
    }
}
