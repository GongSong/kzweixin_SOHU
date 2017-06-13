package com.kuaizhan.controller.param;

import com.kuaizhan.pojo.po.PostPO;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by zixiong on 2017/4/12.
 */
@Data
public class PostsParamItem {

    @Size(min = 1, max = 64, message = "图文的标题不能为空或者只含有空格（标题会过滤emoji表情等特殊字符")
    private String title;

    // 作者字段前端不能编辑，不校验
    private String author;

    @Size(max = 120, message = "摘要不能超过120个字符")
    private String digest;

    @NotNull(message = "封面图不能为空")
    @NotBlank(message = "封面图不能为空")
    private String thumbUrl;

    @NotNull(message = "内容不能为空")
    @NotBlank(message = "内容不能为空")
    private String content;

    @Size(max = 512, message = "原文链接太长, 不超过512字符")
    private String contentSourceUrl;

    private String thumbMediaId;
    private Short showCoverPic;

    private Long pageId;
    private String mediaId;


    public PostPO toPostDO() {
        PostPO postPO = new PostPO();

        postPO.setTitle(title);
        postPO.setAuthor(author);
        postPO.setDigest(digest);
        postPO.setContent(content);
        postPO.setThumbMediaId(thumbMediaId);
        postPO.setThumbUrl(thumbUrl);
        postPO.setContentSourceUrl(contentSourceUrl);
        postPO.setShowCoverPic(showCoverPic);
        postPO.setPageId(pageId);
        postPO.setMediaId(mediaId);

        return postPO;
    }
}
