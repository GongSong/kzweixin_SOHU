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
3.1 图片上传接口


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
5.2

## 统计锚点
6.1 发表图文数