
# 物联网平台

- 后端采用Spring boot
- 前端采用Vue
- 消息服务器采用EMQX
- 数据库采用Mysql、TDengine和Redis
- 设备端采用ESP32、ESP8266、树莓派、合宙等 
- 移动端支持微信小程序、安卓、苹果和H5采用Uniapp

![image](https://github.com/webVueBlog/Springboot-IOT-Platform/assets/59645426/8eaec2ce-c75e-4fc1-8714-7882fbb1b0a7)

时序数据库是专门用于存储和处理时间序列数据的数据库系统。时间序列数据是按时间顺序排列的数据集，例如传感器数据、日志数据、金融数据等。以下是一些常见的时序数据库：

InfluxDB: InfluxDB 是一个开源的时序数据库，专门设计用于处理高吞吐量的时间序列数据。它具有水平扩展性、内置的 SQL 查询语言（InfluxQL）和灵活的数据模型。

Prometheus: Prometheus 是一种开源的监控系统和时序数据库，最初由SoundCloud开发。它具有强大的查询语言（PromQL）和灵活的数据模型，被广泛用于监控和警报。

TimescaleDB: TimescaleDB 是一个开源的关系型数据库扩展，构建在 PostgreSQL 之上，专门用于处理时间序列数据。它提供了与标准 SQL 兼容的查询语言，以及水平扩展和自动分区功能。

OpenTSDB: OpenTSDB 是一个开源的分布式时序数据库，构建在 Apache HBase 之上。它特别适用于处理大规模的时间序列数据，并提供了用于数据存储和查询的 RESTful API。

KairosDB: KairosDB 是一个开源的时序数据库，设计用于存储大量的时间序列数据，并且可以与 Apache Cassandra 集成使用。它支持灵活的数据模型和基于 HTTP 的查询 API。

Graphite: Graphite 是一个开源的监控工具和时序数据库，主要用于收集、存储和可视化时间序列数据。它包含了一个存储后端，可以用于持久化数据，并提供了一个基于 HTTP 的查询 API。

## 系统功能

- 产品管理	产品详情、产品物模型、产品分类、设备授权、产品固件
- 设备管理	设备详情、设备分组、设备日志、设备分享、设备实时控制、实时状态、数据监测
- 物模型管理	属性（设备状态和监测数据），功能（执行特定任务），事件（设备主动上报给云端）
- MQTT接入	支持emqx4.0、 emqx5.0、 自研的Netty-mqtt作为mqtt broker
- 硬件 SDK	ESP-IDF、Arduino、RaspberryPi、合宙等平台设备接入

## 技术栈

### 服务端

- 相关技术：Spring boot、MyBatis、Spring Security、Jwt、Mysql、Redis、TDengine、EMQX、Netty等
- 开发工具：IDEA

### Web端

- 相关技术：ES6、Vue、Vuex、Vue-router、Vue-cli、Axios、Element-ui、Echart等
- 开发工具：Visual Studio Code

### 移动端（微信小程序 / Android / Ios / H5）

- 相关技术：uniapp、[uView](https://www.uviewui.com/)、[uChart](https://www.ucharts.cn/)
- 开发工具：HBuilder

### 硬件端

- 相关技术： ESP-IDF、Arduino、FreeRTOS、Python、Lua等
- 开发工具：Visual Studio Code 和 Arduino等

### 一、项目目录

- admin     ------------- 主程序入口
- common    ---------- 公共模块
- framework -------- 开发框架
- gateway   ----------- 消息通道转发<br/>
- open-api  ---------- 系统开放接口
- plugs     --------------- 拓展插件<br/>
- protocol  ------------ 编解码协议
- server    --------------- 传输层服务端 （netty-mqtt,tcp,udp,sip,coap）
- service   -------------- 核心业务处理<br/>

![image](https://github.com/webVueBlog/Springboot-IOT-Platform/assets/59645426/27852925-ca9a-4ceb-b385-9417afddadf1)

- docker部署文件
- Mqtt消息服务器使用EMQX5.0开源版

### 新增功能

- 支持netty mqtt broker ([#1]())
- 支持多种编码协议管理([#2]())
- 支持emqx5.0([#3]())


