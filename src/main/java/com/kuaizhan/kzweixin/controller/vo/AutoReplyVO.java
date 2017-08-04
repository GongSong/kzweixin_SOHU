package com.kuaizhan.kzweixin.controller.vo;

import com.kuaizhan.kzweixin.entity.responsejson.ResponseJson;
import com.kuaizhan.kzweixin.enums.ComponentResponseType;
import lombok.Data;

/**
 * Created by fangtianyu on 8/3/17.
 */
@Data
public class AutoReplyVO {
    private Integer id;
    private Long weixinAppid;
    private ComponentResponseType responseType;
    private ResponseJson responseJson;
}
