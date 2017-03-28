# 公众号接口 - 账户模块

#### 1 根据siteId获取账号信息
* **协议**：HTTPS
* **方法**：GET
* **URL**：/v1/account
* **参数**：
    * siteId 站点Id

* **返回**：
    * 获取成功时返回:

        ```
        {
            "weixinAppid": 601145633,
            "appSecret": "",
            "headImg": "http://wx.qlogo.cn/mmopen/l0fmnePhf2dtiaEmpOZmeMrUMYBLbcQHOSYOPjFWCNdOoWUO53oawfQJA5k1DvdfK4sbX3Dn60rYI2AbOUU10thWiasCH8Q4re/0",
            "interest": [
                "0",
                "0",
                "0",
                "0",
                "0",
                "0"
            ],
            "qrcode": "http://mmbiz.qpic.cn/mmbiz/UqZMrMwVpn1ulvkiaTJ2P6TRsljBSnjm9XEOZlVw08lrIYGHHe8oicoxttaNm48Kribps5ib18GPamib9GnWt92BmOg/0",
            "name": "快站开发测试专用2",
            "type": 2 //1订阅号 2服务号
        }
        ```
    * 异常情况:
        * 缺少参数weixinAppid或page，返回码 101001 
