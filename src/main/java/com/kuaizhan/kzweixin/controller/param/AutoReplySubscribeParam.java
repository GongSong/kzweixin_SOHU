package com.kuaizhan.kzweixin.controller.param;

import com.kuaizhan.kzweixin.enums.ResponseType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Created by fangtianyu on 8/2/17.
 */
@Data
public class AutoReplySubscribeParam {
    @NotNull
    private Long weixinAppid;
    @NotNull
    private ResponseType responseType;
    @NotNull
    private Map<String, Object> responseJson;
}
