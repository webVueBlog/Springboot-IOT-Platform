package com.webVueBlog.mqtt.handler.adapter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;

/**
 * 
 */
public interface MqttHandler {

    public void handler(ChannelHandlerContext ctx, MqttMessage message);
}
