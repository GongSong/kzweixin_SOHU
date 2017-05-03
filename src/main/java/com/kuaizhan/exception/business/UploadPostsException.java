package com.kuaizhan.exception.business;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.config.ErrorCodeConfig;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Created by zixiong on 2017/3/30.
 */
public class UploadPostsException extends BusinessException {

    public UploadPostsException() {
        super(ErrorCodeConfig.UPLOAD_POSTS_ERROR.getCode(), ErrorCodeConfig.UPLOAD_POSTS_ERROR.getMsg());
    }

    public UploadPostsException(String msg) {
        super(ErrorCodeConfig.UPLOAD_POSTS_ERROR.getCode(), msg);
    }

    public UploadPostsException(String msg, Exception e) {
        super(ErrorCodeConfig.UPLOAD_POSTS_ERROR.getCode(), msg, e);
    }
}
