package com.kuaizhan.kzweixin.exception.common;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.exception.BusinessException;

/**
 * Created by zixiong on 2017/4/17.
 */
public class DownloadFileFailedException extends BusinessException {

    private static final ErrorCode errorCode = ErrorCode.DOWNLOAD_FILE_FAILED;

    public DownloadFileFailedException(String msg, Exception cause) {
        super(DownloadFileFailedException.errorCode,  msg, cause);
    }
}
