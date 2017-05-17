package com.kuaizhan.pojo.vo;

import com.kuaizhan.constant.ErrorCodes;
import lombok.Data;


/**
 * A wrapped Json Response
 * Created by jinyong on 2016/12/27.
 */
@Data
public class JsonResponse {

    private int code;
    private String msg;
    private Object data;

    public JsonResponse(Object data) {
        this.code = ErrorCodes.SUCCESS.getCode();
        this.msg = ErrorCodes.SUCCESS.getMessage();
        this.data=data;
    }


    public JsonResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
