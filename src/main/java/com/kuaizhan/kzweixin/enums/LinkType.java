package com.kuaizhan.kzweixin.enums;


/**
 * 链接组类型
 * Created by zixiong on 2017/07/30.
 */
public enum LinkType implements BaseEnum {
    // 普通链接类型
    URL(1),
    // 页面
    PAGE(2),
    // 社区
    CLUB(3),
    // 电商
    SHOP(4),
    // 海报
    POSTER(5),
    // 快文
    ARTICLE(8);

    private int code;

    LinkType(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }
}
