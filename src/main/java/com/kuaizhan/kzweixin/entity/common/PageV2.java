package com.kuaizhan.kzweixin.entity.common;

import lombok.Data;

import java.util.List;

/**
 * 新版的分页返回对象
 * Created by zixiong on 2017/7/19.
 */
@Data
public class PageV2<T> {
    /**
     * 总数
     */
    private long total;
    /**
     * 结果集
     */
    private List<T> dataSet;

    public PageV2() {
        super();
    }

    public PageV2(long total, List<T> dataSet) {
        this.total = total;
        this.dataSet = dataSet;
    }
}
