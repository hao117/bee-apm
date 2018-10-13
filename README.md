# BeeAPM
Application Performance Monitoring
基于电信系统一天二三十亿条调用链数据采集经验

## 部署
- [agent部署](docs/cn/agent-deploy.md)
- [server部署](docs/cn/server-deploy.md)
- [UI部署](docs/cn/ui-deploy.md)

## 介绍
### 采集端（agent）
采集数据发送到`APM Server`端或者消息中间件或存储器等
#### 1、采集功能
所有的采集功能均有单独开关配置
- 分布式调用链（方法调用链路、远程调用追踪）
- 采样控制
- 方法采集
    - 方法调用链路
    - 参数采集
    - 执行耗时
    - 方法信息
    - 耗时阀值控制（超过一定时间阀值的不采集）
    - 采集点配置
    - 返回值采集 （考虑）
- http追踪
  - 支持httpclient3
  - 支持httpclient4
  - 支持okhttp3
  - jdk-http（待开发）
- 支持servlet采集（tomcat，weblogic，jetty等）
    - 链路支持
    - 基本请求信息
    - 耗时采集
    - 耗时阀值控制
    - request body采集
    - request parameter
    - request header采集（待开发）
    - response body采集（待开发）
    - session采集（待开发）
- jdbc采集，包含采集sql语句、参数、执行耗时、耗时阀值控制、结果集大小
- logger采集（支持log4j，log4j2，logback），采集点可配置，error以上级别是否采样控制
- spring事务采集,包含事务持有时间、执行sql次数、事务耗时阀值控制
- 异常信息采集,支持采集点黑白名单配置
- 半自动API（待开发）
- jvm、cpu、磁盘、内存、线程等采集（待开发）

#### 2、传输能力
- 输出到控制台
- 输出到远程http服务器
  - 使用okhttp发送
- 输出到kafka（待开发）
- 输出到ElasticSearcch（待开发）

### APM Server端
数据收集和存储。从采集端接受数据或者从消息中间件中拉取数据，并进行存储
#### 1、数据收集
- http接受，使用servlet接收采集端发送过来的数据
- 从kafka拉取数据（待开发）

#### 2、数据存储
- ElasticSearch
- mysql（待开发）
- oracle（待开发）
- h2（待开发）

### APM UI端
开发中。。。
支持调用链展示，采集数据查询，报表等

