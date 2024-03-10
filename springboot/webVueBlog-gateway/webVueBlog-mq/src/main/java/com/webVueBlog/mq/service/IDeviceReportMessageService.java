package com.webVueBlog.mq.service;

import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.protocol.base.protocol.IProtocol;

/**
 * 处理设备上报数据解析
 * 
 */
public interface IDeviceReportMessageService {

    /**
     * 处理设备主动上报数据
     * @param bo
     */
    public void parseReportMsg(DeviceReportBo bo);

    /**
     * 处理设备普通消息回调
     * @param bo
     */
    public void parseReplyMsg(DeviceReportBo bo);


    /**
     * 构建消息
     * @param bo
     */
    public Device buildReport(DeviceReportBo bo);


    /**
     * 根据产品id获取协议处理器
     */
    IProtocol selectedProtocol(Long productId);

    /**
     * 处理设备主动上报属性
     *
     * @param topicName
     * @param message
     */
    public void handlerReportMessage(DeviceReport message, String topicName);

}
