# weixin_msg

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| msg_id | bigint(10) | - | No | - | 主键，消息id，自增| - |
| app_id | varchar(45) | - | No | - | 对应公众号app_id | - |
| open_id | varchar(45) | - | No | - | 粉丝对公众号唯一标识 | - |
| type | tinyint(3) | - | No | 0 | 消息类型，1非关键词文字2图片3语音4视频5小视频6地理位置7链接8音乐9微信图文10外链图文11卡券12关键词文字13红包14点击菜单15模板消息| - |
| content | text | - | No | - | 消息数据json | - |
| send_type | tinyint(1) | - | No | - | 发送类型，1粉丝发送2公众号发送 | - |
| is_collect | tinyint(1) | - | No | 0 | 是否收藏，0否1是 | - |
| status | tinyint(1) | - | No | 1 | 状态，0删除1未读2已读3已回复 | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_APP_ID_TYPE_SEND_TYPE | `status` ASC，`app_id` ASC，`type` ASC，`send_type` ASC | 用于根据公众号app_id、消息类型、发送类型以及状态查询公众号消息记录 |
| IDX_STATUS_SEND_TYPE_CREATE_TIME | `status` ASC，`send_type` ASC，`create_time` ASC | 用于根据消息状态、发送类型以及创建时间查询公众号消息记录 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充