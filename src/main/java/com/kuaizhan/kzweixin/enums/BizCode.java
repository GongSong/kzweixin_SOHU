package com.kuaizhan.kzweixin.enums;


/**
 * Created by zixiong on 2017/6/26.
 */
public enum BizCode implements BaseEnum {
    VOTE(1);

    private int code;

    BizCode(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
