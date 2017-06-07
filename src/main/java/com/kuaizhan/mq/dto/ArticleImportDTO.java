package com.kuaizhan.mq.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by zixiong on 2017/6/7.
 */
@Data
public class ArticleImportDTO {
    private Long weixinAppid;
    private List<Long> pageIds;
}
