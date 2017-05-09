package com.kuaizhan.exception.deprecated.system;

import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/27.
 */
public class SystemException extends BaseException {

    public SystemException(int code, String msg){
        super(code, msg);
    }
    public SystemException(int code, String msg, Exception e) {
        super(code, msg, e);
    }
}
