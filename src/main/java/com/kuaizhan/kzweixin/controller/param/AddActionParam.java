package com.kuaizhan.kzweixin.controller.param;

import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.BizCode;
import com.kuaizhan.kzweixin.enums.ResponseType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by zixiong on 2017/6/22.
 */
@Data
public class AddActionParam {
    @NotNull(message = "accountId can not be null")
    private String accountId;
    @NotNull(message = "bizCode can not be null")
    private BizCode bizCode;
    private String keyword;
    @NotNull(message = "actionType can not be null")
    private ActionType actionType;
    @NotNull(message = "responseType can not be null")
    private ResponseType responseType;
    @NotNull(message = "responseJson can not be null")
    private Map responseJson;

    private Boolean status;
}
