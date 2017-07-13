package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.dao.mapper.auto.MassMapper;
import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPO;
import com.kuaizhan.kzweixin.dao.po.auto.CustomMassPOExample;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPOExample;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.service.MassService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 17/7/11.
 */
@Service("massService")
public class MassServiceImpl implements MassService {

    @Resource
    protected MassMapper massMapper;

    @Override
    public MassPO getMassById(long id) {
        MassPOExample example = new MassPOExample();
        example.createCriteria()
                .andMassIdEqualTo(id)
                .andIsTimingEqualTo(1)
                .andStatusNotEqualTo(0);
        List<MassPO> massList = massMapper.selectByExample(example);
        if (massList.size() == 0) {
            throw new BusinessException(ErrorCode.MASS_NOT_EXIST);
        }
        return massList.get(0);
    }

    @Override
    public List<MassPO> getMassByWxAppId(long wxAppId) {
        MassPOExample example = new MassPOExample();
        example.createCriteria()
                .andWeixinAppidEqualTo(wxAppId)
                .andStatusNotEqualTo(0);
        example.setOrderByClause("publish_time desc");
        List<MassPO> massList = massMapper.selectByExample(example);
        if (massList.size() == 0) {
            throw new BusinessException(ErrorCode.MASS_NOT_EXIST);
        }
        return massList;
    }

    @Override
    public List<CustomMassPO> getCustomMassByWxAppId(long wxAppid) {
        CustomMassPOExample example = new CustomMassPOExample();
        //example.createCriteria()
        example.setOrderByClause("update_time desc");
        return new ArrayList<CustomMassPO>();
    }
}
