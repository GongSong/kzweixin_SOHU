# site_weixin
	
## 属性描述

| name | type | length | null or not | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| weixin_appid | bigint(10) | - | No | - | 微信插件ID，主键 | - |
| site_id | bigint(10) | - | No | - | 站点ID | - |
| access_token | varchar(512) | - | No | - | 访问接口令牌 | - |
| refresh_token | varchar(512) | - | No | - | 刷新令牌 | - |
| expires_time | int(10) | - | No | - | 令牌失效时间 | - |
| app_id | varchar(45) | - | No | - | 微信公众平台开发模式提供的appid用于访问接口 | - |
| app_secret | varchar(45) | - | No | - | 微信公众平台开发模式提供的app_secret用于访问接口 for old bind | - |
| func_info_json | text | - | No | - | 公众号授权给开发者的权限集列表1-13 | - |
| nick_name | varchar(45) | - | No | - | 授权方昵称 | - |
| head_img | varchar(512) | - | No | - | 授权方头像 | - |
| service_type | tinyint(1) | - | No | - | 授权方公众号类型，0代表订阅号，1代表由历史老帐号升级后的订阅号，2代表服务号 | - |
| verify_type | int(10) | - | No | - | 授权方认证类型，-1代表未认证，0代表微信认证，1代表新浪微博认证，2代表腾讯微博认证，3代表已资质认证通过但还未通过名称认证，4代表已资质认证通过、还未通过名称认证，但通过了新浪微博认证，5代表已资质认证通过、还未通过名称认证，但通过了腾讯微博认证 | - |
| user_name | varchar(45) | - | No | - | 授权方公众号的原始ID | - |
| alias | varchar(45)) | - | No | - | 授权方公众号所设置的微信号，可能为空 | - |
| business_info_json | text | - | No | - | 用以了解以下功能的开通状况（0代表未开通，1代表已开通）：open_store:是否开通微信门店功能，open_scan:是否开通微信扫商品功能，open_pay:是否开通微信支付功能，open_card:是否开通微信卡券功能，open_shake:是否开通微信摇一摇功能 | - |
| qrcode_url | varchar(200) | - | No | - | 二维码图片的URL | - |
| qrcode_url_kz | varchar(200) | - | No | - | 二维码图片的快站URL | - |
| menu_json | text | - | No | - | 菜单列表 | - |
| interest_json | text | - | No | - | 开关列表,四个值分别代表新闻、笑话、天气、听歌，0为关，1为开 | - |
| advanced_func_info_json | varchar(256) | - | No | - | 高级功能开通情况(0未开通1已开通):open_login服务号授权登录,open_share微信自定义分享 | - |
| preview_open_id | varchar(45) | - | No | - | 群发预览指定的用户open_id | - |
| bind_time | int(10) | - | No | - | 绑定时间 | - |
| unbind_time | int(10) | - | No | - | 解除绑定时间 | - |
| is_del | tinyint(1) | - | No | - | 是否删除(1已删除0未删除) | - |
| create_time | int(10) | - | No | - | 新建时间 | - |
| update_time | int(10) | - | No | - | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_SITE_ID_APP_ID_IS_DEL | `site_id` ASC，`app_id` ASC，`is_del` ASC | 用于根据站点ID、微信ID以及是否删除查询公众号信息 |
| IDX_APP_ID_IS_DEL | `app_id` ASC，`is_del` ASC | 用于根据微信ID以及是否删除查询公众号信息 |
| IDX_IS_DEL_BIND_TIME | `is_del` ASC，`bind_time` ASC | 用于根据是否删除以及绑定时间查询公众号信息 |
| IDX_IS_DEL_SERVICE_TYPE_VERIFY_TYPE | `is_del` ASC，`service_type` ASC，`verify_type` ASC | 用于根据是否删除、公众号类型以及认证类型查询公众号信息 |
| IDX_IS_DEL_CREATE_TIME | `is_del` ASC，`create_time` ASC | 用于根据是否删除以及创建时间查询公众号信息 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存信息暂无！
