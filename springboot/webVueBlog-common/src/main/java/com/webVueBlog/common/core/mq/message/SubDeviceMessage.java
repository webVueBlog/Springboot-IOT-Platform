package com.webVueBlog.common.core.mq.message;

import lombok.Data;

/**
 * 网关子设备model
 * 
 */
@Data
public class SubDeviceMessage {
    /*子设备编号或编码*/
    private String serialNumber;
    /*数据*/
    private byte[] data;
    /*消息id*/
    private String messageId;
}
