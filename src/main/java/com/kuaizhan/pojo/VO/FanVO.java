package com.kuaizhan.pojo.VO;

import com.kuaizhan.pojo.DTO.TagDTO;

import java.util.List;

/**
 * 粉丝展示对象
 * Created by liangjiateng on 2017/3/16.
 */
public class FanVO {
    private Long id;
    private String name;
    private String avatar;
    private Integer sex;
    private String openId;
    private String address;
    private Long focusTime;

    private List<String> tags;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(Long focusTime) {
        this.focusTime = focusTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "FanVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", openId='" + openId + '\'' +
                ", address='" + address + '\'' +
                ", focusTime=" + focusTime +
                ", tags=" + tags +
                '}';
    }
}
