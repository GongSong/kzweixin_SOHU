package com.kuaizhan.kzweixin.manager.db;

import com.kuaizhan.kzweixin.constant.PostConstant;
import com.kuaizhan.kzweixin.dao.mapper.PostDao;
import com.kuaizhan.kzweixin.dao.mongo.MongoPostDao;
import com.kuaizhan.kzweixin.dao.po.PostPO;
import com.kuaizhan.kzweixin.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by zixiong on 2017/7/12.
 */
@Component
public class PostDBManager {

    @Resource
    private MongoPostDao mongoPostDao;
    @Resource
    private PostDao postDao;

    private static final Logger logger = LoggerFactory.getLogger(PostDBManager.class);

    /**
     * 生成pageId主键
     */
    public long genPageId() {
        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long pageId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (postDao.isPageIdExist(pageId)) {
            pageId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }
        // 随机三次及以上才随机到，报警
        if (count >= 3) {
            logger.warn("[genPageId] gen times reach {}", count);
        }
        return pageId;
    }

    /**
     * 新增多图文
     * 必须先经过cleanPosts清洗数据
     * @return 返回总图文pageId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public long saveMultiPosts(long weixinAppid, List<PostPO> posts) {

        // 记录pageId
        long mainPageId = 0;

        // 1. 单图文 2. 多图文总记录 3. 多图文中的一条
        short type = (short) (posts.size() > 1 ? 3 : 1);

        int index = 0;
        for (PostPO postPO : posts) {

            // 生成pageId
            long pageId = genPageId();
            mainPageId = pageId;

            postPO.setPageId(pageId);
            postPO.setWeixinAppid(weixinAppid);
            postPO.setType(type);
            postPO.setIndex(index++);

            // 保存content
            mongoPostDao.upsertPost(pageId, postPO.getContent());
            // 保存其他
            postDao.insertPost(postPO);
        }

        // 如果是多图文，生成一条type为2的图文总记录
        if (posts.size() > 1) {

            // 生成pageId
            long pageId = genPageId();
            mainPageId = pageId;

            PostPO sumPost = new PostPO();
            // 有效数据
            sumPost.setWeixinAppid(weixinAppid);
            sumPost.setPageId(pageId);
            sumPost.setType((short) 2);
            sumPost.setIndex(0);
            sumPost.setMediaId(posts.get(0).getMediaId());
            sumPost.setUpdateTime(posts.get(0).getUpdateTime());
            // 把title拼接起来，保存多图文总记录里面
            StringBuilder sumTitle = new StringBuilder();
            for (PostPO postPO : posts) {
                sumTitle.append(postPO.getTitle());
            }
            int endIndex = sumTitle.length() < PostConstant.TITLE_MAX? sumTitle.length(): PostConstant.TITLE_MAX;
            sumPost.setTitle(sumTitle.toString().substring(0, endIndex));

            // 无效数据
            sumPost.setThumbMediaId("");
            sumPost.setThumbUrl("");
            sumPost.setAuthor("");
            sumPost.setDigest("");
            sumPost.setContentSourceUrl("");
            sumPost.setShowCoverPic((short) 0);

            postDao.insertPost(sumPost);
        }

        return mainPageId;
    }

    /**
     * 修改多图文
     * 此方法假设没有修改图文数目
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateMultiPosts(long weixinAppid, String mediaId, List<PostPO> posts) {
        // 更新每篇图文的数据库，修改内容
        StringBuilder titleSum = new StringBuilder();

        int updateTime = DateUtil.curSeconds();

        for (PostPO postPO : posts) {

            postPO.setUpdateTime(updateTime);
            mongoPostDao.upsertPost(postPO.getPageId(), postPO.getContent());
            postDao.updatePost(postPO, postPO.getPageId());

            // 拼接总图文title
            titleSum.append(postPO.getTitle());
        }

        // 修改图文总记录
        if (posts.size() > 1) {
            PostPO postSum = postDao.getPostByMediaId(weixinAppid, mediaId);
            if (postSum.getType() != 2) {
                logger.error("[图文垃圾数据], weixinAppid:{} mediaId:{}", weixinAppid, mediaId);
            }

            postSum.setUpdateTime(updateTime);
            int endIndex = titleSum.length() < PostConstant.TITLE_MAX? titleSum.length(): PostConstant.TITLE_MAX;
            postSum.setTitle(titleSum.toString().substring(0, endIndex));
            postSum.setMediaId(posts.get(0).getMediaId());
            postDao.updatePost(postSum, postSum.getPageId());
        }
    }
}
