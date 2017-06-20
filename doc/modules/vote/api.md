 公众号接口 - 扫码投票文档

#### 1 修改扫码投票配置
* **协议**：HTTPS
* **方法**：put
* **URL**：/v1/vote/vote_setting
* **参数**：
    * weixinAppid 公众号唯一识别 必需
    * status 开光状态true/false 必需
    * followReply 是否关注自动回复 bool类型 必需
    * keyword 投票关键字 必需
    * postTitle 图文标题 必需
    * postCover 图文封面图 必需
    * postDigest 图文摘要 必需
    * postUrl 点击图文跳转链接 必需

* **成功时返回**：

        ```
        {}
        ```
        
 
#### 2 获取扫码投票配置
* **协议**：HTTPS
* **方法**：GET
* **URL**：/v1/vote/vote_setting
* **参数**：
    * weixinAppid 公众号唯一识别 必需

* **成功时返回**：

        ```
        {
          // 没有setting则不存在配置
          "setting": {
             "status": true,
             "followReply": true,
             "keyword": "投票",
             "postTitle": "快来投票吧",
             "postCover": "http://pic.kuaizhan.com/fda3fda",
             "postDigest": "手快有，手慢无",
             "postUrl": "xxxxxxx"
          }
        }
        ```
