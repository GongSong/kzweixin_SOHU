package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.PostDao;
import com.kuaizhan.exception.business.MaterialDeleteException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.PostService;
import com.kuaizhan.service.WeixinPostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zixiong on 2017/3/20.
 */
@Service("postService")
public class PostServiceImpl implements PostService {

    @Resource
    PostDao postDao;
    @Resource
    WeixinPostService weixinPostService;


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

    @Override
    public void deletePost(long pageId,String accessToken) throws DaoException, MaterialDeleteException {

        PostDO postDO = getPostByPageId(pageId);
        if (postDO != null) {
            String mediaId = postDO.getMediaId();
            //微信删除
            weixinPostService.deletePost(mediaId,accessToken);
            //数据库删除
            try {
                postDao.deletePost(mediaId);
            } catch (Exception e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public PostDO getPostByPageId(long pageId) throws DaoException {
        try {
            return postDao.getPost(pageId);
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

}
