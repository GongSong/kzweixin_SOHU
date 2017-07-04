# weixin_custom_msg

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| msg_id | bigint(10) | - | No | - | 主键，客服消息id唯一标识符| - |
| custom_mass_id | bigint(10) | - | No | - | 群发客服消息任务id| - |
| app_id | varchar(45) | - | No | - | 发送者公众号app_id | - |
| open_id | varchar(45) | - | No | 0 | 粉丝对公众号唯一标识 |
| msg_type | tinyint(3) | - | No | 0 | 消息类型,1文字2微信图文3图片4链接组 | - |
| msg_json | text | - | No | - | 消息数据json | - |
| status | tinyint(1) | - | No | - | 消息状态，1已经发送2送达成功3用户拒收导致送达失败4其他原因导致送达失败(非用户拒绝) | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_CUSTOM_MASS_ID_STATUS | `custom_mass_id` ASC，`status` ASC | 用于根据客服消息群发任务ID以及状态查询客服消息记录 |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询客服消息记录 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充