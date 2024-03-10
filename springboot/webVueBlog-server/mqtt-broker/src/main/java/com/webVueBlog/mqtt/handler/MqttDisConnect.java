package com.webVueBlog.mqtt.handler;

import com.webVueBlog.mqtt.annotation.Process;
import com.webVueBlog.mqtt.handler.adapter.MqttHandler;
import com.webVueBlog.mqtt.manager.ClientManager;
import com.webVueBlog.mqtt.manager.SessionManger;

import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.util.AttributeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端主动断开连接(正常断开)
 *
 * 
 */
@Process(type = MqttMessageType.DISCONNECT)
@Slf4j
public class MqttDisConnect implements MqttHandler {


    @Override
    public void handler(ChannelHandlerContext ctx, MqttMessage message) {
        /*获取clientId*/
        String clientId = AttributeUtils.getClientId(ctx.channel());
        /*获取session*/
        Session session = AttributeUtils.getSession(ctx.channel());
        log.debug("=>客户端正常断开,clientId:[{}]", clientId);
        try {
            if (!session.getConnected()) {
                session.getHandlerContext().close();
                return;
            }
            /*处理断开客户端连接*/
            SessionManger.pingTimeout(session.getClientId());
            /*移除相关topic*/
            ClientManager.remove(session.getClientId());
        } catch (Exception e) {
            log.error("=>客户端断开连接异常:{}", session);
        }
    }
}
