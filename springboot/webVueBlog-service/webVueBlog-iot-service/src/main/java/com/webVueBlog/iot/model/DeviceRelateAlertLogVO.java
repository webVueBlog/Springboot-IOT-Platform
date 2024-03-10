package com.webVueBlog.iot.model;

import lombok.Data;

/**
 * 
 */
@Data
public class DeviceRelateAlertLogVO {

    /**
     * 设备ID
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String serialNumber;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 用户ID
     */
    private Long userId;
}
