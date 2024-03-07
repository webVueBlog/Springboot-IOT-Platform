package com.webVueBlog.data.config;

import com.webVueBlog.common.core.mq.ota.OtaUpgradeDelayTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.DelayQueue;

/**
 * OTA延迟升级队列
 *
 */
@Slf4j
public class DelayUpgradeQueue {

    /**
     * 使用springboot的 DelayQueue 实现延迟队列(OTA对单个设备延迟升级，提高升级容错率)
     */
    private static DelayQueue<OtaUpgradeDelayTask> queue = new DelayQueue<>();// 延迟队列

    public static void offerTask(OtaUpgradeDelayTask task) {// 生产者生产任务
        try {
            queue.offer(task);// 添加任务到队列中
            log.info("=>OTA任务添加成功");
        } catch (Exception e) {
            log.error("OTA任务推送异常", e);
        }
    }

    public static OtaUpgradeDelayTask task() {// 消费者获取任务
        log.info("=>OTA任务获取");
        try {
            return queue.take();// 获取队列中的任务
        } catch (Exception exception) {
            log.error("=>OTA任务获取异常");
            return null;
        }
    }
}
