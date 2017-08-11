package com.kuaizhan.kzweixin.controller.vo;

import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.BizCode;
import com.kuaizhan.kzweixin.enums.WxResponseType;
import lombok.Data;

import java.util.Map;

/**
 * Created by zixiong on 2017/07/25.
 */
@Data
public class ActionVO {
    private Integer id;
    private BizCode bizCode;
    private String keyword;
    private ActionType actionType;
    private WxResponseType responseType;
    private Map responseJson;
    private Boolean status;
}
