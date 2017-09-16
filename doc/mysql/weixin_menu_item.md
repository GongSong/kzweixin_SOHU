# `weixin_auto_reply`

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| menu_key | bigint(10) | - | No | 0 | 主键，菜单key | - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id | - |
| response_type | int(10) | - | No | 0 | 返回类型 | 1 弹出文章列表，2 跳转到页面，3 弹出文字，4 弹出图片，5 跳转到外链 |
| response_json | text | - | No | - | 返回数据 | - |
| status | tinyint(1) | - | No | 0 | 状态 | 1 正常，2 删除 |
| create_time | int( 10 ) | - | No | - | 新建时间 | - |
| update_time | int( 10 ) | - | No | - | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询菜单选项信息 |
| IDX_WX_APPID_STATUS | `weixin_appid` ASC，`status` ASC | 用于根据微信插件ID以及状态查询菜单选项信息 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充