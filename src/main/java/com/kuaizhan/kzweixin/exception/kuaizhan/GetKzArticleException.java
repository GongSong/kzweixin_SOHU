package com.kuaizhan.kzweixin.exception.kuaizhan;

/**
 * Created by zixiong on 2017/5/10.
 */
public class GetKzArticleException extends Exception {
    public GetKzArticleException(String msg) {
        super(msg);
    }
    public GetKzArticleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
