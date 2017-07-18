package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.config.WxApiConfig;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.dao.mapper.auto.QrcodeMapper;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.dao.po.auto.QrcodePO;
import com.kuaizhan.kzweixin.dao.po.auto.QrcodePOExample;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.manager.WxCommonManager;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.QrcodeService;
import com.kuaizhan.kzweixin.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by zixiong on 2017/6/1.
 */
@Service("qrcodeService")
public class QrcodeServiceImpl implements QrcodeService {

    @Resource
    private AccountService accountService;
    @Resource
    protected QrcodeMapper qrcodeMapper;

    private static final Logger logger = LoggerFactory.getLogger(QrcodeServiceImpl.class);

    @Override
    public String getTmpQrcode(long weixinAppid, long sceneId) {
        String accessToken = accountService.getAccessToken(weixinAppid);

        String ticket = WxCommonManager.genTmpQrcode(accessToken, sceneId);
        return WxApiConfig.qrcodeUrl(ticket);
    }

    @Override
    public String genQrcodeByWxAppId(long weixinAppid,int respType,String respJson,String qrName) {
        String accessToken = accountService.getAccessToken(weixinAppid);
        long sceneId = genSceneId();
        String ticket = WxCommonManager.genTmpQrcode(accessToken, sceneId);
        QrcodePO qrcodePO=new QrcodePO();
        long qrcodeId = genQrcodeId();
        qrcodePO.setQrcodeId(qrcodeId);
        qrcodePO.setWeixinAppid(weixinAppid);
        qrcodePO.setQrcodeName(qrName);
        qrcodePO.setSceneId(sceneId);
        qrcodePO.setTicket(ticket);
        qrcodePO.setResponseType(respType);
        qrcodePO.setResponseJson(respJson);
        qrcodePO.setStatus(1);
        qrcodePO.setCreateTime(DateUtil.curSeconds());
        qrcodePO.setUpdateTime(DateUtil.curSeconds());
        int insertResult =qrcodeMapper.insert(qrcodePO);
        if (insertResult <= 0) {
            throw new BusinessException(ErrorCode.QRCODE_INSERT_ERROR);
        }
        return WxApiConfig.qrcodeUrl(ticket);
    }

    @Override
    public List<QrcodePO> getQrcodeByWxAppId(long weixinAppid,String query){
        QrcodePOExample example = new QrcodePOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(weixinAppid)
                .andQrcodeNameLike("%"+query+"%")
                .andStatusEqualTo(1);
        example.setOrderByClause("update_time desc");
        List<QrcodePO> qrcodeList = qrcodeMapper.selectByExample(example);
        if ( qrcodeList.size() == 0) {
            throw new BusinessException(ErrorCode.QRCODE_NOT_EXIST);
        }
        return qrcodeList ;

    }

    @Override
    public long genSceneId() {
        final long MIN = 96000000L;
        final long MAX = 99999999L;
        int count = 1;

        long sceneId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (qrcodeMapper.isSceneIdExist(sceneId)) {
            sceneId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }

        if (count >= 3) {
            logger.warn("[genPageId] gen times reach {}", count);
        }
        return sceneId;
    }
    @Override
    public long genQrcodeId() {
        final long MIN = 1000000000L;
        final long MAX = 9999999999L;
        int count = 1;

        long qrcodeId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
        while (qrcodeMapper.isQrcodeIdExist(qrcodeId)){
            qrcodeId = ThreadLocalRandom.current().nextLong(MIN, MAX + 1);
            count++;
        }

        if (count >= 3) {
            logger.warn("[genPageId] gen times reach {}", count);
        }
        return qrcodeId;
    }
}

