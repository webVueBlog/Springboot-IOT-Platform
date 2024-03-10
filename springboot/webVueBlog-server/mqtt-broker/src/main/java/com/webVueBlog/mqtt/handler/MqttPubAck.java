package com.webVueBlog.mqtt.handler;

import com.webVueBlog.mqtt.annotation.Process;
import com.webVueBlog.mqtt.handler.adapter.MqttHandler;
import com.webVueBlog.mqtt.manager.ClientManager;
import com.webVueBlog.mqtt.service.IMessageStore;
import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.util.AttributeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息等级=Qos1,收到发布消息确认
 *
 * 
 */
@Process(type = MqttMessageType.PUBACK)
@Slf4j
public class MqttPubAck implements MqttHandler {

    @Autowired
    private IMessageStore messageStore;

    @Override
    public void handler(ChannelHandlerContext ctx, MqttMessage message) {
        MqttPubAckMessage ackMessage = (MqttPubAckMessage) message;
        // PacketId
        int packetId = ackMessage.variableHeader().messageId();
        Session session = AttributeUtils.getSession(ctx.channel());
        // Qos1 的存储消息释放
        messageStore.removePubMsg(packetId);
        /*更新平台ping*/
        ClientManager.updatePing(session.getClientId());
    }
}
