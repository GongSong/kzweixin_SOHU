package com.kuaizhan.param;

import com.kuaizhan.pojo.DO.PostDO;
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

    private String thumbMediaId;
    private String contentSourceUrl;
    private Short showCoverPic;

    private Long pageId;
    private String mediaId;


    public PostDO toPostDO() {
        PostDO postDO = new PostDO();

        postDO.setTitle(title);
        postDO.setAuthor(author);
        postDO.setDigest(digest);
        postDO.setContent(content);
        postDO.setThumbMediaId(thumbMediaId);
        postDO.setThumbUrl(thumbUrl);
        postDO.setContentSourceUrl(contentSourceUrl);
        postDO.setShowCoverPic(showCoverPic);
        postDO.setPageId(pageId);
        postDO.setMediaId(mediaId);

        return postDO;
    }
}
