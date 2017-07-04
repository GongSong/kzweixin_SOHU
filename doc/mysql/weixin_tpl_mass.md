# weixin_tpl_mass

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| tpl_mass_id | bigint(10) | - | No | - | 主键，模版消息群发唯一标识 | - |
| weixin_appid | bigint(10) | - | No | - | 主键，微信插件唯一标志 | - |
| group_id | int(10) | - | No | - | 群发消息指定的群组id，0表示全部 | - |
| tpl_id | varchar(100) | - | No | - | 模版id | - |
| tpl_title | varchar(45) | - | No | - | 模版标题 | - |
| msg_url | varchar(200) | - | No | - | 模版消息跳转的url | - |
| msg_content | text | - | No | - | 模版消息的数据 | - |
| total_count | int(10) | - | No | - | group_id下粉丝数 | - |
| success_count | int(10) | - | No | - | 发送成功数 | - |
| reject_fail_count | int(10) | - | No | - | 用户拒绝失败数 | - |
| other_fail_count | int(10) | - | No | - | 其他情况失败数 | - |
| is_timing | tinyint(1) | - | No | - | 是否定时任务（0否1是） | - |
| publish_time | int( 10 ) | - | No | - | 发布时间 | - |
| status | tinyint(1) | - | No | - | 状态 | 0 删除，1 发送成功，2 发送失败，3 正在发送，4 未发送 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_WX_APPID_CREATE_TIME | `status` ASC，`weixin_appid` ASC，`create_time` ASC | 用于根据状态，微信插件ID以及新建时间查询任务 |
| IDX_STATUS_TPL_MASS_ID | `status` ASC，`tpl_mass_id` ASC | 用于根据状态以及模版消息群发任务ID查询任务 |
| IDX_STATUS_WEIXIN_APPID_PUBLISH_TIME | `status` ASC，`weixin_appid` ASC，`publish_time` ASC | 用于根据状态，微信插件ID以及发布时间查询任务 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充