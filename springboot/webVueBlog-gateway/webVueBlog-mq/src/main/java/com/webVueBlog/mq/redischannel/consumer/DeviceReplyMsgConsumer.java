package com.webVueBlog.mq.redischannel.consumer;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.mq.service.IDeviceReportMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 设备消息回复
 *
 * 
 */
@Component
@Slf4j
public class DeviceReplyMsgConsumer {


    @Resource
    private IDeviceReportMessageService deviceReportMessageService;


    /*设备回调消息，统一用上报model*/
    @Async(DaConstant.TASK.DEVICE_REPLY_MESSAGE_TASK)
    public void consume(DeviceReportBo bo) {
        try {
            String topicName = bo.getTopicName();

            if (topicName.endsWith(DaConstant.TOPIC.MSG_REPLY)) {
                //普通设备回复消息
                deviceReportMessageService.parseReplyMsg(bo);
            } else if (topicName.endsWith(DaConstant.TOPIC.UPGRADE_REPLY) ||
                    topicName.endsWith(DaConstant.TOPIC.SUB_UPGRADE_REPLY)) {
            }
        } catch (Exception e) {
            log.error("=>设备回复消息消费异常", e);
        }
    }
}
