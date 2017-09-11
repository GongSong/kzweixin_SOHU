# 公众号接口 - 自动回复模块

#### 1 创建新规则
* **协议**：HTTP
* **方法**：POST
* **URL**：/v1/autoreply/keyword_replies
* **参数**：
    * weixinAppid   Long    主键，必传 
    * ruleName      String  新建标签名，必传 
    * keywords      List    对象为String keyword，String type，必传 
    * responseType  String  枚举回复类型, 支持`POST(图文), TEXT(文本), IMAGE(图片), LINK_GROUP(链接组)`，必传 
    * responseJson  Map     回复数据，必传    

* **参数说明**:
    当responseType不同值时，responseJson有不同数据结构，以下为例
    * responseType ==  "POST"
    ```
    {
        "mediaIds": [
                        "kWaw2cfOFVT5E0ds4onzp3yD3pSVvR4ohG-1l6zVrxhEMddZHJMZsbfSJd08cPl1",
                        "Rvx0mZfEZ3_nFkms33dFF7rMESaZ8WMZzQo_9Aqhn4k"
                    ]
    }
    ```
    
    * responseType == "TEXT"
    
    ```
    {
      "content": "哈哈"
    }
    ```
    
    * responseType == "IMAGE"
    
    ```
    {
      "picUrl": "http://pic.kuaizhan.com/a1/c3/w3fdadfafda-fdafda"
    }
    ```
    
    * responseType ==  "LINK_GROUP"
    
    ```
    {
     "news": [
       {
         "title": "新一轮投票来临",
         "description": "每天都可以投哦",
         "picUrl": "http://pic.kuaizhan.com/a1/c3/w3fdadfafda-fdafda",
         "url": "http://vote.kuaizhan.com/df23fdafda3fda"  // 用户点击链接组的跳转链接
         "linkType": "URL"
       }
     ]
    }
    ```
    description为选传字段，其余为必传
    
* **返回**：
    * 获取成功时返回:

        ```
        {
            "ruleId": 9990221094,  //快站服务器分配的自增规则id
            "ruleName": "链接组测试"  //创建成功的新规则名
        }
        ```
   
#### 2 获取规则列表
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/autoreply/keyword_replies
* **参数**：
    * weixinAppid   Long    主键，必传 
    * query         String  查询规则名和关键字的搜索语句，可为空
      
* **返回**：
    * 获取成功时返回:

        ```
        {
            "ruleId": 9990221094,
            "weixinAppid": 8111772986,
            "ruleName": "链接组测试",
            "responseType": "LINK_GROUP",
            "keywords": [
                {
                    "keyword": "啊哈哈",
                    "type": "0"
                },
                {
                    "keyword": "ddd",
                    "type": "1"
                },
                {
                    "keyword": "555",
                    "type": "0"
                }
            ],
            "responseJson": {
                "linkGroups": [
                    {
                        "title": "hahaha",
                        "description": "链接测试",
                        "picUrl": "http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png",
                        "url": "http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png",
                        "linkType": "URL"
                    }
                ]
            }
        }
        ```

#### 3 更新规则
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/autoreply/keyword_replies/{ruleId}
* **参数**：
    * ruleId        Long    规则Id，必传
    * weixinAppid   Long    主键，必传 
    * ruleName      String  新建标签名，必传 
    * keywords      List    对象为String keyword，String type，必传 
    * responseType  String  枚举回复类型, 支持`POST(图文), TEXT(文本), IMAGE(图片), LINK_GROUP(链接组)`，必传 
    * responseJson  Map     回复数据，必传
    
* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
#### 4 删除规则
* **协议**：HTTP
* **方法**：DELETE
* **URL**：/v1/autoreply/keyword_replies/{ruleId}
* **参数**：
    * ruleId        Long    规则Id，必传
    * weixinAppid   Long    主键，必传         

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
#### 5 创建/更新被关注自动回复
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/autoreply/follow_reply
* **参数**：
    * weixinAppid   Long    主键，必传 
    * responseType  String  枚举回复类型, 支持`POST(图文), TEXT(文本), IMAGE(图片), LINK_GROUP(链接组)`，必传 
    * responseJson  Map     回复数据，必传 

* **返回**：
    * 获取成功时返回:

        ```
        {
            "followReplyId": 82  //快站服务器分配的自增回复id
        }
        ```
        
#### 6 获取被关注自动回复内容
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/autoreply/follow_reply
* **参数**：
    * weixinAppid   Long    主键，必传 
   
* **返回**：
    * 获取成功时返回:    
    
        ```
            {
                "id": 82,
                "weixinAppid": 8111772986,
                "responseType": "LINK_GROUP",
                "responseJson": {
                    "linkGroups": [
                        {
                            "title": "hahah",
                            "description": "链接测试",
                            "picUrl": "http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png",
                            "url": "http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png",
                            "linkType": "URL"
                        }
                    ]
            }
        ```
        
#### 7 删除被关注自动回复
* **协议**：HTTP
* **方法**：DELETE
* **URL**：/v1/autoreply/follow_reply
* **参数**：
    * weixinAppid   Long    主键，必传         

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```

#### 8 创建/更新消息自动回复
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/autoreply/msg_reply
* **参数**：
    * weixinAppid   Long    主键，必传 
    * responseType  String  枚举回复类型, 支持`POST(图文), TEXT(文本), IMAGE(图片), LINK_GROUP(链接组)`，必传 
    * responseJson  Map     回复数据，必传 

* **返回**：
    * 获取成功时返回:

        ```
        {
            "msgReplyId": 82  //快站服务器分配的自增回复id
        }
        ```
        
#### 9 获取消息自动回复内容
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/autoreply/msg_reply
* **参数**：
    * weixinAppid   Long    主键，必传 
   
* **返回**：
    * 获取成功时返回:    
    
        ```
            {
                "id": 82,
                "weixinAppid": 8111772986,
                "responseType": "LINK_GROUP",
                "responseJson": {
                    "linkGroups": [
                        {
                            "title": "hahah",
                            "description": "链接测试",
                            "picUrl": "http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png",
                            "url": "http://s0.dev.com/res/weixin/images/post-default-cover-900-500.png",
                            "linkType": "URL"
                        }
                    ]
            }
        ```
        
#### 10 删除消息自动回复
* **协议**：HTTP
* **方法**：DELETE
* **URL**：/v1/autoreply/msg_reply
* **参数**：
    * weixinAppid   Long    主键，必传         

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```