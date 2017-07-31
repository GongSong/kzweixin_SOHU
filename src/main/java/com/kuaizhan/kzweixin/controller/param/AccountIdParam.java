package com.kuaizhan.kzweixin.controller.param;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/07/31.
 */
@Data
public class AccountIdParam {
    @NotNull(message = "accountId can not be null")
    @NotBlank(message = "accountId can not be blank")
    private String accountId;
}
