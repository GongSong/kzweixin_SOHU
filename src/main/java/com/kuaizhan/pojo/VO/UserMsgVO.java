package com.kuaizhan.pojo.VO;

/**
 * 用户消息展示对象
 * Created by liangjiateng on 2017/3/18.
 */
public class UserMsgVO {
    private Long id;
    private Integer sendType;
    private String content;
    private Long time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
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
        return "UserMsgVO{" +
                "id=" + id +
                ", sendType=" + sendType +
                ", content='" + content + '\'' +
                ", time=" + time +
                '}';
    }
}
