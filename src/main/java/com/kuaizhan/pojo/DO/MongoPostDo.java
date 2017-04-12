package com.kuaizhan.pojo.DO;

/**
 * Post存在mongo的数据
 * Created by zixiong on 2017/3/29.
 */
public class MongoPostDo {

    private Long id;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
