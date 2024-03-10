package com.webVueBlog.iot.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 设备MQTT连接参数实体类
 * 
 */
@Accessors(chain = true)
@Data
public class DeviceMqttConnectVO {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 连接用户名
     */
    private String username;

    /**
     * 连接密码
     */
    private String passwd;

    /**
     * 连接端口号
     */
    private Long port;

}
