package com.kuaizhan.pojo.VO;

import lombok.Data;

/**
 * 消息展示对象
 * Created by liangjiateng on 2017/3/18.
 */
@Data
public class MsgVO {
    private Long id;
    private String name;
    private String headImgUrl;
    private String openId;
    private Integer isFocus;
    private String content;
    private Long time;
}
