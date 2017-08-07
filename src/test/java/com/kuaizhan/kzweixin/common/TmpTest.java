package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.entity.msg.MsgLinkGroupResponseJson;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * 临时测试入口, 不用加载spring的测试
 * Created by zixiong on 2017/4/12.
 */
public class TmpTest {

    private static final Logger logger = LoggerFactory.getLogger(TmpTest.class);

    @Test
    public void testXml() throws Exception {
        MsgLinkGroupResponseJson responseJson = new MsgLinkGroupResponseJson();
        responseJson.setLinkGroups(new ArrayList<>());
        MsgLinkGroupResponseJson.LinkGroup linkGroup = new MsgLinkGroupResponseJson.LinkGroup();
        linkGroup.setUrl("url");
        linkGroup.setPicUrl("pic url");
        linkGroup.setDescription("descri");
        linkGroup.setTitle("title");
        linkGroup.setOldPicUrl("oldPic");

        responseJson.getLinkGroups().add(linkGroup);
        System.out.println("---->" + JsonUtil.bean2String(responseJson));
    }

    @Test
    public void testInt() throws Exception {
    }
}
