package com.kuaizhan.kzweixin.entity.http.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by zixiong on 2017/08/09.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KzCallbackResponse {
    private Integer ret;
    private String result;
}
