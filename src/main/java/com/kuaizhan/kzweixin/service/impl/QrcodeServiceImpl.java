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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zixiong on 2017/6/1.
 */
@Service("qrcodeService")
public class QrcodeServiceImpl implements QrcodeService {

    @Resource
    private AccountService accountService;
    @Resource
    protected QrcodeMapper qrcodeMapper;

    @Override
    public String getTmpQrcode(long weixinAppid, int sceneId) {
        String accessToken = accountService.getAccessToken(weixinAppid);

        String ticket = WxCommonManager.genTmpQrcode(accessToken, sceneId);
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
        List<QrcodePO> QrcodeList = qrcodeMapper.selectByExample(example);
        if ( QrcodeList.size() == 0) {
            throw new BusinessException(ErrorCode.QRCODE_NOT_EXIST);
        }
        return QrcodeList ;

    }
}
