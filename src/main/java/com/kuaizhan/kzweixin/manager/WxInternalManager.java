package com.kuaizhan.kzweixin.manager;

import com.google.common.collect.ImmutableMap;
import com.kuaizhan.kzweixin.constant.ErrorCode;
import com.kuaizhan.kzweixin.constant.WxErrCode;
import com.kuaizhan.kzweixin.controller.vo.JsonResponse;
import com.kuaizhan.kzweixin.exception.BusinessException;
import com.kuaizhan.kzweixin.exception.weixin.WxApiException;
import com.kuaizhan.kzweixin.exception.weixin.WxInvalidOpenIdException;
import com.kuaizhan.kzweixin.exception.weixin.WxOutOfResponseLimitException;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import com.kuaizhan.kzweixin.utils.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steffanchen on 2017/7/19.
 */
public class WxInternalManager {

    private static final String KZCRON_SERVICE_IP = "192.168.44.206";

    protected static String getDeleteJobUrl() {
        return "http://" + KZCRON_SERVICE_IP + "/cron/deletejobs";
    }

    protected static String getCreateJobUrl() {
        return "http://" + KZCRON_SERVICE_IP + "/cron/jobs";
    }

    /**
     * 创建定时任务接口
     * @param jobName string 任务名（id）
     * @param jobUrl string 定时执行的任务接口
     * @param startTime long 任务执行时间(10位时间戳)
     */
    public static JsonResponse createTimingJob(String jobName, String jobUrl, long startTime) throws BusinessException {
        Map<String, String> param = new HashMap<>();
        param.put("name", jobName);
        param.put("url", jobUrl);
        param.put("start", String.valueOf(startTime * 1000));
        String paramStr = JsonUtil.bean2String(param);
        String result = HttpClientUtil.postJson(getCreateJobUrl(), paramStr);
        if (result == null) {
            throw new BusinessException(ErrorCode.TIMING_JOB_NOT_EXIST);
        }
        try{
            JSONObject resultJson = new JSONObject(result);
            int errCode = resultJson.optInt("errcode");
            if (errCode != 0) {
                throw new BusinessException(ErrorCode.WEIXIN_JOB_ERROR);
            }
        } catch (JSONException e) {
            throw new BusinessException(ErrorCode.JSON_OBJECT_ERROR);
        }

        return new JsonResponse(ErrorCode.SUCCESS.getCode(), "creating timing job success", ImmutableMap.of());
    }
    /**
     * 删除定时任务接口
     * @param jobName string 任务名（id）
     */
    public static JsonResponse deleteTimingJob(String jobName) throws BusinessException {
        Map<String, String> param = new HashMap<>();
        param.put("name", jobName);
        String paramStr = JsonUtil.bean2String(param);
        String result = HttpClientUtil.postJson(getDeleteJobUrl(), paramStr);
        if (result == null) {
            throw new BusinessException(ErrorCode.TIMING_JOB_NOT_EXIST);
        }
        try{
            JSONObject resultJson = new JSONObject(result);
            int errCode = resultJson.optInt("errcode");
            if (errCode != 0) {
                throw new BusinessException(ErrorCode.WEIXIN_JOB_ERROR);
            }
        } catch (JSONException e) {
            throw new BusinessException(ErrorCode.JSON_OBJECT_ERROR);
        }
        return new JsonResponse(ErrorCode.SUCCESS.getCode(), "delete timing job success", ImmutableMap.of());
    }
}
