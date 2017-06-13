package com.kuaizhan.kzweixin.controller.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息列表展示对象
 * Created by zixiong on 2017/3/21.
 */
@Data
public class PostListVO {
    private Long totalNum;
    private Integer currentPage;
    private Integer totalPage;
    private List<List<PostVO>> posts = new ArrayList<>();
}
