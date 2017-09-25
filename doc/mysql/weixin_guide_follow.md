# weixin_guide_follow

公众号引导关注

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| weixin_appid | bigint(10) | - | No | - | 主键，微信插件id| - |
| nick_name | varchar(45) | - | No | - | 公众号名称 | - |
| head_img | varchar(512) | - | No | - | 公众号头像 | - |
| page_id | bigint(10) | - | No | - | 图文对应的页面id | - |
| post_url | varchar(200) | - | No | - | 引导关注的图文链接 | - |
| status | tinyint(1) | - | No | 0 | 状态 | 1 正常发布，2 删除 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询引导关注信息 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充