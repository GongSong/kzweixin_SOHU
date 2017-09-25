# weixin_mass

## 属性描述

### 基本属性

| name | type | length | can be null | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| mass_id | bigint(10) | - | No | - | 主键，群发id | - |
| weixin_appid | bigint(10) | - | No | - | 微信插件id | - |
| response_type | int(10) | - | No | - | 返回给用户数据类型 | 1 文章列表，2 页面，3 文字，4 图片 |
| response_json | text | - | No | - | 返回给用户数据内容json | - |
| msg_id | varchar | 45 | No | - | 消息ID（群发之后返回消息ID，用于删除消息） | - |
| status_msg | varchar | 45 | No | - | 群发的结构，为“send success”或“send fail”或“err(num)”。但send success时，也有可能因用户拒收公众号 | - |
| total_count | int(10) | - | No | - | group_id下粉丝数；或者openid_list中的粉丝数 | - |
| filter_count | int(10) | - | No | - | 过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，FilterCount = SentCount + ErrorCount | - |
| sent_count | int(10) | - | No | - | 发送成功的粉丝数 | - |
| error_count | int(10) | - | No | - | 发送失败的粉丝数 | - |
| group_id | varchar | 45 | No | - | 群发消息指定的群组id，all表示全部 | - |
| is_timing | tinyint(1) | - | No | - | 是否定时任务（0否1是） | - |
| publish_time | int( 10 ) | - | No | - | 发布时间 | - |
| status | tinyint(1) | - | No | - | 状态 | 0 删除，1 发送成功，2 发送失败，3 已发送，4 未发送 |
| create_time | int( 10 ) | - | No | - | 新建时间 | - |
| update_time | int( 10 ) | - | No | - | 更新时间 | - |

## 索引
| name | columns | desc |
| ---- | ------- | ---- |
| IDX_WX_APPID_MSG_ID | `weixin_appid` ASC，`msg_id` ASC | 用于根据微信插件ID以及消息ID查询群发消息 |
| IDX_WX_APPID_STATUS | `weixin_appid` ASC，`status` ASC | 用于根据微信插件ID以及状态查询群发消息 |
| IDX_STATUS_CREATE_TIME | `create_time` ASC，`status` ASC | 用于根据创建时间以及状态查询群发消息 |

## 缓存
| key | type | desc |
| --- | ---- | ---- |

## 备注
1. 索引和缓存相关信息将来补充
