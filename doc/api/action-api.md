# 公众号接口 - 微信Action模块

> 微信Action是抽象出来的微信功能，调用方可以管理在粉丝回复、关注等两种触发条件下，微信公众号的响应行为。

#### 1 新增action
* **协议**：HTTP
* **方法**：POST
* **URL**：kzweixin/v1/actions
* **参数**：
    * accountId: 公众号appid或weixinAppid  必传
    * bizCode: 业务类型, 目前支持`"vote"` 必传
    * keyword: 回复关键字，支持正则表达式  actionType=1时必传
    * actionType: 动作类型，支持`1(用户关注), 2(用户回复)` 必传
    * responseType: 回复类型, 支持`1(文本), 2(图片), 3(链接组)` 必传
    * responseJson: 回复数据 必传
    
* **参数说明**:
    当responseType不同值时，responseJson有不同数据结构，以下为例
    * responseType == 1
    
    ```
    {
      "content": "哈哈"
    }
    ```
    
    * responseType == 2
    
    ```
    {
      "picUrl": "http://pic.kuaizhan.com/a1/c3/w3fdadfafda-fdafda"
    }
    ```
    
    * responseType == 3 
    
    ```
    {
     "news": [
       {
         "title": "新一轮投票来临",
         "description": "每天都可以投哦",
         "picUrl": "http://pic.kuaizhan.com/a1/c3/w3fdadfafda-fdafda",
         "url": "http://vote.kuaizhan.com/df23fdafda3fda"  // 用户点击链接组的跳转链接
       }
     ]
    }
    ```

* **返回**：
    * 获取成功时返回:

        ```
        {
         "id": 131
        }
        ```
        
#### 2 修改action
* **协议**：HTTP
* **方法**：PUT
* **URL**：kzweixin/v1/actions/`<actionId>`
* **参数**：
    * accountId: 公众号appid或weixinAppid  必传
    * bizCode: 业务类型, 目前支持`"vote"` 传则修改
    * keyword: 回复关键字，支持正则表达式  传则修改
    * actionType: 动作类型，支持`1(用户关注), 2(用户回复)` 传则修改
    * responseType: 回复类型, 支持`1(文本), 2(图片), 3(链接组)` 传则修改
    * responseJson: 回复数据 传则修改
    * status: 启用状态，1开启，2关闭

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
#### 3 获取action详情
* **协议**：HTTP
* **方法**：GET
* **URL**：kzweixin/v1/actions/`<actionId>`
* **参数**：无

* **返回**：
    * 获取成功时返回:

        ```
        {
         "id" : 132,
         "bizCode": "vote",
         "keyword": "投票",
         "actionType": 1,
         "responseType": 3,
         "responseJson": {
              "news": [
                {
                  "title": "新一轮投票来临",
                  "description": "每天都可以投哦",
                  "picUrl": "http://pic.kuaizhan.com/a1/c3/w3fdadfafda-fdafda",
                  "url": "http://vote.kuaizhan.com/df23fdafda3fda"  // 用户点击链接组的跳转链接
                }
              ]
         }
        }
        ```
        
#### 4 根据token换取openId
* **协议**：HTTP
* **方法**：GET
* **URL**：kzweixin/v1/action/open_id
* **参数**：
    * token 换取凭证
* **说明**：
    * 对于投票等业务，会在微信弹出图文的链接后追加三个参数，token(换取openId凭证), timestamp(token生成时间戳), expireIn(token过期时间,单位s)
    * 此接口调用一次，token即生效

* **返回**：
    * 获取成功时返回:

        ```
        {
            "openId": "odfsdefdaefefda="  // token过期或无效，此指为null
        }
        ```

