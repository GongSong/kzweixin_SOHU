package com.kuaizhan.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.kuaizhan.exception.deprecated.business.ParamException;

import java.io.IOException;
import java.net.URL;


/**
 * 参数校验工具类
 * Created by liangjiateng on 2017/3/21.
 */
public class ParamUtil {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();


    public static void validatePostParam(String postData, String pathToJsonSchema) throws ParamException, IOException, ProcessingException {
        URL uri = Thread.currentThread().getContextClassLoader().getResource(pathToJsonSchema);
        JsonNode jsonSchema = JsonLoader.fromURL(uri);
        JsonNode json = JsonLoader.fromString(postData);
        JsonSchema schema = factory.getJsonSchema(jsonSchema);
        ProcessingReport processingReport = schema.validate(json);
        if (!processingReport.isSuccess()){
            throw new ParamException();
        }
    }

    public static void validateRequestParam(String clsName, String methodName, String key, String value) throws ParamException {
        switch (key) {
            case "siteId":
                long siteId = Long.parseLong(value);
                if (siteId < 0) {
                    throw new ParamException();
                }
                break;
            case "weixinAppid":
                long weixinAppid = Long.parseLong(value);
                if (weixinAppid < 0) {
                    throw new ParamException();
                }
                break;
        }
    }
}
