package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service("postService")
public class PostServiceImpl implements PostService{

    @Resource
    PostDao postDao;


    @Override
    public Page<PostDO> listPostsByPagination(long weixinAppid, Integer page) throws DaoException {

        Page<PostDO> postDOPage = new Page<>(page, ApplicationConfig.PAGE_SIZE_LARGE);

        try {
            List<PostDO> posts = postDao.listPostsByPagination(weixinAppid, postDOPage);
            postDOPage.setResult(posts);

            long totalCount = postDao.count(weixinAppid);
            postDOPage.setTotalCount(totalCount);

        } catch (Exception e) {
            throw new DaoException(e);
        }

        return postDOPage;
    }

    @Override
    public List<PostDO> listMultiPosts(String mediaId) throws DaoException {

        List<PostDO> multiPosts;

        try {
            multiPosts = postDao.listMultiPosts(mediaId);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        return multiPosts;
    }

}
