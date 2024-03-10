package com.webVueBlog.mq.redischannel.queue;

import com.webVueBlog.common.core.mq.DeviceReportBo;
import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 设备消息回调队列 {@link DeviceReportBo}
 * 
 */
public class DeviceReplyQueue {
    private static final LinkedBlockingQueue<DeviceReportBo> queue = new LinkedBlockingQueue<>();

    /*元素加入队列,最后*/
    public static void offer(DeviceReportBo dto){
        queue.offer(dto);
    }
    /*取出队列元素 先进先出*/
    @SneakyThrows
    public static DeviceReportBo take(){
        return queue.take();
    }
}
