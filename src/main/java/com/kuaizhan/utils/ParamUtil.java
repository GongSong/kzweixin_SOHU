package com.kuaizhan.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.kuaizhan.exception.business.ParamException;

import java.net.URL;


/**
 * 参数校验工具类
 * Created by liangjiateng on 2017/3/21.
 */
public class ParamUtil {

    private final static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

    public static void checkUnbindPostData(String postData) throws ParamException {
        URL uri = Thread.currentThread().getContextClassLoader().getResource("json-schema/account/unbind-postdata-schema.json");
        try {
            JsonNode jsonSchema = JsonLoader.fromURL(uri);
            JsonNode json = JsonLoader.fromString(postData);
            JsonSchema schema = factory.getJsonSchema(jsonSchema);
            ProcessingReport processingReport = schema.validate(json);
            if (!processingReport.isSuccess())
                throw new ParamException();
        } catch (Exception e) {
            throw new ParamException();
        }

    }

}
