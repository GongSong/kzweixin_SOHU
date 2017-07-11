package com.kuaizhan.kzweixin.service.impl;

import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.dao.mapper.auto.MassMapper;
import com.kuaizhan.kzweixin.dao.po.auto.MassPO;
import com.kuaizhan.kzweixin.dao.po.auto.MassPOExample;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.service.MassService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Chen on 17/7/11.
 */
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
}
