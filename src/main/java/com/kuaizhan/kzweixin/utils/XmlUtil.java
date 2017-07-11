package com.kuaizhan.kzweixin.utils;

import com.kuaizhan.kzweixin.exception.common.XMLParseException;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * xml工具类
 * Created by zixiong on 2017/7/7.
 */
public class XmlUtil {

    /**
     * 解析xml
     * @param xmlStr 字符串
     * @return xml文档
     */
    public static Document parseXml(String xmlStr) {
        xmlStr = removeInvalidChar(xmlStr);

        Document document;
        try {
            document = DocumentHelper.parseText(xmlStr);
        } catch (DocumentException e) {
            throw new XMLParseException("[handleEventPush] xml parse failed, xmlStr:" + xmlStr, e);
        }
        return document;
    }

    /**
     * 过滤xml标准规定的无效字符，以空格代替
     */
    public static String removeInvalidChar(String xmlStr) {
        if (StringUtils.isBlank(xmlStr)) {
            return xmlStr;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < xmlStr.length(); i++) {
            char curChar = xmlStr.charAt(i);
            if ((curChar == 0x9)
                    || (curChar == 0xA)
                    || (curChar == 0xD)
                    || ((curChar >= 0x20) && (curChar <= 0xD7FF))
                    || ((curChar >= 0xE000) && (curChar <= 0xFFFD))
                    || ((curChar >= 0x10000) && (curChar <= 0x10FFFF))
                    ) {
                builder.append(curChar);
            } else {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
}
