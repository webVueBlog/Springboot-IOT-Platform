package com.webVueBlog.mq.redischannel.producer;

import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.common.core.mq.MQSendMessageBo;
import com.webVueBlog.common.core.mq.ota.OtaUpgradeBo;
import com.webVueBlog.common.core.mq.message.DeviceDownMessage;
import com.webVueBlog.mq.redischannel.queue.*;

/**
 *设备消息生产者 ,设备的消息发送通道
 * 
 */
public class MessageProducer {

    /*发送设备状态消息到队列*/
    public static void sendDeviceStatus(DeviceStatusBo bo){
        DeviceStatusQueue.offer(bo);
    }
    /*发送设备回调消息到队列*/
    public static void sendReplyMsg(DeviceReportBo bo){
        DeviceReplyQueue.offer(bo);
    }
    /*发送设备获取属性消息到队列*/
    public static void sendPropFetch(DeviceDownMessage bo){
        DevicePropFetchQueue.offer(bo);
    }
    /*发送设备服务下发消息到队列*/
    public static void sendFunctionInvoke(MQSendMessageBo bo){
        FunctionInvokeQueue.offer(bo);
    }
    /*发送设备上报消息到队列*/
    public static void sendPublishMsg(DeviceReportBo bo){
        DeviceReportQueue.offer(bo);
    }
    public static void sendOtherMsg(DeviceReportBo bo){
        DeviceOtherQueue.offer(bo);
    }
    /*OTA升级消息*/
    public static void sendOTA(OtaUpgradeBo bo){
        OtaUpgradeQueue.offer(bo);
    }


}
