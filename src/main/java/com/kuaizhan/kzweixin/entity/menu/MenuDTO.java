package com.kuaizhan.kzweixin.entity.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单json对象
 * Created by zixiong on 2017/5/25.
 */
@Data
public class MenuDTO {
    @NotNull
    @Size(min = 1, max = 3, message = "最多可创建 3 个一级菜单")
    @JsonProperty("button")
    private List<Button> buttons = new ArrayList<>();

    @Data
    @JsonInclude(Include.NON_NULL)
    public static class Button {
        private String type;
        @NotNull(message = "菜单名称不能为空")
        private String name;
        @NotNull(message = "menu key不能为空")
        private Long key;
        // type == view时有url
        private String url;
        // type == media_id || type == view_limited 时有media_id
        private String media_id;


        @Size(max = 5, message = "每个一级菜单下最多可创建 5 个子菜单")
        @JsonProperty("sub_button")
        private List<Button> subButton = new ArrayList<>();
    }
    // TODO: 校验，view类型，必须有url
}
