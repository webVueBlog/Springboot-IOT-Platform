package com.webVueBlog.mq.redischannel.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.common.core.mq.MQSendMessageBo;
import com.webVueBlog.common.core.mq.message.DeviceDownMessage;
import com.webVueBlog.common.core.mq.ota.OtaUpgradeBo;
import com.webVueBlog.mq.redischannel.queue.DevicePropFetchQueue;
import com.webVueBlog.mq.redischannel.queue.DeviceStatusQueue;
import com.webVueBlog.mq.redischannel.queue.FunctionInvokeQueue;
import com.webVueBlog.mq.redischannel.queue.OtaUpgradeQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * redisChannel消息监听
 *
 * 
 */
@Component
@Slf4j
public class RedisChannelConsume implements MessageListener {

    /**
     * 监听推送消息
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            /*获取channel*/
            String channel = new String(message.getChannel());
            /*获取消息*/
            String body = new String(message.getBody());
            switch (channel) {
                case DaConstant.CHANNEL.DEVICE_STATUS:
                    DeviceStatusBo statusBo = JSONObject.parseObject(body, DeviceStatusBo.class);
                    DeviceStatusQueue.offer(statusBo);
                    break;
                case DaConstant.CHANNEL.PROP_READ:
                    DeviceDownMessage downMessage = JSONObject.parseObject(body, DeviceDownMessage.class);
                    DevicePropFetchQueue.offer(downMessage);
                    break;
                case DaConstant.CHANNEL.FUNCTION_INVOKE:
                    MQSendMessageBo sendBo = JSONObject.parseObject(body, MQSendMessageBo.class);
                    FunctionInvokeQueue.offer(sendBo);
                    break;
                case DaConstant.CHANNEL.UPGRADE:
                    OtaUpgradeBo upgradeBo = JSONObject.parseObject(body, OtaUpgradeBo.class);
                    OtaUpgradeQueue.offer(upgradeBo);
                    break;
                    ///*推送消息本地服务处理，不全局发布*/
                //case DaConstant.CHANNEL.PUBLISH:
                //    DeviceReportBo reportBo = JSONObject.parseObject(body, DeviceReportBo.class);
                //    DeviceReportQueue.offer(reportBo);
                //    break;
                //case DaConstant.CHANNEL.OTHER:
                //    DeviceReportBo otherMsg = JSONObject.parseObject(body, DeviceReportBo.class);
                //    DeviceOtherQueue.offer(otherMsg);
                //    break;

                default:
                    log.error("=>未知消息类型,channel:[{}]", channel);
                    break;
            }
        } catch (Exception e) {
            log.error("=>redisChannel处理消息异常,e", e);
        }
    }
}
