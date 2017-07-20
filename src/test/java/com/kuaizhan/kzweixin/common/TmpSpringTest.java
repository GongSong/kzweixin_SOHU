package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.entity.XmlData;
import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import com.kuaizhan.kzweixin.service.AccountService;
import com.kuaizhan.kzweixin.service.WxPushService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


/**
 * Created by zixiong on 2017/4/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
public class TmpSpringTest {

    @Resource
    private AccountService accountService;
    @Resource
    private WxPushService wxPushService;

    @Test
    public void getAccessToken() throws Exception {
        String accessToken = accountService.getAccessToken(ApplicationConfig.WEIXIN_TEST_WEIXIN_APPID);
        String openId = "oBGGJt5SLU9P_wGu71Xo82m_Zq1s";
        System.out.println("---->" + accessToken);

    }

    @Test
    public void tmp() throws Exception {

        String xmlStr = "" +
                "<xml><ToUserName><![CDATA[gh_c480b0a49842]]></ToUserName>\n" +
                "<FromUserName><![CDATA[oBGGJt5SLU9P_wGu71Xo82m_Zq1s]]></FromUserName>\n" +
                "<CreateTime>1498632800</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[aaa]]></Content>\n" +
                "<MsgId>6436578865132716922</MsgId>\n" +
                "</xml>"
                ;
        Document document;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            throw new XMLParseException("[handleEventPush] xml parse failed, xmlStr:" + xmlStr, e);
        }
        Element root = document.getRootElement();

        XmlData xmlData = new XmlData();
        // 必有字段
        xmlData.setAppId("wx1a4ff9ec0e369bd1");
        xmlData.setFromUserName(root.elementText("FromUserName"));
        xmlData.setToUserName(root.elementText("ToUserName"));
        xmlData.setMsgType(root.elementText("MsgType"));
        xmlData.setCreateTime(root.elementText("CreateTime"));
    }
}
