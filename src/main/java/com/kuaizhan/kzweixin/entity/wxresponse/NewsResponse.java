package com.kuaizhan.kzweixin.entity.wxresponse;

import lombok.Data;

import java.util.List;

/**
 * news类型的返回
 * Created by zixiong on 2017/6/26.
 */
@Data
public class NewsResponse implements CallbackResponse {

    private List<News> news;

    @Data
    public static class News {
        private String title;
        private String description;
        private String picUrl;
        private String url;
    }
}
