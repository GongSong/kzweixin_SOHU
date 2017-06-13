package com.kuaizhan.kzweixin.common;

import com.kuaizhan.kzweixin.config.ApplicationConfig;
import com.kuaizhan.kzweixin.utils.HttpClientUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by zixiong on 2017/4/23.
 */
public class ConsulRegister {
    
    private static final Logger logger = LoggerFactory.getLogger(ConsulRegister.class);

    // 是否已注册、已注销
    private static Boolean registered = false;
    private static Boolean deregister = false;
    // 服务的地址和端口
    private String address;
    private int port;


    // consul 服务name
    private String serviceName = "kzweixin-" + ApplicationConfig.ENV_ALIAS;
    // consul 服务id
    private String serviceId;

    /** 判断是否需要注册 **/
    private Boolean shouldRegister() {
        return Stream.of("dev", "test", "pre", "production").anyMatch(ApplicationConfig.ENV_ALIAS::equals);
    }

    /*** 从命令"hostname -i"中读取ip ***/
    private String getAddress() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("hostname", "-i");
        Process proc = pb.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        StringBuilder addressBuilder = new StringBuilder();
        String s;
        while((s = stdInput.readLine()) != null) {
            addressBuilder.append(s);
        }

        String address = addressBuilder.toString();
        logger.info("[Consul] get address:{}", address);
        if ("".equals(address)) {
            throw new IOException("[Consul] address is not initialized");
        }
        return address;
    }


    /*** 从jetty配置文件读取端口 ***/
    private int getPort() throws IOException {
        Map<String, String> envs = System.getenv();
        int port = Integer.parseInt(envs.getOrDefault("JETTY_PORT", "0"));
        if (port == 0) {
            throw new IOException("[Consul] can not get port");
        }
        logger.info("[Consul] get port:{}", port);
        return port;
    }


    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!registered && shouldRegister()) {
            logger.info("[Consul] start service register");

            try {
                address = getAddress();
                port = getPort();
            } catch (Exception e) {
                // dev环境允许注册失败，其他环境报错
                if (!"dev".equals(ApplicationConfig.ENV_ALIAS)) {
                    logger.error("[Consul] register failed", e);
                }
                return;
            }

            serviceId = serviceName + ":" + address + ":" + port;
            logger.info("[Consul] serviceId:{}", serviceId);

            JSONObject paramJson = new JSONObject();
            paramJson.put("ID", serviceId);
            paramJson.put("Name", serviceName);
            paramJson.put("Address", address);
            paramJson.put("Port", port);
            paramJson.put("EnableTagOverride", false);

            JSONObject checkJson = new JSONObject();
            checkJson.put("DeregisterCriticalServiceAfter", "1m");
            checkJson.put("HTTP", "http://" + address + ":" + port + "/kzweixin/v1/common/ping");
            checkJson.put("Interval", "5s");

            paramJson.put("Check", checkJson);
            HttpClientUtil.putJson("http://127.0.0.1:8500/v1/agent/service/register", paramJson.toString());
            logger.info("[Consul] register succeed");

            registered = true;
        }
    }

    @EventListener
    public void onApplicationEvent(ContextClosedEvent event){
        // 确保只执行一次, 有ApplicationContext和DispatcherServlet两个context需要关闭。
        if (!deregister && serviceId != null && shouldRegister()) {
            logger.info("[Consul] start deregister service, serviceId:{}", serviceId);
            HttpClientUtil.putJson("http://127.0.0.1:8500/v1/agent/service/deregister/" + serviceId, null);
            logger.info("[Consul] deregister succeed");

            deregister = true;
        }
    }
}
