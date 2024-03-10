package com.webVueBlog.mq.redischannel.listen;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.message.DeviceDownMessage;
import com.webVueBlog.mq.redischannel.consumer.DevicePropFetchConsumer;
import com.webVueBlog.mq.redischannel.queue.DevicePropFetchQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 设备属性获取(定时获取)监听
 *
 * 
 */
@Slf4j
@Component
public class DevicePropFetchListen {

    @Autowired
    private DevicePropFetchConsumer devicePropFetchConsumer;

    @Async(DaConstant.TASK.MESSAGE_CONSUME_TASK_FETCH)
    public void listen() {
        while (true) {
            try {
                DeviceDownMessage downMessage = DevicePropFetchQueue.take();
                devicePropFetchConsumer.consume(downMessage);
                Thread.sleep(200);
            } catch (Exception e) {
                log.error("=>设备属性获取异常", e);
            }
        }
    }
}
