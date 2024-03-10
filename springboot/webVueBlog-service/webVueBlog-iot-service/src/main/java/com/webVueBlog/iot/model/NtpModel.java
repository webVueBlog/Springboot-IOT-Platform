package com.webVueBlog.iot.model;

/**
 * 产品分类的Id和名称输出
 * 
 * 
 * 
 */
public class NtpModel
{
    private Long deviceSendTime;// 设备发送时间

    private Long serverRecvTime;// 服务器接收时间

    private Long serverSendTime;// 服务器发送时间

    public Long getDeviceSendTime() {
        return deviceSendTime;
    }

    public void setDeviceSendTime(Long deviceSendTime) {
        this.deviceSendTime = deviceSendTime;
    }

    public Long getServerRecvTime() {
        return serverRecvTime;
    }

    public void setServerRecvTime(Long serverRecvTime) {
        this.serverRecvTime = serverRecvTime;
    }

    public Long getServerSendTime() {
        return serverSendTime;
    }

    public void setServerSendTime(Long serverSendTime) {
        this.serverSendTime = serverSendTime;
    }
}
