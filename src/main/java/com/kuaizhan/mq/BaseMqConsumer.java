package com.kuaizhan.mq;

import com.kuaizhan.utils.LogUtil;

import java.util.Map;

/**
 * Mq消费者接口
 * Created by zixiong on 2017/3/22.
 */

public abstract class BaseMqConsumer {
    /**
     * 消息处理接口
     *
     * @param msgMap MQ消息的HashMap参数
     */
    public final void handleMessage(Map msgMap) {
        try {
            onMessage(msgMap);
        } catch (Exception e) {
            LogUtil.logMsg(e);
        }
    }

    /**
     * Mq消费者实现此接口
     *
     * @param msgMap MQ消息的HashMap参数
     */
    protected abstract void onMessage(Map msgMap) throws Exception;
}
