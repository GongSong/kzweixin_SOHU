# 公众号接口 - 账户模块

#### 1 根据siteId获取账号信息
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/accounts/site/`<siteId>`
* **参数**： 无

* **返回**：
    * 获取成功时返回:

        ```
        {
            "weixinAppid": 601145633, 
            "appId": "wx1a4ff9ec0e369bd1", // 微信后台的appid
            "appSecret": "1323fda2dfa************24332423", // 打码后的appSecret
            "headImg": "http://wx.qlogo.cn/mmopen/l0fmnePhf2dtiaEmpOZmeMrUMYBLbcQHOSYOPjFWCNdOoWUO53oawfQJA5k1DvdfK4sbX3Dn60rYI2AbOUU10thWiasCH8Q4re/0",
            "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
            "name": "快站开发测试专用2",
            "serviceType": 2 //0订阅号 1历史老账号升级后的订阅号 2服务号
            "verifyType": 0 // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
        }
        ```
    * 异常情况:
        * siteId不存在或未绑定公众号，返回码 102002 
        
#### 2 获取公众号详情
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/accounts/`<accountId>`
* **参数**：
    * accountId 公众号的appid或者weixinAppid

* **返回**：
    * 获取成功时返回:

        ```
        {
            "weixinAppid": 601145633,
            "appId": "wx1a4ff9ec0e369bd1", // 微信后台的appid
            "appSecret": "1323fda2dfa************24332423", // 打码后的appSecret
            "headImg": "http://wx.qlogo.cn/mmopen/l0fmnePhf2dtiaEmpOZmeMrUMYBLbcQHOSYOPjFWCNdOoWUO53oawfQJA5k1DvdfK4sbX3Dn60rYI2AbOUU10thWiasCH8Q4re/0",
            "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
            "name": "快站开发测试专用2",
            "serviceType": 2 //0订阅号 1历史老账号升级后的订阅号 2服务号
            "verifyType": 0 // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
        }
        ```
    * 异常情况:
        * 账号不存在，返回码 102003 
        
  
#### 3 修改公众号的app_secret
* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/accounts/`<weixinAppid>`/app_secret
* **参数**：
    * weixinAppid: 主键
    * appSecret: 用户填写提交的App Secret

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
    * 异常情况:
        * IP未设置白名单，返回码 102006
        * appSecret不正确，返回码 102007
        
#### 4 获取公众号账号设置

* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/accounts/`<accountId>`/settings
* **参数**：
    * accountId 公众号的appid或者weixinAppid

* **返回**：
    * 获取成功时返回:

        ```
        {
          "openLogin": true, // 是否开启授权登录
          "openShare": false, // 是否开启自定义分享
          "interest": ["0", "0", "0", "0", "0", "0"], // 智能回复开关，六个值分别代表新闻、笑话、天气、听歌，中英文翻译、股票
        }
        ```
    * 异常情况:
        * 账号不存在，返回码 102003 

#### 5 修改用户自定义分享开启／关闭状态

* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/accounts/`<weixinAppid>`/customize_share
* **参数**：
    * weixinAppid 主键
    * openShare 用户选择切换的自定义分享状态

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
 
#### 6 修改服务号授权登录开启／关闭状态

* **协议**：HTTP
* **方法**：PUT
* **URL**：/v1/accounts/`<weixinAppid>`/authorize_login
* **参数**：
    * weixinAppid 主键
    * openLogin 用户选择切换的授权登录状态

* **返回**：
    * 获取成功时返回:

        ```
        {}
        ```
        
#### 7 获取绑定url

* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/account/bind_url
* **参数**：
    * userId 账号userId，必需
    * siteId 公众号绑定到的站点Id， 非必需
    * redirectUrl 绑定完成后的跳转地址， 需包含协议头，需进行url encode

* **返回**：
    * 获取成功时返回:

        ```
        {
          "url": "HTTP://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=wx54effaa795c75b4f&pre_auth_code=preauthcode@@@1LhymUZZU7G5s4mXPdRA1lsn5Sf5DdIgc6eSdTEAgzxBxQROZSd3FP3B11T3SPRE&redirect_uri=http%3A%2F%2F94dbabe2de.kzsite09.cn%3A8080%2Fv1%2Faccounts%2Ftmp%3FuserId%3D3"
        }
        ```

#### 8 绑定新用户

* **协议**：HTTP
* **方法**：POST
* **URL**：/v1/account/binds
* **参数**：
    * userId 账号userId，必需
    * siteId 公众号绑定到的站点Id， 非必需
    * redirectUrl 绑定完成后的跳转地址， 需包含协议头，需进行url encode

* **返回**：
    * 获取成功时302跳转
        
#### 9 获取用户的所有公众号
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/accounts
* **参数**： 
    * userId 账号userId，必需

* **返回**：
    * 获取成功时返回:

        ```
        "accounts": [
             {
                "weixinAppid": 601145633, // 主键
                "appId": "wx1a4ff9ec0e369bd1", // 微信后台的appid
                "headImg": "http://wx.qlogo.cn/mmopen/l0fmnePhf2dtiaEmpOZmeMrUMYBLbcQHOSYOPjFWCNdOoWUO53oawfQJA5k1DvdfK4sbX3Dn60rYI2AbOUU10thWiasCH8Q4re/0",
                "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
                "name": "快站开发测试专用2",
                "serviceType": 2 //0订阅号 1历史老账号升级后的订阅号 2服务号
                "verifyType": 0 // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
            }
         ]
        ```
    * 异常情况:
        * siteId不存在或未绑定公众号，返回码 102002 
        
        
#### 10 列表页获取用户的所有公众号
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/account_list
* **参数**： 
    * userId 账号userId，必需
    * offset 分页偏移量, 默认0
    * limit 每页的数目, 默认5

* **返回**：
    * 获取成功时返回:

        ```
        "total": 12, // 总共的条数
        "authorizedCount": 3, // 已授权的公众号数
        "accounts": [
             {
                "isAuthorized": true,  // 是否是授权状态
                "weixinAppid": 601145633, // 主键
                "appId": "wx1a4ff9ec0e369bd1", // 微信后台的appid
                "headImg": "http://wx.qlogo.cn/mmopen/l0fmnePhf2dtiaEmpOZmeMrUMYBLbcQHOSYOPjFWCNdOoWUO53oawfQJA5k1DvdfK4sbX3Dn60rYI2AbOUU10thWiasCH8Q4re/0",
                "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
                "name": "快站开发测试专用2",
                "serviceType": 2 //0订阅号 1历史老账号升级后的订阅号 2服务号
                "verifyType": 0 // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
                "newMsgCount": 100, // 今日消息数
                "newUserCount": 1000, // 今日用户数
                "userCount": 1003423, // 总用户数
            }
         ]
        ```

#### 11 获取公众号access_token
* **协议**：HTTP
* **方法**：GET
* **URL**：/v1/accounts/`<accountId>`/access_token
* **参数**： 
    * accountId weixinAppid或者appId
    

* **返回**：
    * 获取成功时返回:

        ```
        {
            "accessToken": "fda3fdavdw3fdafv3rdfadfa3fdadfa=="
        }
        ```
