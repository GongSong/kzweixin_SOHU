package com.kuaizhan.service.impl;


import com.kuaizhan.config.ApplicationConfig;
import com.kuaizhan.dao.mapper.MsgDao;
import com.kuaizhan.exception.system.DaoException;
import com.kuaizhan.pojo.DO.MsgDO;
import com.kuaizhan.pojo.DTO.Page;
import com.kuaizhan.service.MsgService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 消息管理服务
 * Created by Mr.Jadyn on 2017/1/10.
 */
@Service("msgService")
public class MsgServiceImpl implements MsgService {

    @Resource
    MsgDao msgDao;

    @Override
    public long countMsg(String appId, int status, String keyword, int isHide) throws DaoException {
        if (keyword == null) {
            keyword = "";
        }
        List<String> tableNames = ApplicationConfig.getMsgTableNames();
        long total = 0;
        try {
            List<Long> counts = msgDao.count(appId, status, keyword, isHide, tableNames);
            for (Long count : counts) {
                total += count;
            }
        } catch (Exception e) {
            throw new DaoException(e.getMessage());
        }
        return total;
    }

    @Override
    public Page<MsgDO> listMsgsByPagination(String appId, int page, String keyword, int isHide) throws IOException {
        return null;
    }

    @Override
    public List<MsgDO> listNewMsgs(String appId) {
        return null;
    }

    @Override
    public List<MsgDO> listMsgsByOpenId(String appId, String openId, int page) throws IOException {
        return null;
    }

    @Override
    public void updateMsgsStatus(String appId, List<MsgDO> msgs) {

    }

    @Override
    public int sendMsgByOpenId(String appId, String accessToken, String openId, int msgType, JSONObject content) {
        return 0;
    }
}
