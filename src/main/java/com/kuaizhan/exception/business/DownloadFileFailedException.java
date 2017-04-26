package com.kuaizhan.exception.business;

import com.kuaizhan.config.ErrorCodeConfig;

/**
 * Created by zixiong on 2017/4/17.
 */
public class DownloadFileFailedException extends BusinessException {
    public DownloadFileFailedException() {
        super(ErrorCodeConfig.DOWNLOAD_FILE_ERROR.getCode(), ErrorCodeConfig.DOWNLOAD_FILE_ERROR.getMsg());
    }

    public DownloadFileFailedException(String msg, Exception e) {
        super(ErrorCodeConfig.DOWNLOAD_FILE_ERROR.getCode(), msg, e);
    }
}
