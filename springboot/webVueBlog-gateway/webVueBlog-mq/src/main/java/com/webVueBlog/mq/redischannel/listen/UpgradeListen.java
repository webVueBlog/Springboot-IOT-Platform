package com.webVueBlog.mq.redischannel.listen;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.ota.OtaUpgradeBo;
import com.webVueBlog.mq.redischannel.queue.OtaUpgradeQueue;
import com.webVueBlog.mq.service.IMqttMessagePublish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * OTA升级消息监听
 *
 * 
 */
@Slf4j
@Service
public class UpgradeListen {

    @Autowired
    private IMqttMessagePublish functionSendService;

    @Async(DaConstant.TASK.MESSAGE_CONSUME_TASK)
    public void listen() {
        while (true) {
            try {
                /*获取队列中的OTA升级消息*/
                OtaUpgradeBo upgradeBo = OtaUpgradeQueue.take();
                // OTA升级处理
                functionSendService.upGradeOTA(upgradeBo);
            } catch (Exception e) {
                log.error("->OTA消息监听异常", e);
            }
        }
    }
}
