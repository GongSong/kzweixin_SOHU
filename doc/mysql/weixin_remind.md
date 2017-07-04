﻿# weixin_remind

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| weixin_appid | bigint(10) | - | No | - | 微信插件id，主键 | - |
| last_remind_time | int( 10 ) | - | No | 0 | 最近提醒时间 | - |
| status | tinyint(1) | - | No | 1 | 状态(1正常2删除) | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
