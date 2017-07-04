# weixin_push

站点相关微信模版消息推送

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| site_id | int(10) | - | No | - | 主键，站点id| - |
| push_config | varchar(100) | - | No | - | 推送配置["ec_new_order": 1, "ec_refund": 0] | - |
| status | tinyint(1) | - | No | - | 状态 1开启2关闭 | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询推送配置信息 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充