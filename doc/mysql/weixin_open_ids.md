# weixin_open_ids

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| id | int(10) | - | No | - | 主键，用户关注公众号数据记录唯一标识符 | - |
| app_id | varchar(45) | - | No | - | 微信公众平台开发模式提供的appid用于访问接口 | - |
| open_id | varchar(45) | - | No | - | 用户对应这个公众号的open_id | - |
| status | tinyint(1) | - | No | 1 | 状态 | 1关注，2未关注 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_CREATE_TIME_UPDATE_TIME | `status` ASC，`create_time` ASC，`update_time` ASC | 用于根据状态、创建时间以及更新时间查询用户关注公众号记录 |
| IDX_APP_ID_OPEN_ID | `app_id` ASC，`open_id` ASC | 用于根据微信公众平台开发模式提供的appid以及用户对应这个公众号的open_id查询用户关注公众号记录 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
