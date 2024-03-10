package com.webVueBlog.mq.redischannel.consumer;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.mq.service.impl.DeviceOtherMsgHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 
 */
@Slf4j
@Component
public class DeviceOtherMsgConsumer {

    @Resource
    private DeviceOtherMsgHandler otherMsgHandler;

    @Async(DaConstant.TASK.DEVICE_OTHER_TASK)
    public void consume(DeviceReportBo bo){
        try {
             //处理emq订阅的非 property/post 属性上报的消息 ，因为其他消息量小，放在一起处理
            otherMsgHandler.messageHandler(bo);
        }catch (Exception e){
            log.error("=>设备其他消息处理出错",e);
        }
    }

}
