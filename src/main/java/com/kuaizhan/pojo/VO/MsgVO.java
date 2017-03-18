package com.kuaizhan.pojo.VO;

/**
 * 消息展示对象
 * Created by liangjiateng on 2017/3/18.
 */
public class MsgVO {
    private Long id;
    private String name;
    private String headImgUrl;
    private String openId;
    private Integer isFocus;
    private String content;
    private Long time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(Integer isFocus) {
        this.isFocus = isFocus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MsgVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", openId='" + openId + '\'' +
                ", isFocus=" + isFocus +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
