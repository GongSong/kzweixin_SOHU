package com.kuaizhan.kzweixin.manager;

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

    public static void deleteTimingJob(String jobName, String jobUrl, long startTime) {
        Map<String, String> param = new HashMap<>();
        param.put("name", jobName);
        param.put("url", jobUrl);
        param.put("start", String.valueOf(startTime * 1000));
    }
}
