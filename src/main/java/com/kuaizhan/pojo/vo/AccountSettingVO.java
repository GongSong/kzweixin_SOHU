package com.kuaizhan.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by zixiong on 2017/6/9.
 */
@Data
public class AccountSettingVO {
    private List<String> interest;
    private Boolean openLogin;
    private Boolean openShare;
}
