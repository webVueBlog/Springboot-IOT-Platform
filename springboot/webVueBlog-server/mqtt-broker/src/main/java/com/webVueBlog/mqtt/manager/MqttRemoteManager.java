package com.webVueBlog.mqtt.manager;

import com.webVueBlog.common.enums.DeviceStatus;
import com.webVueBlog.common.enums.TopicType;
import com.webVueBlog.common.utils.gateway.mq.TopicsUtils;
import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.service.IDeviceService;
import com.webVueBlog.mq.mqttClient.PubMqttClient;
import com.webVueBlog.mqtt.model.PushMessageBo;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * mqttBroker获取设备在线情况
 * 
 */
@Component
public class MqttRemoteManager {

    @Resource
    private TopicsUtils topicsUtils;
    @Resource
    private IDeviceService deviceService;
    /**
     * true: 使用netty搭建的mqttBroker  false: 使用emq
     */
    @Value("${server.broker.enabled}")
    private Boolean enabled;

    @Resource
    private PubMqttClient pubMqttClient;

    /**
     * 检查设备是否在该集群节点上(集群)
     * @param clientId
     * @return
     */
    public static boolean checkDeviceStatus(String clientId){
        return ClientManager.validClient(clientId);
    }


    /**
     * 推送设备状态
     * @param serialNumber 设备
     * @param status 状态
     */
    public  void pushDeviceStatus(Long productId, String serialNumber, DeviceStatus status){
        //兼容emqx推送TCP客户端上线
        Device device = deviceService.selectDeviceNoModel(serialNumber);
        String message = "{\"status\":" + status.getType() + ",\"isShadow\":" + device.getIsShadow() + ",\"rssi\":" + device.getRssi() + "}";
        String topic = topicsUtils.buildTopic(device.getProductId(), serialNumber, TopicType.STATUS_POST);
        if (enabled){
            MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttPublishVariableHeader(topic, 0),
                    Unpooled.buffer().writeBytes(message.getBytes(StandardCharsets.UTF_8))
            );
            ClientManager.pubTopic(publishMessage);
        }else {
            //emqx直接用客户端推送
            pubMqttClient.publish(1,false,topic,message);
        }

    }

    /**
     * 公共推送消息方法
     * @param bo 消息体
     */
    public void pushCommon(PushMessageBo bo){
        //netty版本发送
        if (enabled){
            MqttPublishMessage publishMessage = (MqttPublishMessage) MqttMessageFactory.newMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttPublishVariableHeader(bo.getTopic(), 0),
                    Unpooled.buffer().writeBytes(bo.getMessage().getBytes(StandardCharsets.UTF_8))
            );
            ClientManager.pubTopic(publishMessage);
        }else {
            pubMqttClient.publish(0,false,bo.getTopic(), bo.getMessage());
        }
    }
}
