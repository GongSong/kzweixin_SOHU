# 微信图文消息模块总览
## 功能 & 页面

[请参考文档](http://confluence.sohuno.com/pages/viewpage.action?pageId=24448540)

## 后端接口
3.1 微信导入图文

3.2 快站文章导入图文

3.3 获取已有图文

3.4 通过图文标题或图文关键词搜索图文

3.5 删除图文

3.6 新建图文

3.7 修改图文

3.8 上传图片至个人图片库

3.9 从个人图片库获取图片列表

3.10 从素材库获取图片列表


## 消息队列

4.1 从微信导入图片队列
    - exchange name: sys-weixin-post-sync.direct
    - queue name: sys-weixin-post-sync
    - 消息内容
        site_id
        weixin_appid
        post_list

4.2 从快站文章导入队列
    - exchange name: sys-weixin-import-kuaizhan-post.direct
    - queue name: sys-weixin-import-kuaizhan-post
    - 消息内容
        site_id
        weixin_appid
        post_list

4.3 图文保存并同步队列
    - 线上无

4.4 多图文合并队列
    - 线上无

## 数据结构
5.1 [weixin_post](http://c.sohuno.com/kuaizhan/kuaizhan-doc/blob/master/internal/modules/mysql/kuaizhan_weixin_*/weixin_post.md) 存储微信图文消息主题  
5.2 [icon_user](http://c.sohuno.com/kuaizhan/kuaizhan-doc/blob/master/internal/modules/mysql/kuaizhan_site_*/icon_user.md)  存储"我的图片"    
5.3 [icon_tag](http://c.sohuno.com/kuaizhan/kuaizhan-doc/blob/master/internal/modules/mysql/kuaizhan_site_*/icon_tag.md)  存储"我的图片"分组信息  

## 统计锚点

[请参考文档](http://confluence.sohuno.com/pages/viewpage.action?pageId=24448540)