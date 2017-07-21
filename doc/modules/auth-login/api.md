# 公众号接口 - 授权登录模块

#### 1 跳转到微信授权登录页面的url
* **协议**：HTTP
* **方法**：GET
* **URL**：kzweixin/public/v1/authorize_login
* **参数**：
    * appid: 公众号appid 必传
    * redirectUrl: 授权完成后的跳转url 必传
    * scope: 授权类型，可选值snsapi_base(静默授权)、snsapi_userinfo(非静默授权)
    
* **返回**:
    重定向到redirectUrl, 并携带以下url参数
    * status, 1为授权成功，0为授权失败
    * token, 换取用户信息的凭证, status为1时有此值
    
    
#### 2 根据token换取授权登录用户信息
* **协议**：HTTP
* **方法**：GET
* **URL**：kzweixin/v1/authorize_user_info
* **参数**：
    * token: 凭证


* **返回**：
    * 获取成功时返回:

        ```
        {
          "appid": "wx1a4ff9ec0e369bd1",
          "userInfo": {
            "appid": "wx1a4ff9ec0e369bd1",
            "openid": "oBGGJt5SLU9P_wGu71Xo82m_Zq1s",
            "unionid": "ol1kfuFaEf0-4GmXXz5BvswBIhys",
            "nickname": "子雄",
            "sex": 1,
            "province": "湖北",
            "city": "武汉",
            "country": "中国",
            "headimgurl": "http://wx.qlogo.cn/mmhead/ucIq3XfFAVCNsy5bsUNrZibEzPQicgtOrpPWbchAJ7M7M/0"
          }
        }
        ```