## 环境
IntelliJ IDEA 2018.2.5+jdk8+mysql-8.0.13+redis+maven+git <br> 
## 模块介绍
| 项目 | 端口 |描述 |
| ------ | ------ | ------ |
| zzq-admin | 8088 | 用户资源管理模块 |
| sping-cloud-eureka  | 8888 | 服务注册中心  |
| spring-cloud-zuul  | 8666 | 网关 |
| zzq-common | 0 | 公共部分模块封装|
| spring-boot-admin | 9999 | 服务监控中心|
| spring-boot-quartz | 8777 | 定时任务|
| spring-boot-websocket | 8084 | websocket|
## 功能点介绍
+ zzq-admin <br> 
  - admin用户管理
  - mybatis-plus使用
  - swagger使用
  - rest风格
+ sping-cloud-eureka <br> 
  - eureka服务注册中心
+ spring-cloud-zuul <br> 
  - zuul网关
  - Spring cloud Scurity Oauth2 权限验证
  - feign使用
  - Hystric使用
+ zzq-common <br> 
  - zzq-admin
  - zzq-admin
  - zzq-admin
  - zzq-admin
+ spring-boot-admin <br> 
  - 集成eureka监控服务状态
+ spring-boot-quartz <br> 
  - 可视化监控与管理定时任务
+ spring-boot-websocket <br> 
  - redis消息订阅
  - websocket实时通信

## 主要功能点介绍
### 1、Spring cloud Scurity Oauth2 权限验证
OAuth2四个重要角色 https://blog.csdn.net/qq_33594101/article/details/84895775 <br> 
OAuth2四中授权模式 https://blog.csdn.net/qq_33594101/article/details/84896767 <br> 
Spring cloud security oauth2搭建 https://blog.csdn.net/qq_33594101/article/details/84898267 <br> 
### 2.spring-boot-admin可视化应用监控中心
SpringBootAdmin应用监控基于eureka搭建 https://blog.csdn.net/qq_33594101/article/details/84919962 <br> 
### 3.spring-boot-quartz可视化监控与管理定时任务中心
### 4.swagger2构建可视化操作的RESTAPI
swagger2常用注解使用 https://blog.csdn.net/qq_33594101/article/details/84797771 <br> 
springboot2整合swagger2构建强大的RESTful API文档 https://blog.csdn.net/qq_33594101/article/details/84797551 <br> 
### 5.spring-boot-websocket前后端实时通信，业务订阅
## 后期扩展功能点
