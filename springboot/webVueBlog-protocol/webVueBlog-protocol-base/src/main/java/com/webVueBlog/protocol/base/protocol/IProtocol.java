package com.webVueBlog.protocol.base.protocol;


import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.message.DeviceData;

/**
 * 基础协议
 */
public interface IProtocol {

    DeviceReport decode(DeviceData data, String clientId);// 解码

    byte[] encode(DeviceData message, String clientId);// 编码

}
