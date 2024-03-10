package com.webVueBlog.mq.redischannel.listen;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.mq.redischannel.consumer.DeviceStatusConsumer;
import com.webVueBlog.mq.redischannel.queue.DeviceStatusQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 设备状态监听
 * 
 */
@Slf4j
@Component
public class DeviceStatusListen {

    @Autowired
    private DeviceStatusConsumer deviceStatusConsumer;

    @Async(DaConstant.TASK.MESSAGE_CONSUME_TASK)
    public void listen() {
        try {
            while (true) {
                DeviceStatusBo status = DeviceStatusQueue.take();
                deviceStatusConsumer.consume(status);
            }
        } catch (Exception e) {
            log.error("设备状态监听错误", e);
        }
    }

}
