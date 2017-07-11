# weixin_tpl_config

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| weixin_appid | bigint(10) | - | No | - | 主键，微信插件唯一标志 | - |
| is_user_mp | tinyint(1) | - | No | - | 是否是自己的公众号进行业务推送1是0不是 | - |
| tpl_ids | text | - | No | - | 模版消息id列表json | - |
| status | tinyint(1) | - | No | - | 1正常2删除 | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_WX_APPID | `status` ASC，`weixin_appid` ASC | 用于根据状态以及微信插件ID查询模版消息推送配置 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充