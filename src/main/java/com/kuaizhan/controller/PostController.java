package com.kuaizhan.controller;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DO.PostDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.VO.JsonResponse;
import com.kuaizhan.pojo.VO.PostListVO;
import com.kuaizhan.pojo.VO.PostVO;
import com.kuaizhan.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 图文消息controller
 * Created by zixiong on 2017/3/20.
 */
@RestController
@RequestMapping(value = ApplicationConfig.VERSION, produces = "application/json")
public class PostController extends BaseController{

    @Resource
    PostService postService;


    @RequestMapping(value = "/posts", method = RequestMethod.GET)
    public JsonResponse listPostByPagination(@RequestParam long weixinAppid, @RequestParam int page) throws DaoException {

        Page<PostDO> postDOPage = postService.listPostsByPagination(weixinAppid, page);

        List<PostDO> postDOList = postDOPage.getResult();
        PostListVO postListVO = new PostListVO();

        if (postDOList != null) {
            postListVO.setTotalNum(postDOPage.getTotalCount());
            postListVO.setCurrentPage(postDOPage.getPageNo());
            postListVO.setTotalPage(postDOPage.getTotalPages());

            for (PostDO postDO : postDOList) {
                PostVO postVO = new PostVO();
                postVO.setPageId(postDO.getPageId());
                postVO.setAuthor(postDO.getAuthor());
                postVO.setDigest(postDO.getAuthor());
                postVO.setThumbUrl(postDO.getThumbUrl());
                postVO.setTitle(postDO.getTitle());
                postVO.setType(postDO.getType());
                postVO.setUpdateTime(postDO.getUpdateTime());
                postListVO.getPosts().add(postVO);

                // 获取图文下面的多图文
                if (postDO.getType() == 2) {
                    List<PostDO> multiPostDOList = postService.listMultiPosts(postDO.getMediaId());

                    if (multiPostDOList != null) {
                        for (PostDO multiPostDo : multiPostDOList) {
                            PostVO multiPostVO = new PostVO();
                            multiPostVO.setPageId(multiPostDo.getPageId());
                            multiPostVO.setAuthor(multiPostDo.getAuthor());
                            multiPostVO.setDigest(multiPostDo.getAuthor());
                            multiPostVO.setThumbUrl(multiPostDo.getThumbUrl());
                            multiPostVO.setTitle(multiPostDo.getTitle());
                            multiPostVO.setType(multiPostDo.getType());
                            multiPostVO.setUpdateTime(multiPostDo.getUpdateTime());
                            postVO.getMultiPosts().add(multiPostVO);
                        }
                    }
                }
            }
        }
        return new JsonResponse(postListVO);
    }
}
