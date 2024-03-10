package com.webVueBlog.mq.redischannel.consumer;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.MQSendMessageBo;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.service.IDeviceService;
import com.webVueBlog.mq.service.IMqttMessagePublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 指令(服务)下发处理类
 *
 * 
 */
@Slf4j
@Component
public class FunctionInvokeConsumer {


    @Autowired
    private IMqttMessagePublish functionSendService;
    @Autowired
    private IDeviceService deviceService;

    @Async(DaConstant.TASK.FUNCTION_INVOKE_TASK)
    public void handler(MQSendMessageBo bo) {
        try {
            Device device = deviceService.selectDeviceBySerialNumber(bo.getSerialNumber());
            Optional.ofNullable(device).orElseThrow(()->new ServiceException("服务下发的设备:["+bo.getSerialNumber()+"]不存在"));
            //处理数据下发
            functionSendService.funcSend(bo);
        } catch (Exception e) {
            log.error("=>服务下发异常", e);
        }
    }
}
