package com.kuaizhan.exception.system;

import com.kuaizhan.config.ErrorCodeConfig;
import com.kuaizhan.exception.BaseException;

/**
 * Created by liangjiateng on 2017/3/17.
 */
public class ServerException extends SystemException {
    public ServerException() {
        // TODO: 这里的null留下了NPE祸根
        super(ErrorCodeConfig.SERVER_ERROR.getCode(), ErrorCodeConfig.SERVER_ERROR.getMsg(), null);
    }
    public ServerException(Exception e) {
        super(ErrorCodeConfig.SERVER_ERROR.getCode(), ErrorCodeConfig.SERVER_ERROR.getMsg(), e);
    }
}
