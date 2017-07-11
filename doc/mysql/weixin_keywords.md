# weixin_keywords

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| rule_id | bigint(10) | - | No | - | 主键，规则唯一标识符 | - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id | - |
| rule_name | varchar | 60 | No | '' | 规则名称 | - |
| keywords_json | text | - | No | - | 关键字及匹配方式列表 | 以键值对形式存储关键词与其匹配方式，"0"为模糊匹配，"1"为完全匹配 |
| response_type | int(10) | - | No | 0 | 回复类型 | 1 文章列表，2 页面，3 文字，4 图片 |
| response_json | text | - | No | - | 返回数据 | - |
| status | tinyint(1) | - | No | 0 | 状态 | 1 正常，2 删除 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_WX_APPID_RULE_NAME_STATUS | `weixin_appid` ASC，`rule_name` ASC，`status` ASC | 用于根据微信插件ID、规则名以及状态查询规则信息 |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询规则信息 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
