package com.kuaizhan.pojo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息列表展示对象
 * Created by zixiong on 2017/3/21.
 */
public class PostListVO {
    private Long totalNum;
    private Integer currentPage;
    private Integer totalPage;
    private List<List<PostVO>> posts = new ArrayList<>();

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<List<PostVO>> getPosts() {
        return posts;
    }

    public void setPosts(List<List<PostVO>> posts) {
        this.posts = posts;
    }


    @Override
    public String toString() {
        return "PostListVO{" +
                "totalNum=" + totalNum +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", posts=" + posts +
                '}';
    }
}
