package com.kuaizhan.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 在MenuDTO外面包了一层menu
 * Created by zixiong on 2017/6/5.
 */
@Data
public class MenuWrapper {
    private MenuDTO menu;
    @JsonProperty("publish")
    private Boolean isPublished;
}
