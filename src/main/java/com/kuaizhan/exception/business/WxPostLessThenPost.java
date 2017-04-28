package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/4/28.
 */
public class WxPostLessThenPost extends BusinessException {
    public WxPostLessThenPost() {
        super(ErrorCodeConfig.WX_POST_LESS_THEN_POST.getCode(), ErrorCodeConfig.WX_POST_LESS_THEN_POST.getMsg());
    }
}
