package com.kuaizhan.kzweixin.controller.param;

import com.kuaizhan.kzweixin.entity.autoreply.KeywordItem;
import com.kuaizhan.kzweixin.enums.ResponseType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Created by fangtianyu on 7/31/17.
 */
@Data
public class KeywordReplyRuleParam {
    @NotNull
    private Long weixinAppid;
    @NotNull
    @Size(max = 60, message = "规则名不能超过60个字符")
    private String ruleName;
    @NotNull
    private List<KeywordItem> keywords;
    @NotNull
    private ResponseType responseType;
    @NotNull
    @Size(max = 1000, message = "文字内容不能超过1000个字符")
    private Map<String, Object> responseJson;
}
