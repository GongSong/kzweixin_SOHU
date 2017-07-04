# weixin_attract_fans_relation

## 属性描述

| name | type | length | null or not | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| relation_id | int(10) | - | No | - | 自增的id, 主键 | - |
| weixin_appid | bigint(10) | - | No | - | 公众号插件唯一标识 | - |
| card_owner_open_id | varchar(45) | - | No | - | 卡片生成用户open_id | - |
| card_scanner_open_id | varchar(45) | - | No | - | 卡片扫描用户open_id | - |
| card_scanner_total_score | int(10) | - | No | 0 | 卡片扫描用户微积粉获取的总积分 | - |
| status | tinyint(1) | - | No | - | 状态 1正常2删除 | - |
| create_time | int(10) | - | No | - | 新建时间 | - |
| update_time | int(10) | - | No | - | 修改时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS | `status` ASC,| 用于根据微积粉是否开始状态查询微积粉关系 |
| IDX_WEIXIN_APPID_CARD_OWNER_OPEN_ID_CARD_SCANNER_OPEN_ID | `weixin_appid` ASC, `card_owner_open_id` ASC, `card_scanner_open_id` ASC | 用于根据公众号插件唯一标识、卡片生成用户open_id以及卡片扫描用户open_id查询微积粉关系 |
| IDX_STATUS_WEIXIN_APPID_CARD_OWNER_OPEN_ID | `weixin_appid` ASC, `card_owner_open_id` ASC, `status` ASC | 用于根据公众号插件唯一标识、卡片生成用户open_id以及是否删除查询微积粉关系 |
| IDX_STATUS_WEIXIN_APPID_CARD_OWNER_OPEN_ID | `weixin_appid` ASC, `card_scanner_open_id` ASC, `status` ASC | 用于根据公众号插件唯一标识、卡片扫描用户open_id以及是否删除查询微积粉关系 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存信息暂无！