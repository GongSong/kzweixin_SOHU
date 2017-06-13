package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 粉丝列表展示对象
 * Created by liangjiateng on 2017/3/16.
 */
@Data
public class FanListVO {

    private Long totalNum;
    private Integer currentPage;
    private Integer totalPage;
    private List<FanVO> fans = new ArrayList<>();
}
