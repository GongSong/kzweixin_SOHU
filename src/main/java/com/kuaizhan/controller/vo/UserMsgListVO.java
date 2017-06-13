package com.kuaizhan.controller.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/18.
 */
@Data
public class UserMsgListVO {
    private Integer isExpire;
    private List<UserMsgVO> msgs = new ArrayList<>();
}
