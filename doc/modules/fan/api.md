# 公众号接口 - 粉丝管理模块

#### 1 创建新粉丝标签
* **协议**：HTTP
* **方法**：POST
* **URL**：/v1/fan/tags
* **参数**：
    * weixinAppid 主键
    * tagName 新建标签名 

* **返回**：
    * 获取成功时返回:

        ```
        {
            "id": 184,  //微信服务器分配的标签id
            "name": "广东"  //创建成功的新标签名
        }
        ```
    * 异常情况：
        * 新标签和已有标签重名，返回码 104001
        * 标签名长度超过30个字节，返回码 104002
        * 创建的标签数过多，超过100个，返回码 104003
        
#### 2 获取已创建的标签
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/fan/tags
* **参数**：
    * weixinAppid 主键

* **返回**：
    * 获取成功时返回:

        ```
        {
            "id": 2,  //标签id
            "name": "星标组",  //标签名
            "count": 6  //该标签下拥有的粉丝数
        },
        {
            "id": 145,
            "name": "test",
            "count": 1
        },
        ......
        
        ```
        
#### 3 重命名标签
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/fan/tag/`{tagId}`
* **参数**：
    * tagId 要修改的标签id
    * weixinAppid 主键
    * newTag 新标签名

* **返回**：
    * 获取成功时返回:
       
        ```
        {}
        ```
    * 异常情况：
        * 新标签和已有标签重名，返回码 104001
        * 标签名长度超过30个字节，返回码 104002
        * 修改默认保留标签，返回码 104004
        
#### 4 删除标签
* **协议**：HTTP
* **方法**：DELETE
* **URL**：/v1/fan/tag/`{tagId}`
* **参数**：
    * tagId 要删除的标签id
    * weixinAppid 主键

* **返回**：
    * 获取成功时返回:
       
        ```
        {}
        ```
    * 异常情况：
        * 修改默认保留标签，返回码 104004 
        * 删除超过10万粉丝的标签，返回码 104005
        
#### 5 给粉丝贴标签
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/fan/fan_tags
* **参数**：
    * weixinAppid 主键
    * fansOpenId 要贴标签的粉丝openId
    * newTagsId 增加的标签id
    * deleteTagsId 删除的标签id

* **返回**：
    * 获取成功时返回:
       
        ```
        {}
        ```
    * 异常情况：
        * 传入粉丝openId超过50个，返回码 104006 
        * 非法标签，返回码 104007
        * 粉丝标签超过20个，返回码 104008
        * openId不属于此公众号，返回码 104009
        * 非法的openId或用户未关注公众号，返回码 108006
        
#### 6 按标签搜索粉丝
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/fan/fans
* **参数**：
    * weixinAppid 主键
    * pageNum 当前页码
    * tagIds 要查找的标签id
    * queryStr 要查找的昵称字符串
    * isBlacklist 是否在黑名单里查找

* **返回**：
    * 获取成功时返回:
       
        ```
         "totalNum": 2,
                "currentPage": 1,
                "totalPage": 1,
                "fans": [
                    {
                        "id": 4337,
                        "name": "Fe.y.z",
                        "headImgUrl": "http://wx.qlogo.cn/mmopen/1XRNasCU4H4YUNfib1b8rpK0L61c3sU7iaQZ7y9IcRpNWlD5ctNbzMKKWNIRqyxKo0dMmVaoqBat4Cyvum3KLbrzL2XvPrJibdp/0",
                        "sex": 1,
                        "openId": "oBGGJt1hO7E-HVpveyJNRpW3xc9Q",
                        "address": "中国 四川",
                        "focusTime": 1490091579,
                        "tagIds": [
                            199,
                            201,
                            202
                        ]
                    },
                    {
                        ......
                    },
                    ......
                ]
        ```

#### 7 更新黑名单用户信息
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/fan/blacklist
* **参数**：
    * weixinAppid 主键
    * openIds 要操作的粉丝openId
    * setBlacklist 是否设置为黑名单用户，true拉黑false解除拉黑

* **返回**：
    * 获取成功时返回:
       
       ```
       {}
       ```   
    * 异常情况：
        * 非法的openId或用户未关注公众号，返回码 108006
        * openId不属于此公众号，返回码 104009
        * 一次只能拉黑/取消拉黑20个用户，返回码 104010
                    
                    
#### 8 判断粉丝是否关注
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/fans/`<open_id>`/is_subscribe
* **参数**：
    * appId 哪个公众号

* **返回**：
    * 获取成功时返回:
       
       ```
       {
         "isSubscribe": false
       }
       ```   
     
#### 9 根据粉丝信息判断是否关注
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/fan/is_subscribe
* **参数**：
    * appId 哪个公众号
    * nickName 粉丝昵称
    * headImgUrl 粉丝头像url

* **返回**：
    * 获取成功时返回:
       
       ```
       {
         "isSubscribe": false
       }
       ```   
