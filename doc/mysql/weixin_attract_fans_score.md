# weixin_attract_fans_score

## 属性描述

| name | type | length | null or not | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| score_id | int(10) | - | No | - | 自增的id，主键 | - |
| weixin_appid | int(10) | - | No | - | 公众号插件唯一标识 | - |
| open_id | varchar(45) | - | No | - | 获取积分的用户open_id | - |
| score_num | int(10) | - | No | - | 获取的积分值 | - |
| score_type | tinyint(1) | - | No | - | 积分类型1扫码关注获取积分2粉丝扫码获取积分 | - |
| status | tinyint(1) | - | No | - | 1正常2删除 | - |
| create_time | int(10) | - | No | - | 新建时间 | - |
| update_time | int(10) | - | No | - | 修改时间 | - |

## 索引

| name | columns | desc |
| ---- | ------- | ---- |
| IDX_STATUS_WEIXIN_APPID_CREATE_TIME | `weixin_appid` ASC, `status` ASC, `create_time` ASC | 用于根据公众号唯一标识、是否删除以及创建时间查询微积粉分数 |
| IDX_STATUS_WEIXIN_APPID_OPEN_ID_SCORE_TYPE | `weixin_appid` ASC, `open id` ASC, `score_type` ASC, `status` ASC | 用于根据公众号插件唯一标识、获取积分的用户open_id、积分类型以及是否删除来查询微积粉积分 |
| IDX_STATUS | `status` ASC | 用于根据是否删除查询微积粉积分 |


## 缓存

| key | type | desc |
| --- | ---- | ---- |

## 备注

1. 索引和缓存信息暂无！