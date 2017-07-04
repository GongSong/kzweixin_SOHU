# weixin_attract_fans_card

## 属性描述

| name | type | length | null or not | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| card_id | bigint(10) | - | No | - | 卡片id，主键 | - |
| weixin_appid | bigint(10) | - | No | - | 公众号插件唯一标识 | - |
| open_id | varchar(45) | - | No | - | 用户open_id | - |
| qrcode_url | varchar(200) | - | No | - | 临时二维码地址 | - |
| qrcode_expire_time | int(10) | - | No | - | 临时二维码过期时间 | - |
| card_img_url | varchar(200) | - | No | - | 微积粉卡片图片地址 | - |
| card_media_id | varchar(100) | - | No | - | 卡片media_id | - |
| media_expire_time | int(10) | - | No | - | 卡片media_id过期时间 | - |
| status | tinyint(1) | - | No | - | 1正常2删除 | - |
| create_time | int(10) | - | No | - | 新建时间 | - |
| update_time | int(10) | - | No | - | 更新时间 | - |

## 索引

| name | columns | desc |
| ---- | ------- | ---- |


## 缓存

| key | type | desc |
| --- | ---- | ---- |

## 备注

1. 索引和缓存信息暂无！