package com.kuaizhan.kzweixin.entity.wxresponse;

import lombok.Data;

/**
 * Created by zixiong on 2017/6/26.
 */
@Data
public class TextResponse implements CallbackResponse {
    private String content;
}
