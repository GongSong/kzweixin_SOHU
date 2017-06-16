# 微信服务器回调相关接口

#### 1 事件推送
* **协议**：HTTPS
* **方法**：POST
* **URL**：/v1/app_ids/`<appId>`/events
* **参数**：
    * msgSignature 签名
    * timestamp 时间戳
    * nonce 随机字符串
    * postBody 微信服务器http请求body的内容

* **返回**：
    返回任意字符串，例如 "success"
