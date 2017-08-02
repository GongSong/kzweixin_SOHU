# 链接组文档

重构后的菜单层级如下

| 一级 | 二级 | 三级 |
| --- | --- | --- |
| 网址(URL) | |
| 页面(PAGE) | 页面项 | |
| 快文(ARTICLE) | 快文首页(HOME) | |
|     | 快文栏目(COLUMN) | 栏目项 |
|     | 快文文章(ARTICLE) | 文章项 |
| 社区(CLUB) | 社区项  |  |
| 电商(SHOP) | 店铺项  | 首页(HOME)/购物车(CART)/我的订单(ORDER) |
| 海报(POSTER) | 海报项  |  |

### 各个菜单的接口文档

* 页面 无文档  
* 快文 无文档  
* 社区 稍后给出  
* 电商 [获取店铺列表] (http://c.sohuno.com/kuaizhan/kclub/blob/master/docs/shop_apis.md#1)  
* 海报 暂无  


### 链接组数据结构

json结构示例:

```json
{
  "linkGroups": [
      {
          "title": "我是个标题",
          "description": "我是个摘要",
          "picUrl": "http://pic.kuaizhan.com/fda2df23432.jpg",  // 封面图
          "url": "http://www.sohu.com/news",  // 链接组跳转链接
          "linkType": "SHOP",  // 链接组类型，见详细说明
          "linkId": "WIMVcFhO2jV2A7j_,ORDER",
      }
  ]
}
```

### 重要字段说明

#### linkType

链接类型，可选值为`URL、PAGE、CLUB、SHOP、POSTER、ARTICLE`，各自的含义见上表

#### linkId 

| linkId | 含义 |
| --- | --- |
| 网址URL | linkId无意义，不传此字段 |
| 页面PAGE | 页面的ID |
| 社区CLUB | 社区的ID |
| 电商SHOP |  `<店铺ID>,<首页(HOME)/购物车(CART)/我的订单(ORDER)>`, 例如`123,ORDER`的含义是id为123店铺的购物车 |
| 海报POSTER | 海报的ID |
| 快文ARTICLE | 快文首页值为`HOME`, 快文栏目为`COLUMN,<栏目ID>`, 快文文章为`ARTICLE,<文章ID>` |
