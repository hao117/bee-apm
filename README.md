# BeeAPM
Application Performance Management
基于电信系统一天二三十亿条调用链数据采集经验

## 采集端（agent）
采集数据发送到`APM Server`端或者消息中间件或存储器等
#### 1、采集功能
- 方法调用链，包含采集方法参数，执行耗时
- http servlet调用链（请求报文，返回报文待开发）
  - 支持httpclient3
  - 支持httpclent4
  - 支持okhttp3
  - 支持基于servlet的容器（tomcat，weblogic，jetty等）
- jdbc采集，包含采集sql，参数，执行耗时
- logger采集（支持log4j，log4j2，logback）
- spring事务采集
- session采集（待开发）
- 采样控制（开发中）

#### 2、传输能力
- 输出到控制台
- 输出到远程http服务器
  - 使用okhttp发送
- 输出到kafka（待开发）
- 输出到ElasticSearcch（待开发）

## APM Server端
数据收集和存储。从采集端接受数据或者从消息中间件中拉取数据，并进行存储
#### 1、数据收集
- http接受，使用servlet接收采集端发送过来的数据
- 从kafka拉取数据（待开发）

#### 2、数据存储
- ElasticSearcch
- mysql（待开发）
- oracle（待开发）
- h2（待开发）

## APM WEB端
调用链展示，数据查询，报表等（待开发）

