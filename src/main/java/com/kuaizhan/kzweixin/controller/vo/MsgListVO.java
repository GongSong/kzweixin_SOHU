package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjiateng on 2017/3/18.
 */
@Data
public class MsgListVO {
    private Long total;
    private List<MsgVO> msgs = new ArrayList<>();
    private Integer lastInteractTime;
}
