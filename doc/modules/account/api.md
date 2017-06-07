# 公众号接口 - 账户模块

#### 1 根据siteId获取账号信息
* **协议**：HTTPS
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
            "interest": ["0", "0", "0", "0", "0", "0"]
            "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
            "name": "快站开发测试专用2",
            "serviceType": 2 //0订阅号 1历史老账号升级后的订阅号 2服务号
            "verifyType": 0 // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
        }
        ```
    * 异常情况:
        * siteId不存在或未绑定公众号，返回码 102002 
        
#### 2 根据weixinAppid获取账号信息
* **协议**：HTTPS
* **方法**：GET
* **URL**：/v1/accounts/`<weixinAppid>`
* **参数**：
    * weixinAppid 主键

* **返回**：
    * 获取成功时返回:

        ```
        {
            "weixinAppid": 601145633,
            "appId": "wx1a4ff9ec0e369bd1", // 微信后台的appid
            "appSecret": "1323fda2dfa************24332423", // 打码后的appSecret
            "headImg": "http://wx.qlogo.cn/mmopen/l0fmnePhf2dtiaEmpOZmeMrUMYBLbcQHOSYOPjFWCNdOoWUO53oawfQJA5k1DvdfK4sbX3Dn60rYI2AbOUU10thWiasCH8Q4re/0",
            "interest": ["0", "0", "0", "0", "0", "0"]
            "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
            "name": "快站开发测试专用2",
            "serviceType": 2 //0订阅号 1历史老账号升级后的订阅号 2服务号
            "verifyType": 0 // 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证
        }
        ```
    * 异常情况:
        * 账号不存在，返回码 102003 
        
  
#### 3 根据weixinAppid获取accessToken
* **协议**：HTTPS
* **方法**：PUT
* **URL**：/v1/account/<weixinAppid>/app_secret
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