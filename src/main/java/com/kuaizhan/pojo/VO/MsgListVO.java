package com.kuaizhan.pojo.VO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/18.
 */
public class MsgListVO {
    private Long totalNum;
    private Integer currentPage;
    private Integer totalPage;
    private List<MsgVO> msgs = new ArrayList<>();

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

    public List<MsgVO> getMsgs() {
        return msgs;
    }

    public void setMsgs(List<MsgVO> msgs) {
        this.msgs = msgs;
    }

    @Override
    public String toString() {
        return "MsgListVO{" +
                "totalNum=" + totalNum +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", msgs=" + msgs +
                '}';
    }
}
