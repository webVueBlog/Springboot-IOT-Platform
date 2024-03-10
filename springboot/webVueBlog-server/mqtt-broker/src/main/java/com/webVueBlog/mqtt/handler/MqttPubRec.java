package com.webVueBlog.mqtt.handler;

import com.webVueBlog.mqtt.annotation.Process;
import com.webVueBlog.mqtt.handler.adapter.MqttHandler;
import com.webVueBlog.mqtt.manager.ClientManager;
import com.webVueBlog.mqtt.manager.ResponseManager;
import com.webVueBlog.mqtt.service.IMessageStore;
import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.util.AttributeUtils;
import com.webVueBlog.mqtt.utils.MqttMessageUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 消息等级=Qos2 发布消息收到 交付第一步
 *
 * 
 */
@Process(type = MqttMessageType.PUBREC)
@Slf4j
public class MqttPubRec implements MqttHandler {

    @Autowired
    private IMessageStore messageStore;

    @Override
    public void handler(ChannelHandlerContext ctx, MqttMessage message) {
        MqttMessageIdVariableHeader variableHeader = MqttMessageUtils.getIdVariableHeader(message);
        //clientId
        String clientId = AttributeUtils.getClientId(ctx.channel());
        Session session = AttributeUtils.getSession(ctx.channel());
        //获取packetId
        int messageId = variableHeader.messageId();
        /*释放消息*/
        messageStore.removePubMsg(messageId);
        messageStore.saveRelOutMsg(messageId);
        // 回复REL 进入第二阶段
        MqttMessage mqttMessage = MqttMessageUtils.buildPubRelMessage(message);
        ResponseManager.responseMessage(session, mqttMessage, true);
        /*更新平台ping*/
        ClientManager.updatePing(session.getClientId());
    }
}
