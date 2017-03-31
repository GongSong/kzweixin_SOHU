# 公众号接口 - 图文管理模块

#### 1 获取图文列表
* **协议**：HTTPS
* **方法**：GET
* **URL**：/v1/posts
* **参数**：
    * weixinAppid 微信Appid 
    * title 按title模糊搜索
    * page 当前请求页码

* **返回**：
    * 获取成功时返回:

        ```
        {
            "totalNum": 11,
            "currentPage": 1,
            "totalPage": 3,
            "posts": [
              // posts列表的实体也是列表，表示多图文
              [
                {
                 "pageId": 1023151612, 
                 "title": "我的买房故事",
                 "thumbUrl": "http://192.168.110.218/g1/M00/01/32/CgoYvFb4lkyAIXPGAAKXxGRjh_U4764144",
                 "updateTime": 1463542009
                }
              ]
           ]
        }
        ```
    * 异常情况:
        * 缺少参数weixinAppid或page，返回码 101001 
        * 数据库异常 返回码 100001

#### 2 获取图文详情
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/posts/`<pageId>`
* **参数**：无

        
* **说明**：用于预览页面获取单个图文详情

* **返回**：
    * 获取成功时返回:

        ```
        {
         "pageId": 1023151612, 
         "meidiaId": "Mf3vdzdf3f", // 图文在微信的mediaId
         "title": "我的买房故事",
         "author": "马德华",
         "digest": "我的买房故事",
         "content" "<section>...</section>",
         "thumbUrl": "http://192.168.110.218/g1/M00/01/32/CgoYvFb4lkyAIXPGAAKXxGRjh_U4764144",
         "thumbMediaId": "fdevae3vdar3fa", // 封面图片在微信的media_id
         "contentSourceUrl": "www.sohu.com",  // 原文链接
         "updateTime": 1463542009
        }
        ```
        
    * 异常情况: 
        * 数据库异常 返回码 100001
        * mongo异常 返回码 100008

#### 3 获取多图文详情
* **协议**：HTTPS
* **方法**：GET
* **URL**：/v1/multi_posts/`<pageId>`
* **参数**：
    * weixinAppid 微信Appid
    
* **说明**: 用于图文编辑页面，一次性获取多图文所有图文详情的情况


* **返回**：
    * 获取成功时返回:

        ```
       // 多图文的详情页是一个列表
       [
        {
         "pageId": 1023151612, 
         "meidiaId": "Mf3vdzdf3f", // 图文在微信的mediaId
         "title": "我的买房故事",
         "author": "马德华",
         "digest": "我的买房故事",
         "content" "<section>...</section>",
         "thumbUrl": "http://192.168.110.218/g1/M00/01/32/CgoYvFb4lkyAIXPGAAKXxGRjh_U4764144",
         "thumbMediaId": "fdevae3vdar3fa", // 封面图片在微信的media_id
         "contentSourceUrl": "www.sohu.com",  // 原文链接
         "updateTime": 1463542009
        }
       ]
        ```
    * 异常情况: 
        * 数据库异常 返回码 100001
        * mongo异常 返回码 100008
        * 参数异常，返回码 101001 

#### 4 新建图文
* **协议**：HTTPS
* **方法**：POST
* **URL**：/v1/posts
* **参数**：
    * weixinAppid: 微信Appid
    * posts: 多图文列表，示例如下
    
    ```
       [
        {
         "title": "我的买房故事",
         "author": "马德华",
         "digest": "我的买房故事",
         "content" "<section>...</section>",
         "thumbMediaId": "fdevae3vdar3fa", // 封面图片在微信的media_id
         "thumbUrl": "http://192.168.110.218/g1/M00/01/32/CgoYvFb4lkyAIXPGAAKXxGRjh_U4764144",
         "contentSourceUrl": "www.sohu.com",  // 原文链接
        }
       ]
    ```

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
    * 异常情况: 
        * 数据库异常 返回码 100001
        * redis异常 返回码 100002
        * json解析异常 返回码 100003
        * 账户不存在 返回码 102002
        * 上传图文异常 返回码 107004
        * 参数异常，返回码 101001 

#### 5 修改图文
* **协议**：HTTPS
* **方法**：PUT
* **URL**：/v1/posts/`<pageId>`  // pageId传修改前第一篇图文的
* **参数**：
    * weixinAppid: 微信Appid
    * posts: 多图文列表，示例如下
    
    ```
       [
        {
         "pageId": 13353432e23, // 图文在后端的主键，没有则为空
         "mediaId": "Mf3vdzdf3f", // 图文在微信的mediaId
         "title": "我的买房故事",
         "author": "马德华",
         "digest": "我的买房故事",
         "content" "<section>...</section>",
         "thumbMediaId": "fdevae3vdar3fa", // 封面图片在微信的media_id
         "thumbUrl": "http://192.168.110.218/g1/M00/01/32/CgoYvFb4lkyAIXPGAAKXxGRjh_U4764144",
         "contentSourceUrl": "www.sohu.com",  // 原文链接
        }
       ]
    ```

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
    * 异常情况: 
        * 数据库异常 返回码 100001
        * redis异常 返回码 100002
        * json解析异常 返回码 100003
        * 账户不存在 返回码 102002
        * 上传图文异常 返回码 107004
        * 参数异常，返回码 101001 
       
#### 6 删除图文
* **协议**： HTTPS
* **方法**： DELETE
* **URL**： /v1/posts/`<pageId>`
* **参数**：
    * weixinAppid 微信Appid
    
* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
    * 异常情况: 
         * 数据库异常 返回码 100001
         * redis异常 返回码 100002
         * json解析异常 返回码 100003
         * 账户不存在 返回码 102002
         * 删除失败 返回码 107001
         * 参数异常，返回码 101001 
    
#### 7 从微信导入(同步微信文章)
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/post/wx_syncs
* **参数**：
    * weixinAppid 微信Appid
    

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
    * 异常情况: 无
    
#### 8  快站文章同步到微信
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/posts/kz_syncs
* **参数**：
    * weixinAppid 微信Appid
    * posts:示例如下  
    
        ```
           {
               "pageIds": [
                   312321312,
                   32131232131
               ]
           }
        ```
    
* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
    * 异常情况: 
        * 参数异常，返回码 101001 

#### 8  快站微信本地文章同步到快站文章
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/posts/kzweixin_syncs
* **参数**：
    * weixinAppid 微信Appid
    * posts:示例如下  
    
        ```
       [
           {
               "siteId": 12312312,
               "pageId": 312312321,
               "categoryId": 312312321
           },
           {
               "siteId": 12312312,
               "pageId": 312312321,
               "categoryId": 312312321
           }
       ]
        ```
    
* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
    * 异常情况: 
        * 参数异常，返回码 101001 
        * 同步快站文章失败，返回码 107005 
        * 数据库异常 返回码 100001
        * mongo异常 返回码 100008