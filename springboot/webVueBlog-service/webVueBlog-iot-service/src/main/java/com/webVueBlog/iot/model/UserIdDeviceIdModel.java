package com.webVueBlog.iot.model;

/**
 * 用户ID和设备ID模型
 * 
 * 
 * 
 */
public class UserIdDeviceIdModel
{
    private Long userId;

    private Long deviceId;

    public UserIdDeviceIdModel(Long userId, Long deviceId){
        this.userId=userId;
        this.deviceId=deviceId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
}
