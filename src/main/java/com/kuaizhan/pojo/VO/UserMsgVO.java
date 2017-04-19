package com.kuaizhan.pojo.VO;

import lombok.Data;

/**
 * 用户消息展示对象
 * Created by liangjiateng on 2017/3/18.
 */
@Data
public class UserMsgVO {
    private Long id;
    private Integer sendType;
    private String content;
    private Long time;
}
