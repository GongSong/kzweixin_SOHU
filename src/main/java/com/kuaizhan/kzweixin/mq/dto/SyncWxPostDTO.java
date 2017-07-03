package com.kuaizhan.kzweixin.mq.dto;

import com.kuaizhan.kzweixin.entity.post.WxPostDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by zixiong on 2017/6/7.
 */
@Data
public class SyncWxPostDTO {
    private Long userId;
    private Long weixinAppid;
    private Integer updateTime;
    private String mediaId;
    private Boolean isNew;
    private List<WxPostDTO> wxPostDTOS;
}
