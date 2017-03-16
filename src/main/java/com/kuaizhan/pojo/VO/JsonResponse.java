package com.kuaizhan.pojo.VO;

import com.kuaizhan.config.ErrorCodeConfig;


/**
 * A wrapped Json Response
 * Created by jinyong on 2016/12/27.
 */
public class JsonResponse {

    private int code;
    private String msg;
    private Object data;

    public JsonResponse(Object data) {
        this.code = ErrorCodeConfig.SUCCESS.getCode();
        this.msg = ErrorCodeConfig.SUCCESS.getMsg();
        this.data=data;
    }


    public JsonResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
