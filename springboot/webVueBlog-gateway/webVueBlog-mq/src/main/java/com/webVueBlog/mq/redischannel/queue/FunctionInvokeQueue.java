package com.webVueBlog.mq.redischannel.queue;

import com.webVueBlog.common.core.mq.MQSendMessageBo;
import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 服务下发队列 处理{@link MQSendMessageBo}
 * 
 */
public class FunctionInvokeQueue {
    private static final LinkedBlockingQueue<MQSendMessageBo> queue = new LinkedBlockingQueue<>();

    /*元素加入队列,最后*/
    public static void offer(MQSendMessageBo dto){
        queue.offer(dto);
    }
    /*取出队列元素 先进先出*/
    @SneakyThrows
    public static MQSendMessageBo take(){
        return queue.take();
    }
}
