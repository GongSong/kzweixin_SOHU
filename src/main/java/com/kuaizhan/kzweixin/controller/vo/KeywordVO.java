package com.kuaizhan.kzweixin.controller.vo;

import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import lombok.Data;

import com.kuaizhan.kzweixin.controller.param.KeywordParamItem;
import java.util.List;
import java.util.Map;

/**
 * Created by fangtianyu on 7/31/17.
 */
@Data
public class KeywordVO {
    private Long ruleId;
    private Long weixinAppid;
    private String ruleName;
    private ComponentResponseType responseType;
    private List<KeywordParamItem> keywords;
    private ResponseJson responseJson;
}
