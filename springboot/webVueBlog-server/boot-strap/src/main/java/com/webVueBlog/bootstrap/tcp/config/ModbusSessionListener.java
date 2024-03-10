package com.webVueBlog.bootstrap.tcp.config;

import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.session.SessionListener;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.common.enums.DeviceStatus;
import com.webVueBlog.iot.service.IDeviceService;
import com.webVueBlog.mq.service.IMessagePublishService;

import com.webVueBlog.base.util.DeviceUtils;
import com.webVueBlog.mqtt.manager.MqttRemoteManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 */
@Slf4j
@Service
public class ModbusSessionListener implements SessionListener {

    @Resource
    private IMessagePublishService publishService;

    @Resource
    private IDeviceService deviceService;
    @Resource
    private MqttRemoteManager remoteManager;

    /** 客户端建立连接 */
    @Override
    public void sessionCreated(Session session) {

    }

    /** 客户端完成注册或鉴权 */
    @Override
    public void sessionRegistered(Session session) {
        DeviceStatusBo statusBo = DeviceUtils.buildStatusMsg(session.getChannel(), session.getClientId(),
                DeviceStatus.ONLINE, session.getIp());
        publishService.publish(statusBo, DaConstant.CHANNEL.DEVICE_STATUS);
        remoteManager.pushDeviceStatus(-1L,statusBo.getSerialNumber(),statusBo.getStatus());
        log.info("TCP客户端:[{}],注册上线",session.getClientId());
    }

    /** 客户端注销或离线 */
    @Override
    public void sessionDestroyed(Session session) {
        /*推送离线消息到mq处理*/
        DeviceStatusBo statusBo = DeviceUtils.buildStatusMsg(session.getChannel(),
                session.getClientId(), DeviceStatus.OFFLINE, session.getIp());
        publishService.publish(statusBo, DaConstant.CHANNEL.DEVICE_STATUS);
        remoteManager.pushDeviceStatus(-1L,statusBo.getSerialNumber(),statusBo.getStatus());
        log.info("TCP客户端:[{}],离线",session.getClientId());

    }
}
