package com.kuaizhan.kzweixin.entity.responsejson;

/**
 * Created by zixiong on 2017/08/01.
 */
public interface ResponseJson {
    /**
     * 入库前清理数据
     */
    void cleanBeforeInsert();

    /**
     * 查询后清理数据
     */
    void cleanAfterSelect();
}
