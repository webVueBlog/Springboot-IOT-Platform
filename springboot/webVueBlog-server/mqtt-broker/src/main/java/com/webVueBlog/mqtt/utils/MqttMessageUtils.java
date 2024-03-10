package com.webVueBlog.mqtt.utils;

import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.common.enums.DeviceStatus;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.mqtt.model.ClientMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.*;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 服务器应答信息构建
 * 
 */
public class MqttMessageUtils {


    /**
     * 服务器确认连接应答消息 CONNACK
     */
    public static MqttConnAckMessage buildConntAckMessage(MqttConnectReturnCode code, boolean sessionPresent) {
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.CONNACK);// 固定报头
        MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(code, sessionPresent);// 可变报头
        return new MqttConnAckMessage(fixedHeader, variableHeader);// 连接应答消息
    }


    /**
     * 设备ping(心跳信息)应答 PINGRESP
     */
    public static MqttMessage buildPingResp() {
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.PINGRESP);// 固定报头
        return new MqttMessage(fixedHeader);// ping响应消息
    }

    /**
     * 取消订阅消息应答 UNSUBACK
     */
    public static MqttUnsubAckMessage buildUnsubAckMessage(MqttMessage message) {
        /*构建固定报文*/
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.UNSUBACK);// 固定报头
        return new MqttUnsubAckMessage(fixedHeader, getIdVariableHeader(message));// 取消订阅应答消息
    }

    /**
     * 订阅确认应答 SUBACK
     */
    public static MqttSubAckMessage buildSubAckMessage(MqttMessage message) {/*构建固定报文*/
        /*构建固定报文*/
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.SUBACK);// 固定报头
        /*构建可变报文*/
        MqttSubscribeMessage mqttSubscribeMessage = (MqttSubscribeMessage) message;

        /*获取订阅topic的Qos*/
        Set<String> topics = mqttSubscribeMessage.payload().topicSubscriptions().stream().map(MqttTopicSubscription::topicName).collect(Collectors.toSet());
        // 主题集合
        List<Integer> grantedQos = new ArrayList<>(topics.size());// 获取订阅topic的Qos
        for (int i = 0; i < topics.size(); i++) {
            grantedQos.add(mqttSubscribeMessage.payload().topicSubscriptions().get(i).qualityOfService().value());// 添加到集合中
        }
        /*负载*/
        MqttSubAckPayload payload = new MqttSubAckPayload(grantedQos);// 订阅确认应答消息的负载
        return new MqttSubAckMessage(fixedHeader, getIdVariableHeader(message), payload);// 订阅确认应答消息
    }

    /**
     * 构建推送应答消息 PUBLISH
     */
    public static MqttPublishMessage buildPublishMessage(ClientMessage msg, int packageId) {
        /*报文固定头*/
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, msg.isDup(), msg.getQos(), false, 0);
        /*报文可变头*/
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(msg.getTopicName(), packageId);
        /*负载*/
        ByteBuf payload = msg.getPayload() == null ? Unpooled.EMPTY_BUFFER : Unpooled.wrappedBuffer(msg.getPayload());
        /*完整报文，固定头+可变头+payload*/
        return new MqttPublishMessage(fixedHeader, variableHeader, payload);
    }

    /**
     * Qos1 收到发布消息确认 无负载 PUBACK
     */
    public static MqttPubAckMessage buildAckMessage(MqttMessage message) {
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.PUBACK);// 构建固定报文头
        return new MqttPubAckMessage(fixedHeader, getIdVariableHeader(message));
    }

    /**
     * Qos2 发到消息收到 无负载 PUBREC
     */
    public static MqttMessage buildPubRecMessage(MqttMessage message){
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.PUBREC);// 构建固定报文头
        return new MqttMessage(fixedHeader, getIdVariableHeader(message));
    }

    /**
     * Qos2 发布消息释放 PUBREL
     */
    public static MqttMessage buildPubRelMessage(MqttMessage message){
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.PUBREL);// 构建固定报文头
        return new MqttMessage(fixedHeader, getIdVariableHeader(message));

    }

    /**
     * Qos2 发布消息完成 PUBCOMP
     */
    public static MqttMessage buildPubCompMessage(MqttMessage message){
        MqttFixedHeader fixedHeader = buildFixedHeader(MqttMessageType.PUBCOMP);// 构建固定报文头
        return new MqttMessage(fixedHeader, getIdVariableHeader(message));
    }

    /**
     * 固定头定制
     */
    public static MqttFixedHeader buildFixedHeader(MqttMessageType messageType) {
        return new MqttFixedHeader(messageType, false, MqttQoS.AT_MOST_ONCE, false, 0);// 构建固定报文头
    }

    /**
     * 构造MqttMessageIdVariableHeader
     */
    public static MqttMessageIdVariableHeader getIdVariableHeader(MqttMessage mqttMessage) {
        MqttMessageIdVariableHeader idVariableHeader = (MqttMessageIdVariableHeader) mqttMessage.variableHeader();// 获取消息id
        return MqttMessageIdVariableHeader.from(idVariableHeader.messageId());// 构建消息id
    }

    /*构造返回MQ的设备状态model*/
    public static DeviceStatusBo buildStatusMsg(ChannelHandlerContext ctx, String clientId,DeviceStatus status,String ip){
        return DeviceStatusBo.builder()
                .serialNumber(clientId)
                .status(status)
                .ip(ip)
                .hostName(ip)
                .timestamp(DateUtils.getNowDate()).build();
    }
}
