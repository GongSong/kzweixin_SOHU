package com.kuaizhan.kzweixin.entity.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by zixiong on 2017/6/15.
 */
@Data
public class FuncInfo {

    @JsonProperty("funcscope_category")
    private Id category;

    @Data
    public class Id {
        private String id;
    }
}
