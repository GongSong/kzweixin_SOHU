# weixin_follow

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| id | int(10) | - | No | - | 主键，微信首次关注数据记录唯一标识符| - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id | - |
| response_type | int(10) | - | No | 0 | 回复类型 | 1 文章列表，2 页面列表，3 文字，4 图片 |
| response_json | text | - | No | - | 回复数据json | - |
| status | tinyint(1) | - | No | 0 | 状态 | 0 正常，1 停用 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_WX_APPID_STATUS | `weixin_appid` ASC，`status` ASC | 用于根据微信插件ID以及状态查询被添加自动回复内容 |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询被添加自动回复内容 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
