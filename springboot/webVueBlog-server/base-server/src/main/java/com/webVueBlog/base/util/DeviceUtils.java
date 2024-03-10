package com.webVueBlog.base.util;

import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.common.enums.DeviceStatus;
import com.webVueBlog.common.utils.DateUtils;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * 设备信息工具类
 * 
 */
public class DeviceUtils {




    /*构造返回MQ的设备状态model*/
    public static DeviceStatusBo buildStatusMsg(Channel channel, String clientId, DeviceStatus status, String ip){
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return DeviceStatusBo.builder()
                .serialNumber(clientId)
                .status(status)
                .ip(ip)
                .hostName(address.getHostName())
                .port(address.getPort())
                .timestamp(DateUtils.getNowDate()).build();
    }
}
