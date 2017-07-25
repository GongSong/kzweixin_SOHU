package com.kuaizhan.kzweixin.controller.param;

import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.BizCode;
import com.kuaizhan.kzweixin.enums.ResponseType;
import lombok.Data;

import java.util.Map;

/**
 * Created by zixiong on 2017/07/25.
 */
@Data
public class UpdateActionParam {
    private BizCode bizCode;
    private String keyword;
    private ActionType actionType;
    private ResponseType responseType;
    private Map responseJson;
    private Boolean status;
}
