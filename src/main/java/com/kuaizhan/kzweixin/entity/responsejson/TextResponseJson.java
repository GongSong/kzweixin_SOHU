package com.kuaizhan.kzweixin.entity.responsejson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/08/01.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextResponseJson implements ResponseJson {
    @NotNull
    private String content;

    @Override
    public void cleanBeforeInsert() {
    }

    @Override
    public void cleanAfterSelect() {
    }
}
