package com.kuaizhan.service.impl;

import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.redis.RedisFanDao;
import com.kuaizhan.exception.business.*;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.exception.system.RedisException;
import com.kuaizhan.exception.system.ServerException;
import com.kuaizhan.pojo.DO.FanDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.pojo.DTO.TagDTO;
import com.kuaizhan.service.FanService;
import com.kuaizhan.service.WeixinFanService;
import org.json.JSONArray;
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
    public List<TagDTO> listTags(long siteId, String accessToken) throws RedisException, TagGetException {

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
            throw new TagGetException();
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
    public void insertTag(long siteId, String tagName, String accessToken) throws ServerException, TagNameLengthException, TagDuplicateNameException, TagNumberException, RedisException {
        //通过微信接口创建
        int result = weixinFanService.insertTag(accessToken, tagName);
        switch (result) {
            case -1:
                throw new ServerException("微信服务器错误");
            case 1:
                try {
                    redisFanDao.deleteTag(siteId);
                } catch (Exception e) {
                    throw new RedisException(e.getMessage());
                }
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
    public void updateUserTag(long siteId, String appId, List<String> openIds, List<Integer> tagIds, String accessToken) throws ServerException, OpenIdNumberException, TagException, FanTagNumberException, OpenIdException, DaoException {

        //微信后台更新
        for (Integer tagId : tagIds) {
            int result = weixinFanService.updateTag(accessToken, openIds, tagId);
            switch (result) {
                case -1:
                    throw new ServerException("微信服务器错误");
                case 40032:
                    throw new OpenIdNumberException();
                case 45159:
                    throw new TagException();
                case 45059:
                    throw new FanTagNumberException();
                case 40003:
                    throw new OpenIdException();
                case 49003:
                    throw new OpenIdException();

            }
        }
        List<String> tables = ApplicationConfig.getFanTableNames();
        //更新数据库
        try {
            List<FanDO> fanses = fanDao.listFansByOpenIds(appId, openIds, tables);
            JSONArray jsonArray = new JSONArray();
            //json_tag里添加数据
            for (Integer tagId : tagIds) {
                jsonArray.put(tagId);
            }
            for (FanDO fans : fanses) {
                fans.setTagIdsJson(jsonArray.toString());
            }
            //设置mysql数据
            fanDao.updateFansBatch(fanses, tables);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        try {
            //清空redis缓存
            redisFanDao.deleteFanByPagination(siteId);
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public void deleteTag(long siteId, String appId, Integer tagId, String accessToken) throws ServerException, TagDeleteFansNumberException, TagDeleteException, DaoException, RedisException {
        //微信后台移除标签
        int result = weixinFanService.deleteTag(accessToken, tagId);
        switch (result) {
            case -1:
                throw new ServerException("微信服务器错误");
            case 45058:
                throw new TagDeleteException();
            case 45057:
                throw new TagDeleteFansNumberException();
            case 1:
                List<String> tables = ApplicationConfig.getFanTableNames();
                Map<String, Object> map = new HashMap<>();
                map.put("appId", appId);
                map.put("tagId", tagId);
                //获取粉丝数据
                try {
                    List<FanDO> fanses = fanDao.listFansByParam(map, tables);
                    if (fanses != null) {
                        //更新用户数据
                        for (FanDO fanDO : fanses) {
                            JSONArray jsonArray = new JSONArray(fanDO.getTagIdsJson());
                            //遍历jsonarray找tagId 删除
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (tagId == jsonArray.getInt(i)) {
                                    jsonArray.remove(i);
                                }
                            }
                            fanDO.setTagIdsJson(jsonArray.toString());
                            //设置mysql数据
                            fanDao.updateFansBatch(fanses, tables);
                        }
                    }
                } catch (Exception e) {
                    throw new DaoException(e.getMessage());
                }
                try {
                    //删除缓存
                    redisFanDao.deleteTag(siteId);
                    redisFanDao.deleteFanByPagination(siteId);
                } catch (Exception e) {
                    throw new RedisException(e.getMessage());
                }
        }
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
