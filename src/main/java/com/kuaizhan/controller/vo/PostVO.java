package com.kuaizhan.controller.vo;

import lombok.Data;

/**
 * Created by zixiong on 2017/3/21.
 */
@Data
public class PostVO {
    private Long pageId;
    private String mediaId;
    private String title ;
    private String author;
    private String digest;
    private String content;
    private String thumbUrl;
    private String thumbMediaId;
    private String contentSourceUrl;
    private Short showCoverPic;
    private Integer updateTime;
}
