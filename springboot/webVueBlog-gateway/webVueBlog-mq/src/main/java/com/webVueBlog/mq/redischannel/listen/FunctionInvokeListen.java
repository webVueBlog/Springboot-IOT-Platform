package com.webVueBlog.mq.redischannel.listen;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.MQSendMessageBo;
import com.webVueBlog.mq.redischannel.consumer.FunctionInvokeConsumer;
import com.webVueBlog.mq.redischannel.queue.FunctionInvokeQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 设备服务下发监听
 *
 * 
 */
@Slf4j
@Component
public class FunctionInvokeListen {

    @Autowired
    private FunctionInvokeConsumer functionInvokeConsumer;

    @Async(DaConstant.TASK.MESSAGE_CONSUME_TASK)
    public void listen() {
        while (true) {
            try {
                MQSendMessageBo sendBo = FunctionInvokeQueue.take();
                functionInvokeConsumer.handler(sendBo);
            } catch (Exception e) {
                log.error("=>下发服务消费异常", e);
            }
        }
    }
}
