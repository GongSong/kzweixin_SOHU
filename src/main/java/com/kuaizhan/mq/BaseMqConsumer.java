package com.kuaizhan.mq;

import com.kuaizhan.exception.BaseException;
import com.kuaizhan.utils.LogUtil;

import java.io.IOException;
import java.util.HashMap;

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
    public final void handleMessage(HashMap msgMap) {
        try {
            onMessage(msgMap);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.logMsg(e);
        }
    }

    /**
     * Mq消费者实现此接口
     *
     * @param msgMap MQ消息的HashMap参数
     */
    protected abstract void onMessage(HashMap msgMap) throws Exception;
}
