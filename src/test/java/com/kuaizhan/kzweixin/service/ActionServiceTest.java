package com.kuaizhan.kzweixin.service;

import com.kuaizhan.kzweixin.dao.po.auto.ActionPO;
import com.kuaizhan.kzweixin.enums.ActionType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 * Created by zixiong on 2017/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class ActionServiceTest {

    @Resource
    private ActionService actionService;

    @Test
    public void shouldAction() throws Exception {

        ActionPO actionPO = new ActionPO();
        actionPO.setActionType(ActionType.SUBSCRIBE);
        Assert.assertTrue(actionService.shouldAction(actionPO, null));

        actionPO.setActionType(ActionType.REPLY);
        actionPO.setKeyword(".*投票.*");
        Assert.assertTrue(actionService.shouldAction(actionPO, "我们来投票啊"));
        Assert.assertFalse(actionService.shouldAction(actionPO, ""));
        Assert.assertFalse(actionService.shouldAction(actionPO, null));
    }

}