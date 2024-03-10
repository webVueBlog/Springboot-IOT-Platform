package com.webVueBlog.mqtt.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 订阅topic信息
 * 
 */
@Data
@AllArgsConstructor
public class Subscribe {
    /*topic*/
    private String topicName;
    /*消息质量*/
    private int qos;
    /*客户端id*/
    private String clientId;
    /*清楚回话*/
    private boolean cleanSession;

}
