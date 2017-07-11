# weixin_vote

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| weixin_appid | bigint(10) | - | No | - | 微信插件id，主键 | - |
| vote_id | varchar(32) | - | No | - | 投票活动id | - |
| response_json | text | - | No | - |  链接组数据内容json | - |
| qrcode_url | varchar(200) | - | No | - | 公众号二维码图片的URL | - |
| status | tinyint(1) | - | No | 1 | 状态 | 1开启，2关闭 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询扫码投票设置记录 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
