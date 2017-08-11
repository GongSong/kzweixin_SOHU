package com.kuaizhan.kzweixin.controller.vo;

import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ResponseType;
import lombok.Data;

import com.kuaizhan.kzweixin.entity.autoreply.KeywordItem;
import java.util.List;

/**
 * Created by fangtianyu on 7/31/17.
 */
@Data
public class KeywordVO {
    private Long ruleId;
    private Long weixinAppid;
    private String ruleName;
    private ResponseType responseType;
    private List<KeywordItem> keywords;
    private ResponseJson responseJson;
}
