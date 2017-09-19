# weixin_attract_fans_config

## 属性描述

| name | type | length | null or not | default | desc | dict |
| ---- | ---- | ------ | ----------- | ------- | ---- | ---- |
| weixin_appid | bigint(10) | - | No | - | 公众号插件唯一标识，主键 | - |
| card_tpl_id | tinyint(1) | - | No | - | 积分卡片模版id 1黑色字体模版2白色字体模版 | - |
| card_bg_url | varchar(512) | - | No | - | 积分卡片背景图片地址 | - |
| card_tips | varchar(45) | - | No | - | 积分卡片文字提示 | - |
| scanner_score_num | int(10) | - | No | - | 新人扫码积分 | - |
| promotion_score_rule_type | tinyint(1) | - | No | - | 推广积分规则类型(生成积分卡片的人获取积分的规则类型) 1普通规则2高级规则 | - |
| promotion_score_rule_json | varchar(200) | - | No | - | 推广积分规则内容(生成积分卡片的人获取积分的规则内容) | - |
| product_ids_json | varchar(200) | - | No | - | 积分商品列表 | - |
| integral_shop_json | varchar(256) | - | No | - | 积分店铺数据（是否开启,店铺id,店铺链接）| - |
| generate_qrcode_menu_key | bigint(10) | - | No | - | 生成微积粉卡片图片的menu_key | - |
| my_score_menu_key | bigint(10) | - | No | - | 我的积分菜单menu_key | - |
| guide_follow_page_id | bigint(10) | - | No | - | 微积粉引导关注图文对应的页面id | - |
| guide_follow_post_url | varchar(200) | - | No | - | 微积粉引导关注图文 | - |
| instruction_page_id | bigint(10) | - | No | - | 微积粉玩法说明图文对应的页面id | - |
| condition_menu_id | bigint(10) | - | No | 0 | 个性化菜单id,默认菜单则为0 | - |
| status | tinyint(1) | - | No | - | 1开启2关闭 | - |
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