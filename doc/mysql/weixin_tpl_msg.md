# weixin_tpl_msg

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| msg_id | bigint(10) | - | No | - | 主键，微信消息id唯一标识符| - |
| tpl_mass_id | bigint(10) | - | No | - | 群发模版消息任务id| - |
| sender | varchar(45) | - | No | - | 发送者公众号app_id | - |
| receiver | varchar(100) | - | No | 0 | 接收者open_id |
| tpl_id | varchar(100) | - | No | - | 模版id | - |
| msg_url | varchar(200) | - | No | - | 模版消息跳转的url | - |
| msg_content | text | - | No | - | 模版消息的数据 | - |
| status | tinyint(1) | - | No | - | 消息状态，1已经发送2送达成功3用户拒收导致送达失败4其他原因导致送达失败(非用户拒绝) | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_CREATE_TIME | `create_time` ASC | 用于根据新建时间查询模版消息记录 |
| IDX_STATUS_TPL_MASS_ID | `status` ASC，`tpl_mass_id` ASC | 用于根据状态以及模版消息群发任务ID查询模版消息记录 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充