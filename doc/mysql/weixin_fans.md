# weixin_fans

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| fan_id | int(10) | - | No | - | 主键，粉丝id，自增| - |
| app_id | varchar(45) | - | No | - | 对应公众号app_id | - |
| open_id | varchar(45) | - | No | - | 粉丝对公众号唯一标识 | - |
| nick_name | varchar(45) | - | No | - | 用户的昵称 | - |
| sex | tinyint(1) | - | No | - | 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知 | - |
| city | varchar(45) | - | No | - | 用户所在城市 | - |
| country | varchar(45) | - | No | - | 用户所在国家 | - |
| province | varchar(45) | - | No | - | 用户所在省份 | - |
| language | varchar(45) | - | No | - | 用户的语言，简体中文为zh_CN | - |
| head_img_url | varchar(512) | - | No | - | 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。 | - |
| subscribe_time | int(10) | - | No | 0 | 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间 | - |
| union_id | varchar(45) | - | No | - | 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。 | - |
| remark | varchar(45) | - | No | - | 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注 | - |
| group_id | int(10) | - | No | - | 用户所在的分组ID | - |
| tag_ids_json | varchar(256) | - | No | - | 标签数据json | - |
| in_blacklist | tinyint(1) | - | No | 0 | 是否在黑名单中,0否1是 | - |
| last_interact_time | int( 10 ) | - | No | 0 | 最后互动时间 | - |
| status | tinyint(1) | - | No | - | 1正常 2删除 | - |
| create_time | int( 10 ) | - | No | 0 | 创建时间 | - |
| update_time | int( 10 ) | - | No | 0 | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_APP_ID_OPEN_ID | `status` ASC，`app_id` ASC，`open_id` ASC | 用于根据状态、微信公众平台开发模式提供的app_id以及用户对应这个公众号的open_id查询公众号粉丝记录 |
| IDX_STATUS_APP_ID_UNION_ID | `status` ASC，`app_id` ASC，`union_id` ASC | 用于根据状态、微信公众平台开发模式提供的app_id以及用户对应这个公众号的union_id查询公众号粉丝记录 |
| IDX_STATUS_APP_ID_GROUP_ID | `status` ASC，`app_id` ASC，`group_id` ASC | 用于根据状态、微信公众平台开发模式提供的app_id以及用户对应这个公众号的group_id查询公众号粉丝记录 |
| IDX_STATUS_CREATE_TIME_UPDATE_TIME | `status` ASC，`create_time` ASC，`update_time` ASC | 用于根据状态、创建时间以及更新时间查询公众号粉丝记录 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充