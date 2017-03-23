package com.kuaizhan.pojo.DO;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 文章（测试）
 * Created by liangjiateng on 2017/3/23.
 */
@Document
public class ArticleDO {

    //TODO:Mongo使用样例 随后删除

    private Long id;
    private String title;
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ArticleDO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
