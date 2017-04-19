package com.kuaizhan.pojo.VO;

import com.kuaizhan.pojo.DTO.TagDTO;
import lombok.Data;

import java.util.List;

/**
 * 粉丝展示对象
 * Created by liangjiateng on 2017/3/16.
 */
@Data
public class FanVO {
    private Long id;
    private String name;
    private String avatar;
    private Integer sex;
    private String openId;
    private String address;
    private Long focusTime;

    private List<String> tags;
}
