package com.kuaizhan.pojo.DO;

import lombok.Data;

/**
 * Created by czm on 2017/1/16.
 * 有1标注的表示可为空
 */
@Data
public class PostDO {

    private Long pageId;
    private Long weixinAppid;
    private String title ;
    private String  thumbMediaId;
    private String thumbUrl;
    private Short showCoverPic;
    private String author;
    private String digest;
    private String postUrl = "";
    private String contentSourceUrl;
    private String mediaId;
    private Integer syncTime;
    private Short type; // 1.单图文 2. 多图文总记录 3.多图文中的一条
    private Integer index;
    private Short status; //1 为正常发布，2 为删除
    private Integer createTime;
    private Integer updateTime;
    private String content; //图文的内容
}
