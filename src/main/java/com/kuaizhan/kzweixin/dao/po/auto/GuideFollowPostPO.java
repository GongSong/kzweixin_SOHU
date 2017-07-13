package com.kuaizhan.kzweixin.dao.po.auto;

public class GuideFollowPostPO {
    private Long weixinAppid;

    private String nickName;

    private String headImg;

    private Long pageId;

    private String postUrl;

    private Integer status;

    private Integer createTime;

    private Integer updateTime;

    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }
}