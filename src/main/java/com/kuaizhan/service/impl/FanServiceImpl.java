package com.kuaizhan.service.impl;

import com.kuaizhan.constant.AppConstant;
import com.kuaizhan.dao.mapper.FanDao;
import com.kuaizhan.dao.redis.RedisFanDao;
import com.kuaizhan.exception.deprecated.business.*;
import com.kuaizhan.exception.common.DaoException;
import com.kuaizhan.exception.common.RedisException;
import com.kuaizhan.exception.deprecated.system.ServerException;
import com.kuaizhan.pojo.po.AccountPO;
import com.kuaizhan.pojo.po.FanPO;
import com.kuaizhan.pojo.dto.Page;
import com.kuaizhan.pojo.dto.TagDTO;
import com.kuaizhan.service.AccountService;
import com.kuaizhan.service.FanService;
import com.kuaizhan.service.WeixinFanService;
import com.kuaizhan.utils.DBTableUtil;
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
    private FanDao fanDao;
    @Resource
    private RedisFanDao redisFanDao;
    @Resource
    private WeixinFanService weixinFanService;
    @Resource
    private AccountService accountService;

    @Override
    public long countFan(String appId, int isBlack, List<Integer> tagIds, String keyword) throws DaoException {
        List<String> tables = DBTableUtil.getFanTableNames();
        try {
            List<Long> counts = fanDao.count(appId, isBlack, tagIds, keyword, tables);
            long total = 0;
            for (Long count : counts) {
                total += count;
            }
            return total;
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public FanPO getFanByOpenId(long weixinAppid, String openId) {
        AccountPO accountPO = accountService.getAccountByWeixinAppId(weixinAppid);
        String appId = accountPO.getAppId();
        String tableName = DBTableUtil.getFanTableName(appId);
        return fanDao.getFanByOpenId(openId, appId, tableName);
    }

    @Override
    public Page<FanPO> listFanByPagination(long siteId, String appId, Integer page, Integer isBlack, List<Integer> tagIds, String keyword) throws DaoException, RedisException {
        if (tagIds == null) {
            tagIds = new ArrayList<>();
        }
        if (keyword == null) {
            keyword = "";
        }
        String field = "tags:" + tagIds.toString() + "isBlack:" + isBlack + "page:" + page + "keyword:" + keyword;
        long totalNum = countFan(appId, isBlack, tagIds, keyword);
        Page<FanPO> fanDOPage = new Page<>(page, AppConstant.PAGE_SIZE_LARGE);
        fanDOPage.setTotalCount(totalNum);
        //从redis拿数据
        try {
            List<FanPO> fanses = redisFanDao.listFanByPagination(siteId, field);
            if (fanses != null) {
                fanDOPage.setResult(fanses);
                return fanDOPage;
            }
        } catch (Exception e) {
            throw new RedisException(e);
        }


        //未命中,从数据库拿数据,并且缓存到redis
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        map.put("inBlackList", isBlack);
        map.put("tagIds", tagIds);
        map.put("keyword", keyword);
        fanDOPage.setParams(map);

        List<String> tables = DBTableUtil.getFanTableNames();
        List<FanPO> fanses;
        try {
            fanses = fanDao.listFansByPagination(fanDOPage, tables);
        } catch (Exception e) {
            throw new DaoException(e);
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
            throw new RedisException(e);
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
            throw new RedisException(e);
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
                throw new RedisException(e);
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
                throw new ServerException();
            case 1:
                try {
                    redisFanDao.deleteTag(siteId);
                } catch (Exception e) {
                    throw new RedisException(e);
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
                    throw new ServerException();
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
        List<String> tables = DBTableUtil.getFanTableNames();
        //更新数据库
//        try {
//            List<FanPO> fanses = fanDao.listFansByOpenIds(appId, openIds, tables);
//            JSONArray jsonArray = new JSONArray();
//            //json_tag里添加数据
//            for (Integer tagId : tagIds) {
//                jsonArray.put(tagId);
//            }
//            for (FanPO fans : fanses) {
//                fans.setTagIdsJson(jsonArray.toString());
//            }
//            //设置mysql数据
//            fanDao.updateFansBatch(fanses, tables);
//        } catch (Exception e) {
//            throw new DaoException(e);
//        }
//        try {
//            //清空redis缓存
//            redisFanDao.deleteFanByPagination(siteId);
//        } catch (Exception e) {
//            throw new DaoException(e);
//        }
    }

    @Override
    public void deleteTag(long siteId, String appId, Integer tagId, String accessToken) throws ServerException, TagDeleteFansNumberException, TagModifyException, DaoException, RedisException {
        //微信后台移除标签
        int result = weixinFanService.deleteTag(accessToken, tagId);
        switch (result) {
            case -1:
                throw new ServerException();
            case 45058:
                throw new TagModifyException();
            case 45057:
                throw new TagDeleteFansNumberException();
            case 1:
                List<String> tables = DBTableUtil.getFanTableNames();
                Map<String, Object> map = new HashMap<>();
                map.put("appId", appId);
                map.put("tagId", tagId);
                //获取粉丝数据
                try {
                    List<FanPO> fanses = fanDao.listFansByParam(map, tables);
                    if (fanses != null) {
                        //更新用户数据
                        for (FanPO fanPO : fanses) {
                            JSONArray jsonArray = new JSONArray(fanPO.getTagIdsJson());
                            //遍历jsonarray找tagId 删除
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (tagId == jsonArray.getInt(i)) {
                                    jsonArray.remove(i);
                                }
                            }
                            fanPO.setTagIdsJson(jsonArray.toString());
                            //设置mysql数据
                            fanDao.updateFansBatch(fanses, tables);
                        }
                    }
                } catch (Exception e) {
                    throw new DaoException(e);
                }
                try {
                    //删除缓存
                    redisFanDao.deleteTag(siteId);
                    redisFanDao.deleteFanByPagination(siteId);
                } catch (Exception e) {
                    throw new RedisException(e);
                }
        }
    }

    @Override
    public void renameTag(long siteId, TagDTO newTag, String accessToken) throws TagDuplicateNameException, ServerException, TagNameLengthException, TagModifyException, RedisException {
        //微信后台重命名
        int result = weixinFanService.renameTag(accessToken, newTag.getId(), newTag.getName());
        switch (result) {
            case -1:
                throw new ServerException();
            case 45157:
                throw new TagDuplicateNameException();
            case 45158:
                throw new TagNameLengthException();
            case 45058:
                throw new TagModifyException();
        }
        try {
            redisFanDao.deleteTag(siteId);
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }

    @Override
    public void insertBlack(long siteId, String accessToken, List<FanPO> fanPOList) throws ServerException, OpenIdException, BlackAddNumberException, DaoException, RedisException {

        //注意需要保持fansId与openId的一致性
        //微信后台加入黑名单
        int result = weixinFanService.insertBlack(accessToken, fanPOList);
        switch (result) {
            case -1:
                throw new ServerException();
            case 40003:
                throw new OpenIdException();
            case 49003:
                throw new OpenIdException();
            case 40032:
                throw new BlackAddNumberException();
        }
        for (FanPO fans : fanPOList) {
            fans.setInBlackList(1);
        }
        List<String> tables = DBTableUtil.getFanTableNames();

        //更新mysql
        try {
            fanDao.updateFansBatch(fanPOList, tables);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        try {
            redisFanDao.deleteFanByPagination(siteId);
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }

    @Override
    public void deleteBlack(long siteId, List<FanPO> fanPOList, String accessToken) throws ServerException, OpenIdException, BlackAddNumberException, DaoException, RedisException {
        //注意需要保持fansId与openId的一致性
        //微信后台加入黑名单
        int result = weixinFanService.removeBlack(accessToken, fanPOList);
        switch (result) {
            case -1:
                throw new ServerException();
            case 40003:
                throw new OpenIdException();
            case 40032:
                throw new BlackAddNumberException();
            case 49003:
                throw new OpenIdException();

        }
        //更新mysql
        for (FanPO fan : fanPOList) {
            fan.setInBlackList(0);
        }

        List<String> tables = DBTableUtil.getFanTableNames();

        try {
            fanDao.updateFansBatch(fanPOList, tables);
        } catch (Exception e) {
            throw new DaoException(e);
        }
        try {
            redisFanDao.deleteFanByPagination(siteId);
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }
}
