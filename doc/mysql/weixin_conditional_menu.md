# weixin_conditional_menu

## 属性描述

| name | type | length | null or not | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| id | bigint(10) | - | No | 0 | 个性化菜单id | - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id | - |
| menu_name | varchar(100) | - | Yes | NULL | 个性化菜单名称 | - |
| menu_json | text | - | Yes | NULL | 菜单内容 | - |
| menu_id | varchar(45) | - | Yes | - | 微信返回的菜单标识符 | - |
| status | tinyint(1) | - | No | 1 | 状态：1正常2删除 | - |
| create_time | int(10) | - | No | - | 创建时间 | - |
| update_time | int(10) | - | No | - | 更新时间 | - |

## 索引

| name | columns | desc |
| ---- | ------- | ---- |
| IDX_WX_APPID_STATUS | `weixin_appid` ASC，`status` ASC | 用于根据微信插件ID以及状态查询个性化菜单列表 |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询个性化菜单列表 |

## 缓存

| key | type | desc |
| --- | ---- | ---- |

## 备注

1. 索引和缓存信息暂无！