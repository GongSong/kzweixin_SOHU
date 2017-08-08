package com.kuaizhan.kzweixin.controller.param;

import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

/**
 * Created by fangtianyu on 8/2/17.
 */
@Data
public class AutoReplySubscribeParam {
    @NotNull
    private Long weixinAppid;
    @NotNull
    private ComponentResponseType responseType;
    @NotNull
    private Map<String, Object> responseJson;
}
