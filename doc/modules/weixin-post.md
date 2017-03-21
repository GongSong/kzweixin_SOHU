# 微信图文消息模块总览
## 功能
1.1 图文消息编辑
 - 多图文设置
 - 图片上传

1.2 图文消息列表
 - 列表
 - 从微信图文导入
 - 从快站图文导入

## 页面
2.1 图文编辑器页面
2.2 图文列表页面

## 后端接口
3.1 微信导入图文
3.2 快站文章导入图文
3.3 获取已有图文
3.4 通过图文标题或图文关键词搜索图文
3.5 删除图文
3.6 保存图文
3.7 修改图文
3.8 上传图片至个人图片库
3.9 从个人图片库获取图片列表
3.10 从素材库获取图片列表


## 消息队列
4.1 从微信导入图片队列
4.2 从快站文章导入队列

## 数据结构
5.1 [weixin_post](http://c.sohuno.com/kuaizhan/kuaizhan-doc/blob/master/internal/modules/mysql/kuaizhan_weixin_*/weixin_post.md) 存储微信图文消息主题  
5.2 [icon_user](http://c.sohuno.com/kuaizhan/kuaizhan-doc/blob/master/internal/modules/mysql/kuaizhan_site_*/icon_user.md)  存储"我的图片"    
5.3 [icon_tag](http://c.sohuno.com/kuaizhan/kuaizhan-doc/blob/master/internal/modules/mysql/kuaizhan_site_*/icon_tag.md)  存储"我的图片"分组信息  

## 统计锚点
6.1 发表图文数