package com.webVueBlog.mq.mqttClient;

import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.common.enums.ServerType;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.common.utils.gateway.mq.TopicsPost;
import com.webVueBlog.common.utils.gateway.mq.TopicsUtils;
import com.webVueBlog.mq.redischannel.producer.MessageProducer;
import com.webVueBlog.mq.service.IMessagePublishService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;


@Component
@Slf4j
public class MqttService {

    @Resource
    private TopicsUtils topicsUtils;
    @Resource
    private IMessagePublishService mqService;


    public void subscribe(MqttAsyncClient client) throws MqttException {
        TopicsPost allPost = topicsUtils.getAllPost();
        client.subscribe(allPost.getTopics(), allPost.getQos());
        log.info("mqtt监控主题,{}", Arrays.asList(allPost.getTopics()));
    }

    /**
     * 消息回调方法
     *
     * @param topic       主题
     * @param mqttMessage 消息体
     */
    public void subscribeCallback(String topic, MqttMessage mqttMessage) {

        String message = new String(mqttMessage.getPayload());
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + mqttMessage.getQos());
        log.info("接收消息内容 : " + message);
        /*
         * 从topic中获取信息
         * productId :  产品id
         * serialNumber :设备编号
         * mqttMessage.getPayload() 设备原始消息 byte[]类型
         *
         */
        String serialNumber = topicsUtils.parseSerialNumber(topic);
        Long productId = topicsUtils.parseProductId(topic);
        String name = topicsUtils.parseTopicName(topic);
        DeviceReportBo reportBo = DeviceReportBo.builder()
                .serialNumber(serialNumber)
                .productId(productId)
                .data(mqttMessage.getPayload())
                .platformDate(DateUtils.getNowDate())
                .topicName(topic)
                .serverType(ServerType.MQTT)
                .build();
        /*将mqtt的消息发送至MQ队列处理消息 ,减轻mqtt客户端消息压力*/
        if (name.startsWith("property")) {
            //属性数据量大，单独一个通道
            MessageProducer.sendPublishMsg(reportBo);
            //mqService.publish(reportBo, DaConstant.CHANNEL.PUBLISH);
        } else {
            MessageProducer.sendOtherMsg(reportBo);
            //mqService.publish(reportBo, DaConstant.CHANNEL.OTHER);
        }
    }


}
