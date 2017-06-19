package com.kuaizhan.kzweixin.mq.consumer;

import com.kuaizhan.kzweixin.manager.KzManager;
import com.kuaizhan.kzweixin.mq.dto.AfterBindDTO;
import com.kuaizhan.kzweixin.utils.JsonUtil;

/**
 * 用户新绑定后做的事情
 * Created by zixiong on 2017/6/19.
 */
public class AfterBindConsumer extends  BaseConsumer{

    @Override
    void onMessage(String message) {
        AfterBindDTO dto = JsonUtil.string2Bean(message, AfterBindDTO.class);
    }
}
