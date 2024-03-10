package com.webVueBlog.iot.model;

/**
 * 设备分组对象 iot_device_group
 * 
 * 
 * 
 */
public class DeviceGroupInput
{
    private static final long serialVersionUID = 1L;

    /** 分组ID */
    private Long groupId;

    /** 设备ID */
    private Long[] deviceIds;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long[] getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(Long[] deviceIds) {
        this.deviceIds = deviceIds;
    }


}
