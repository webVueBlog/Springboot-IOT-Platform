package com.webVueBlog.mqtt.model;

import io.netty.handler.codec.mqtt.MqttPublishMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 */
@Data
@AllArgsConstructor
public class WillMessage implements Serializable {
    private static final long serialVersionUID = -1L;

    /*客户端Id*/
    private String clientId;
    /*清楚客户端*/
    private boolean cleanSession;
    /*topic*/
    private String topic;
    /*客户端推送消息*/
    private MqttPublishMessage message;
}
