package com.kuaizhan.kzweixin.exception.kuaizhan;

/**
 * Created by lorin on 17-3-30.
 */
public class KZPicUploadException extends Exception {
    public KZPicUploadException(String msg) {
        super(msg);
    }
    public KZPicUploadException(String msg, Exception cause) {
        super(msg, cause);
    }
}
