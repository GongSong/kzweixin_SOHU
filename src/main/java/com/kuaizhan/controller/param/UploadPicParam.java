package com.kuaizhan.controller.param;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/4/28.
 */
@Data
public class UploadPicParam {
    @NotNull
    private Long weixinAppid;

    @NotNull
    @NotBlank
    private String imgUrl;
}
