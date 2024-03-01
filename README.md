
# 物联网平台

- 后端采用Spring boot
- 前端采用Vue
- 消息服务器采用EMQX
- 数据库采用Mysql、TDengine和Redis
- 设备端采用ESP32、ESP8266、树莓派、合宙等 
- 移动端支持微信小程序、安卓、苹果和H5采用Uniapp

## 系统功能

- 产品管理	产品详情、产品物模型、产品分类、设备授权、产品固件
- 设备管理	设备详情、设备分组、设备日志、设备分享、设备实时控制、实时状态、数据监测
- 物模型管理	属性（设备状态和监测数据），功能（执行特定任务），事件（设备主动上报给云端）
- MQTT接入	支持emqx4.0、 emqx5.0、 自研的Netty-mqtt作为mqtt broker
- 硬件 SDK	ESP-IDF、Arduino、RaspberryPi、合宙等平台设备接入

## 技术栈

* 服务端

- 相关技术：Spring boot、MyBatis、Spring Security、Jwt、Mysql、Redis、TDengine、EMQX、Netty等
- 开发工具：IDEA

* Web端

- 相关技术：ES6、Vue、Vuex、Vue-router、Vue-cli、Axios、Element-ui、Echart等
- 开发工具：Visual Studio Code
* 移动端（微信小程序 / Android / Ios / H5）
- 相关技术：uniapp、[uView](https://www.uviewui.com/)、[uChart](https://www.ucharts.cn/)
- 开发工具：HBuilder
* 硬件端
- 相关技术： ESP-IDF、Arduino、FreeRTOS、Python、Lua等
- 开发工具：Visual Studio Code 和 Arduino等

![image](https://github.com/webVueBlog/Springboot-IOT-Platform/assets/59645426/27852925-ca9a-4ceb-b385-9417afddadf1)

- docker部署文件
- Mqtt消息服务器使用EMQX5.0开源版




