package com.webVueBlog.gateway.boot.start;

import com.webVueBlog.mq.mqttClient.PubMqttClient;
import com.webVueBlog.mq.redischannel.listen.*;
import com.webVueBlog.protocol.service.IProtocolManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 启动类
 *
 */
@Component
@Slf4j
@Order(2)/** 启动顺序为2，数字越小，启动优先级越高 */
public class StartBoot implements ApplicationRunner {

    @Autowired
    private PubMqttClient mqttClient;/** mqtt客户端 */
    @Autowired
    private DeviceReplyListen replyListen;/** 设备回复监听 */
    @Autowired
    private DeviceReportListen reportListen;/** 设备上报监听 */
    @Autowired
    private DeviceStatusListen statusListen;/** 设备状态监听 */
    @Autowired
    private DevicePropFetchListen propFetchListen;/** 设备属性获取监听 */
    @Autowired
    private UpgradeListen upgradeListen;/** 设备升级监听 */
    @Autowired
    private FunctionInvokeListen invokeListen;/** 设备功能调用监听 */
    @Resource
    private DeviceOtherListen otherListen;/** 设备其他监听 */
    @Resource
    private IProtocolManagerService protocolManagerService;/** 协议管理服务 */


    @Override
    public void run(ApplicationArguments args) throws Exception {// 启动时执行
        try {
            replyListen.listen();
            reportListen.listen();
            statusListen.listen();
            propFetchListen.listen();
            upgradeListen.listen();
            invokeListen.listen();
            otherListen.listen();
            /*启动内部客户端,用来下发客户端服务*/
            mqttClient.initialize();
            protocolManagerService.getAllProtocols();// 获取所有的协议，包含脚本解析协议和系统内部定义协议
            log.info("=>设备监听队列启动成功");
        } catch (Exception e) {
            log.error("=>客户端启动失败:{}", e.getMessage(),e);
        }
    }
}
