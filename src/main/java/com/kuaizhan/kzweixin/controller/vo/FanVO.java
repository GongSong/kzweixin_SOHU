package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

import java.util.List;

/**
 * 粉丝展示对象
 * Created by fangtianyu on 2017/6/27.
 */
@Data
public class FanVO {
    private Integer id;
    private String name;
    private String headImgUrl;
    private Integer sex;
    private String openId;
    private String address;
    private Integer subscribeTime;

    private List<Integer> tagIds;
}

//默认这个地方：1.前端有tagId和tagName的对应关系；2.前端只需要address的国家和省，不需要市名；
//3.前端自己能完成时间的数字和真实显示值转换；4.id和focusTime可以用Integer类表示，不需要Long型