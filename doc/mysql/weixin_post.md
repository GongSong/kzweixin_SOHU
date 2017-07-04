# weixin_post

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| page_id | bigint(10) | - | No | - | 文章页面id,主键| - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id| - |
| title | varchar | 640 | No | - | 文章标题| - |
| thumb_media_id | varchar | 50 | No | - | 封面图的media_id| - |
| thumb_url | varchar | 512 | No | - | 封面图的url字符串| - |
| show_cover_pic | tinyint(1) | - | No | 0 | 是否显示封面 | 0 不显示，1 显示 |
| author | varchar | 50 | No | - | 文章作者| - |
| digest | varchar | 512 | No | - | 文章摘要| - |
| post_url | varchar | 512 | No | - | 文章内容url字符串| - |
| content_source_url | varchar | 512 | No | - | 图文消息的原文地址| - |
| media_id | varchar | 50 | No | - | 图文media_id，在微信平台的唯一标识| - |
| sync_time | int( 10 ) | - | No | 0 | 同步时间 | - |
| type | tinyint(1) | - | No | 1 | 文章类型 | 1 单图文，2 多图文总记录，3 多图文中的一篇单图文  |
| index | int( 3 ) | - | No | 0 | 文章序号 | 只在多图文中有意义 |
| kuaizhan_post_id | bigint(10) | - | No | - | 快站文章id | - |
| status | tinyint(1) | - | No | 0 | 状态 | 1 正常发布，2 删除 |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_WX_APPID_STATUS_KZ_POST_ID | `weixin_appid` ASC，`status` ASC，`kuaizhan_post_id` ASC | 用于根据微信插件ID、文章状态以及快站文章ID查询文章 |
| IDX_STATUS_TYPE_MD_ID | `status` ASC, `type` ASC，`media_id` ASC | 用于根据状态、文章类型以及图文ID查询文章 |
| IDX_WX_APPID_MD_ID_STATUS_TYPE | `weixin_appid` ASC, `media_id` ASC，`status` ASC，`type` ASC | 用于根据微信插件ID、图文ID、文章状态以及文章类型查询文章 |
| IDX_WX_APPID_INDEX_STATUS_TYPE | `weixin_appid` ASC, `index` ASC，`status` ASC，`type` ASC | 用于根据微信插件ID、序号、文章状态以及文章类型查询文章 |
| IDX_STATUS_TYPE_CREATE_TIME | `status` ASC, `type` ASC，`create_time` ASC | 用于根据文章状态、文章类型以及创建时间查询文章 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
