package com.kuaizhan.kzweixin.entity.responsejson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by zixiong on 2017/08/01.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageResponseJson implements ResponseJson {

    @NotNull
    private String mediaId;
    @NotNull
    private String picUrl;

    /* php兼容字段 */
    @JsonProperty("media_id")
    private String OldMediaId;
    @JsonProperty("pic_url")
    private String OldPicUrl;

    @Override
    public void cleanBeforeInsert() {
        setOldMediaId(mediaId);
        setOldPicUrl(picUrl);
    }

    @Override
    public void cleanAfterSelect() {
        // 是老数据
        if (mediaId == null) {
            setMediaId(getOldMediaId());
            setPicUrl(getOldPicUrl());
        }
        // 清空老数据，向前端屏蔽
        setOldPicUrl(null);
        setOldMediaId(null);
    }
}
