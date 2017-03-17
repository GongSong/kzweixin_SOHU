package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.redis.RedisFanDao;
import com.kuaizhan.exception.business.TagDuplicateNameException;
import com.kuaizhan.exception.business.TagException;
import com.kuaizhan.exception.business.TagNameLengthException;
import com.kuaizhan.exception.business.TagNumberException;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.exception.system.ServerException;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.service.FanService;
import com.kuaizhan.service.WeixinFanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjiateng on 2017/3/16.
 */
@Service("fanService")
public class FanServiceImpl implements FanService {

    @Resource
    FanDao fanDao;
    @Resource
    RedisFanDao redisFanDao;
    @Resource
    WeixinFanService weixinFanService;

    @Override
    public long countFan(String appId, int isBlack, List<Integer> tagIds, String keyword) throws DaoException {
        List<String> tables = ApplicationConfig.getFanTableNames();
        try {
            List<Long> counts = fanDao.count(appId, isBlack, tagIds, keyword, tables);
            long total = 0;
            for (Long count : counts) {
                total += count;
            }
            return total;
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Page<FanDO> listFanByPagination(long siteId, String appId, Integer page, Integer isBlack, List<Integer> tagIds, String keyword) throws DaoException, RedisException {
        if (tagIds == null) {
            tagIds = new ArrayList<>();
        }
        if (keyword == null) {
            keyword = "";
        }
        String field = "tags:" + tagIds.toString() + "isBlack:" + isBlack + "page:" + page + "keyword:" + keyword;
        long totalNum = countFan(appId, isBlack, tagIds, keyword);
        Page<FanDO> fanDOPage = new Page<>(page, ApplicationConfig.PAGE_SIZE_LARGE);
        fanDOPage.setTotalCount(totalNum);
        //从redis拿数据
        try {
            List<FanDO> fanses = redisFanDao.listFanByPagination(siteId, field);
            if (fanses != null) {
                fanDOPage.setResult(fanses);
                return fanDOPage;
            }
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }


        //未命中,从数据库拿数据,并且缓存到redis
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("inBlackList", isBlack);
        map.put("tagIds", tagIds);
        map.put("keyword", keyword);
        fanDOPage.setParams(map);

        List<String> tables = ApplicationConfig.getFanTableNames();
        List<FanDO> fanses;
        try {
            fanses = fanDao.listFansByPagination(fanDOPage, tables);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        try {
            //存缓存
            if (fanses.size() > 20) {
                redisFanDao.setFanByPagination(siteId, field, fanses.subList(0, 20));
                fanDOPage.setResult(fanses.subList(0, 20));
            } else {
                redisFanDao.setFanByPagination(siteId, field, fanses.subList(0, fanses.size()));
                fanDOPage.setResult(fanses.subList(0, fanses.size()));
            }
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
        return fanDOPage;
    }

    @Override
    public List<TagDTO> listTags(long siteId, String accessToken) throws RedisException, TagException {

        List<TagDTO> tagDTOList;
        //从redis拿数据
        try {
            tagDTOList = redisFanDao.listTags(siteId);
            if (tagDTOList != null) {
                return tagDTOList;
            }
        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }

        List<TagDTO> tags;
        //没有redis数据源,从微信后台拿数据
        try {
            tags = weixinFanService.listTags(accessToken);
        } catch (Exception e) {
            throw new TagException();
        }

        //存到redis
        if (tags != null) {
            try {
                redisFanDao.setTag(siteId, tags);
            } catch (Exception e) {
                throw new RedisException(e.getMessage());
            }
        }
        return tags;

    }

    @Override
    public void insertTag(long siteId, String tagName, String accessToken) throws ServerException, TagNameLengthException, TagDuplicateNameException, TagNumberException {
        //通过微信接口创建
        int result = weixinFanService.insertTag(accessToken, tagName);
        switch (result) {
            case -1:
                throw new ServerException("微信服务器错误");
            case 1:
                redisFanDao.deleteTag(siteId);
                break;
            case 45157:
                throw new TagDuplicateNameException();
            case 45158:
                throw new TagNameLengthException();
            case 45056:
                throw new TagNumberException();
        }
    }

    @Override
    public void updateUserTag(String appId, List<String> openIds, List<Integer> tagIds, String accessToken) {

    }

    @Override
    public void deleteTag(String appId, Integer tagId, String accessToken) {

    }

    @Override
    public void renameTag(String appId, TagDTO newTag, String accessToken) {

    }

    @Override
    public void insertBlack(String appId, String accessToken, List<FanDO> fanDOList) {

    }

    @Override
    public void deleteBlack(String appId, List<FanDO> fanDOList, String accessToken) {

    }
}
