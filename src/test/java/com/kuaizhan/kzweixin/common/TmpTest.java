package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.config.AppConfig;
import com.kuaizhan.kzweixin.entity.http.response.KzCallbackResponse;
import com.kuaizhan.kzweixin.entity.msg.MsgLinkGroupResponseJson;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
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
        AppConfig appConfig = new AppConfig();
        appConfig.init();
        HttpResponse<KzCallbackResponse> httpResponse;
        try {
            httpResponse = Unirest.get("http://localhost:8080/public/v1/test").asObject(KzCallbackResponse.class);
        } catch (UnirestException e) {
            e.printStackTrace();
            throw e;
        }
        KzCallbackResponse response = httpResponse.getBody();
        System.out.println("---->" + httpResponse.getStatus());
        System.out.println("---->" + httpResponse.getRawBody());
        System.out.println("---->" + response.getResult());
    }
}
