package com.kuaizhan.pojo.DO;

/**
 * Post存在mongo的数据
 * Created by zixiong on 2017/3/29.
 */
public class MongoPostDo {
    Long pageId;
    String content;

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
