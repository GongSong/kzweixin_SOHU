package com.kuaizhan.utils;

import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.VO.PostVO;
import org.springframework.stereotype.Component;

/**
 * pojo的转换组件
 * Created by zixiong on 2017/3/30.
 */
@Component("pojoSwitcher")
public class PojoSwitcher {

    public PostVO postDOToVO(PostDO postDO) {

        if (postDO == null) {
            return null;
        }

        PostVO postVO = new PostVO();
        postVO.setPageId(postDO.getPageId());
        postVO.setMediaId(postDO.getMediaId());
        postVO.setTitle(postDO.getTitle());
        postVO.setAuthor(postDO.getAuthor());
        postVO.setDigest(postDO.getAuthor());
        postVO.setContent(postDO.getContent());
        postVO.setThumbUrl(postDO.getThumbUrl());
        postVO.setThumbMediaId(postDO.getThumbMediaId());
        postVO.setContentSourceUrl(postDO.getContentSourceUrl());
        postVO.setUpdateTime(postDO.getUpdateTime());
        return postVO;
    }
}
