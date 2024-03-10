package com.webVueBlog.mq.redischannel.consumer;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.mq.service.IDeviceReportMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 设备上报消息处理
 *
 * 
 */
@Slf4j
@Component
public class DeviceReportMsgConsumer {


    @Autowired
    private IDeviceReportMessageService reportMessageService;

    @Async(DaConstant.TASK.DEVICE_UP_MESSAGE_TASK)
    public void consume(DeviceReportBo bo) {
        try {
            //处理数据解析
            reportMessageService.parseReportMsg(bo);
        } catch (Exception e) {
            log.error("设备主动上报队列监听异常", e);
        }
    }


}
