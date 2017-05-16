package com.kuaizhan.pojo.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 图文消息，展开多图文
 * Created by zixiong on 2017/4/28.
 */
@Data
public class PostListFlatVO {
    private Long totalNum;
    private Integer currentPage;
    private Integer totalPage;
    private List<PostVO> posts = new ArrayList<>();
}
