package com.kuaizhan.pojo.DO;

/**
 * Created by czm on 2017/1/16.
 * 有1标注的表示可为空
 */

public class PostDO implements Comparable<PostDO>{

    private Long pageId;
    private Long weixinAppid;
    private String title ;
    private String  thumbMediaId;
    private String thumbUrl;
    private Short showCoverPic = 0;
    private String author; //1
    private String digest; //1
    private String postUrl;
    private String contentSourceUrl; //1
    private String mediaId;
    private Integer syncTime;
    private Short type;
    private Integer index = 0;
    private Long kuaizhanPostId ; //每次添加图文时都会先调用 新增快文的接口
    private Short status = 1; //1 为正常发布，2 为删除
    private Integer createTime;
    private Integer updateTime;

    private String content; //图文信息中的内容含有标签

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    public Long getWeixinAppid() {
        return weixinAppid;
    }

    public void setWeixinAppid(Long weixinAppid) {
        this.weixinAppid = weixinAppid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Short getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(Short showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getSyncTime() {
        return syncTime;
    }

    public void setSyncTime(Integer syncTime) {
        this.syncTime = syncTime;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getKuaizhanPostId() {
        return kuaizhanPostId;
    }

    public void setKuaizhanPostId(Long kuaizhanPostId) {
        this.kuaizhanPostId = kuaizhanPostId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(PostDO obj) {
        return this.index - obj.getIndex();
    }
    
    @Override
    public String toString() {
        return "PostDO{" +
                "pageId=" + pageId +
                ", weixinAppid=" + weixinAppid +
                ", title='" + title + '\'' +
                ", thumbMediaId='" + thumbMediaId + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", showCoverPic=" + showCoverPic +
                ", author='" + author + '\'' +
                ", digest='" + digest + '\'' +
                ", postUrl='" + postUrl + '\'' +
                ", contentSourceUrl='" + contentSourceUrl + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", syncTime=" + syncTime +
                ", type=" + type +
                ", index=" + index +
                ", kuaizhanPostId=" + kuaizhanPostId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", content='" + content + '\'' +
                '}';
    }

}
