# 微信服务器回调相关接口

#### 1 事件推送
* **协议**：HTTPS
* **方法**：POST
* **URL**：/public/v1/accounts/`<appId>`/events
* **参数**：
    * msg_signature 签名 url参数
    * timestamp 时间戳 url参数
    * nonce 随机字符串 url参数

* **返回**：
    返回任意字符串，例如 "success"


#### 2 ticket推送
* **协议**：HTTPS
* **方法**：POST
* **URL**：/public/v1/auth/tickets
* **参数**：
    * msg_signature 签名 url参数
    * timestamp 时间戳 url参数
    * nonce 随机字符串 url参数

* **返回**：
    返回"success"
    
    
#### 3 授权成功后跳转服务器
* **协议**：HTTPS
* **方法**：GET
* **URL**：/public/v1/bind_redirect
* **参数**：
    * userId 用户id
    * siteId siteId 非必须
    * redirectUrl 跳转url
    * auth_code 授权码

* **返回**：
    302 跳转到 redirectUrl
    

