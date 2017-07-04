# weixin_qrcode

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| qrcode_id | bigint(10) | - | No | - | 二维码唯一标识符，主键 | - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id | - |
| qrcode_name | varchar(60) | - | No | - | 二维码名称 | - |
| scene_id | bigint(10) | - | No | - | 二维码场景ID | - |
| ticket | varchar(128) | - | No | - | 向微信获取二维码图片的凭据 | - |
| response_type | int(10) | - | No | 0 | 1文章列表2页面3文字4图片5链接组 | - |
| response_json | text | - | No | - | 返回数据 | - |
| status | tinyint(1) | - | No | 1 | 状态(1正常2删除) | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_WEIXIN_APPID_SCENE_ID_STATUS | `weixin_appid` ASC，`scene_id` ASC，`status` ASC | 用于根据微信插件ID、场景值以及状态查询二维码数据 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
