package com.kuaizhan.pojo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝列表展示对象
 * Created by liangjiateng on 2017/3/16.
 */
public class FanListVO {

    private Long totalNum;
    private Integer currentPage;
    private Integer totalPage;
    private List<FanVO> users=new ArrayList<>();

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

    public List<FanVO> getFans() {
        return users;
    }

    public void setFans(List<FanVO> fans) {
        this.users = fans;
    }

    @Override
    public String toString() {
        return "FanListVO{" +
                "totalNum=" + totalNum +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", users=" + users +
                '}';
    }
}
