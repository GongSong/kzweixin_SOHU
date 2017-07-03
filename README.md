# 快站微信公众号后端文档

## 接口文档
* [公众号接口约定](doc/modules/api-common.md)
* [公众号接口文档](doc/modules/index.md)


## 部署
1. 部署web端，以开发环境为例：

    `sh deploy/web/build.sh dev`

1. 部署消息队列，以开发环境为例：

    `sh deploy/worker/build.sh dev`
