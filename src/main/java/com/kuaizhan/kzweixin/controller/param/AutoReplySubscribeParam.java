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
    @Size(max = 1000, message = "文字内容不能超过1000个字符")
    private Map<String, Object> responseJson;
}
