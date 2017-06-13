package com.kuaizhan.kzweixin.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 在MenuDTO外面包了一层menu
 * Created by zixiong on 2017/6/5.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuWrapper {
    private MenuDTO menu;
    private Boolean publish;
}
