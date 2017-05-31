# 公众号接口 - 消息模块

#### 1 获取消息列表
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/msg/msgs
* **参数**：  
        * weixinAppid  
        * page：要获取的页数  
        * queryStr: 按内容模糊查询  
        * filterKeywords: 是否过滤关键词消息(1或0)

* **返回**：
    * 获取成功时返回:

        ```
        {
            "totalNum": 370,
            "currentPage": 1,
            "totalPage": 19,
            "msgs": [
              {
                "msgType": 1, // 消息类型，可能值为1: 文本消息 2:图片消息 10: 链接组消息 12: 关键词消息, 以及其他前端不需要处理的类型   
                "sendType": 1, //  1为粉丝发给公众号  2位公众号发给粉丝
                "nickname": "子雄",
                "headImgUrl": "http://wx.qlogo.cn/mmopen/PiajxS/0",  // 头像
                "openId": "oBGGJt5SLU9P_wGu71Xo82m_Zq1s",
                "content": {
                  "content": "预览"
                },
                "createTime": 1493005476
              }
             ]
        }
        ```
        
    * 返回数据说明
    
        * msgType为1和12时, content示例 `{"content": "你说什么"}`
        * msgType为2时，content示例 `{"pic_url": "http://pic.kuaizhan.com/fdae3fdaf"}`
        * msgType为10时, content示例 `{"articles":[{"title":"我是链接组","description":"","picurl":"//pic.kuaizhan.com00x500","url":"http:www.kuaizhan.com/club/apiv1/sites/4142239921/me/notices/jump-to"}]}`
        * 其他类型时, content示例 `{"content": "[收到暂不支持的消息类型，请在微信公众平台上查看]"}`
            
   
#### 2 获取未读消息总数
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/msg/unread_msg_count
* **参数**：  
        * weixinAppid  
        
* **返回**：
    * 获取成功时返回:

        ```
        {
            "count": 12
        }
        ```
        
#### 3 标记所有消息为已读
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/msg/msg_reads
* **参数**：  
        * weixinAppid  
        
        
* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
    
        
#### 4 获取和粉丝的聊天记录
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/msg/chat_list
* **参数**：  
        * weixinAppid  
        * page：要获取的页数  
        * openId: 粉丝的openId
        
* **返回**：
    * 获取成功时返回:

        ```
        {
            "totalNum": 370,
            "currentPage": 1,
            "totalPage": 19,
            "msgs": [
              {
                "msgType": 1,
                "sendType": 1,
                "nickname": "子雄",
                "headImgUrl": "http://wx.qlogo.cn/mmopen/PiajxS/0",  // 头像
                "openId": "oBGGJt5SLU9P_wGu71Xo82m_Zq1s",
                "content": {
                  "content": "预览"
                },
                "createTime": 1493005476
              }
             ]
        }
        ```
   
#### 5 发送(客服)消息
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/msg/msgs
* **参数**：  
        * weixinAppid  
        * openId: 粉丝的openId  
        * msgType: 消息类型, 可选值为1: 文本消息 2:图片消息 9: 多图文消息 10: 链接组消息   
        * content: 消息数据    
        
            * msgType为1时, content示例 `{"content": "你说什么"}`
            * msgType为2时，content示例 `{"pic_url": "http://pic.kuaizhan.com/fdae3fdaf"}`
            * msgType为9时, content示例 `{"posts":[6939030655aa1, 12445342342]}`
            * msgType为10时, content示例 `{"articles":[{"title":"我是链接组","description":"","picurl":"//pic.kuaizhan.com00x500","url":"http:www.kuaizhan.com/club/apiv1/sites/4142239921/me/notices/jump-to"}]}`
        
        
* **返回**：
    * 成功时返回:

        ```
        {}
        ```


#### 6 获取快捷回复配置
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/msg/quick_replies
* **参数**：  
        * weixinAppid  
        
        
* **返回**：
    * 成功时返回:

        ```
        {
            "quickReplies" ["多谢您的惠顾", "您好"]
        }
        ```
        

#### 7 修改快捷回复配置
* **协议**： HTTPS
* **方法**： PUT
* **URL**： /v1/msg/quick_replies
* **参数**：  
        * weixinAppid  
        * quickReplies: 快捷回复配置，示例 `["多谢您的惠顾", "您好"]`
        
        
* **返回**：
    * 成功时返回:

        ```
        {}
        ```
