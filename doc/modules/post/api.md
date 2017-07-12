# 公众号接口 - 图文管理模块

#### 1 获取图文列表
* **协议**：HTTPS
* **方法**：GET
* **URL**：/v1/posts
* **参数**：
    * weixinAppid 公众号唯一识别 必需
    * title 按title模糊搜索 非必需
    * page 当前请求页码 必传
    * flat 是否展开多图文(1/0) 默认0

* **返回**：
    * flat为0时返回:

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
        
    * flat为1时返回
    
        ```
        {
            "totalNum": 11,
            "currentPage": 1,
            "totalPage": 3,
            "posts": [
                {
                 "pageId": 1023151612, 
                 "title": "我的买房故事",
                 "thumbUrl": "http://192.168.110.218/g1/M00/01/32/CgoYvFb4lkyAIXPGAAKXxGRjh_U4764144",
                 "updateTime": 1463542009
                }
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
         "showCoverPic": 1, // 是否显示封面图
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
* **参数**：无  
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
         "showCoverPic": 1, // 是否显示封面图
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
    * weixinAppid 公众号唯一识别
    * posts: 
    
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
         "showCoverPic": 1, // 是否显示封面图
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
    * weixinAppid 公众号唯一识别
    * posts: // 多图文列表，示例如下
    
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
         "showCoverPic": 1, // 是否显示封面图
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
        * 素材或缩略图在微信后台被删除  107007
       
#### 6 删除图文
* **协议**： HTTPS
* **方法**： DELETE
* **URL**： /v1/posts/`<pageId>`
* **参数**：
    * weixinAppid 公众号唯一识别
    
* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
    * 异常情况: 
         * 账户不存在 返回码 102002
         * 删除失败 返回码 107001
         * 参数异常，返回码 101001 
    
#### 7 从微信导入(同步微信文章)
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/post/wx_syncs
* **参数**：
    * weixinAppid 公众号唯一识别
    * uid 用户id
    

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
    * 异常情况:
        * 同步微信图文太频繁 107008
    
#### 8  快站文章导入到快站微信
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/posts/kz_imports
* **参数**：
    * weixinAppid 公众号唯一识别
    * pageIds 图文pageId的列表, 例如: `[1344344, 2323423543]`

    
* **返回**：
    * 获取成功时返回:
    
        ```
        {}
        ```

    * 异常情况: 
        * 参数异常，返回码 101001 

#### 8  快站微信同步到快站文章
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/posts/kzweixin_syncs
* **参数**：
    * weixinAppid 公众号唯一识别
    * siteId  站点id
    * categoryId  要同步到的栏目id
    * pageIds 图文pageId的列表, 例如: `[1344344, 2323423543]`
         
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
        
        
#### 9  上传素材到微信（例如封面图)
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/weixin_materials
* **参数**：
    * weixinAppid 公众号唯一识别
    * imgUrl 图片的地址
         
* **返回**：
    * 获取成功时返回:

        ```
        {
          "mediaId": "fa3fda3fdada"
        }
        ```
      
    * 异常情况: 
        * 参数异常，返回码 101001 
        

#### 10 获取图文微信url
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/posts/`<pageId>`/wx_url
* **参数**：
    * weixinAppid 公众号唯一识别
    
* **返回**：
    * 获取成功时返回:

        ```
        {
         "wxUrl": "http://mq.weixin.com/xxx"
        }
        ```
        
    * 异常情况: 
         * 图文在微信后台被删除 返回码 107009
         
         
#### 11 上传图文中的图片到微信
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/weixin_pics
* **参数**：
    * weixinAppid 公众号唯一识别
    * imgUrl 图片的地址
         
* **返回**：
    * 获取成功时返回:

        ```
        {
          "url": "http://xxxxx"
        }
        ```
      
    * 异常情况: 
        * 参数异常，返回码 101001 


#### 12 新增引导关注图文
* **协议**： HTTPS
* **方法**： POST
* **URL**： /v1/guide_follow_posts
* **参数**：
    * weixinAppid 公众号唯一识别
* **说明**: 此接口为幂等操作，如果已存在可用的引导关注图文，不会重复添加
         
* **返回**：
    * 获取成功时返回:

        ```
        {
          "url": "http://xxxxx"
        }
        ```
        
#### 13 获取引导关注图文
* **协议**： HTTPS
* **方法**： GET
* **URL**： /v1/guide_follow_post
* **参数**：
    * weixinAppid 公众号唯一识别
         
* **返回**：
    * 获取成功时返回:

        ```
        {
          "url": "http://xxxxx"
        }
        ```
      
    * 异常情况: 
        * 引导关注图文不存在，返回码107015  
