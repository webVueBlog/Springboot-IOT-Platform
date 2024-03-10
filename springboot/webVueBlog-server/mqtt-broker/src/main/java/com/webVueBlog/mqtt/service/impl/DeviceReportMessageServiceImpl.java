package com.webVueBlog.mqtt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReplyBo;
import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.DeviceReportBo;
import com.webVueBlog.common.core.mq.message.DeviceData;
import com.webVueBlog.common.core.mq.message.SubDeviceMessage;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.core.redis.RedisKeyBuilder;
import com.webVueBlog.common.core.thingsModel.ThingsModelSimpleItem;
import com.webVueBlog.common.enums.FunctionReplyStatus;
import com.webVueBlog.common.enums.ServerType;
import com.webVueBlog.common.enums.ThingsModelType;
import com.webVueBlog.common.enums.TopicType;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.common.utils.gateway.mq.TopicsUtils;
import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.domain.DeviceLog;
import com.webVueBlog.iot.domain.FunctionLog;
import com.webVueBlog.iot.model.ThingsModels.ValueItem;
import com.webVueBlog.iot.service.IDeviceService;
import com.webVueBlog.iot.service.IFunctionLogService;
import com.webVueBlog.iot.service.IProductService;
import com.webVueBlog.mq.model.ReportDataBo;
import com.webVueBlog.mq.service.IDataHandler;
import com.webVueBlog.mq.service.IDeviceReportMessageService;
import com.webVueBlog.mqtt.manager.MqttRemoteManager;
import com.webVueBlog.mqtt.model.PushMessageBo;
import com.webVueBlog.protocol.base.protocol.IProtocol;
import com.webVueBlog.protocol.service.IProtocolManagerService;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 处理类 处理设备主动上报和设备回调信息
 *
 * 
 */
@Service
@Slf4j
public class DeviceReportMessageServiceImpl implements IDeviceReportMessageService {

    @Autowired
    private IDeviceService deviceService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IProtocolManagerService protocolManagerService;

    @Resource
    private TopicsUtils topicsUtils;
    @Resource
    private IDataHandler dataHandler;
    @Resource
    private MqttRemoteManager remoteManager;
    @Resource
    private RedisCache redisCache;
    @Resource
    private IFunctionLogService logService;

    /**
     * 处理设备主动上报数据
     */
    @Override
    public void parseReportMsg(DeviceReportBo bo) {
        switch (bo.getServerType()) {
            case MQTT:
                log.debug("=>MQ*收到设备主题[{}],消息:[{}]", bo.getTopicName(), bo.getData());
                //构建消息
                Device report = buildReport(bo);
                /*获取协议处理器*/
                IProtocol protocol = selectedProtocol(bo.getProductId());
                DeviceData data = DeviceData.builder()
                        .serialNumber(bo.getSerialNumber())
                        .topicName(bo.getTopicName())
                        .productId(report.getProductId())
                        .data(bo.getData())
                        .prop(bo.getProp())
                        .buf(Unpooled.wrappedBuffer(bo.getData()))
                        .build();
                /*根据协议解析后的数据*/
                DeviceReport reportMessage = protocol.decode(data, null);
                //设备回复数据处理
                if (reportMessage.getIsReply()){
                    reportMessage.setServerType(ServerType.MQTT);
                    handlerDeviceReply(reportMessage, bo.getTopicName());
                    return;
                }
                reportMessage.setSerialNumber(bo.getSerialNumber());
                reportMessage.setProductId(bo.getProductId());
                reportMessage.setPlatformDate(bo.getPlatformDate());
                reportMessage.setServerType(bo.getServerType());
                reportMessage.setUserId(report.getUserId());
                reportMessage.setUserName(report.getUserName());
                reportMessage.setDeviceName(report.getDeviceName());
                //处理网关设备上报数据
                processNoSub(reportMessage, bo.getTopicName());
                break;
            case TCP:
                log.debug("*MQ收到TCP推送消息[{}]", JSON.toJSON(bo.getValuesInput()));
                Device device = deviceService.selectDeviceBySerialNumber(bo.getSerialNumber());
                Optional.ofNullable(device).orElseThrow(() -> new ServiceException("设备不存在"));
                DeviceReport deviceReport = new DeviceReport();
                BeanUtils.copyProperties(bo,deviceReport);
                deviceReport.setProductId(device.getProductId());
                deviceReport.setValuesInput(bo.getValuesInput());
                deviceReport.setSlaveId(bo.getSlaveId());
                deviceReport.setSerialNumber(device.getSerialNumber());
                //设备回复数据处理
                if (bo.getIsReply()){
                    bo.setServerType(ServerType.TCP);
                    handlerDeviceReply(deviceReport,null);
                    return;
                }
                processNoSub(deviceReport, null);
                break;
        }
    }

    /**
     * 处理设备回调数据
     */
    @Override
    public void parseReplyMsg(DeviceReportBo bo) {
        log.debug("=>MQ*收到设备回调消息,[{}]", bo);
        buildReport(bo);
        //获取解析协议
        IProtocol protocol = selectedProtocol(bo.getProductId());
        DeviceData deviceSource = DeviceData.builder()
                .serialNumber(bo.getSerialNumber())
                .topicName(bo.getTopicName())
                .data(bo.getData())
                .build();
        //协议解析后的数据
        DeviceReport message = protocol.decode(deviceSource, null);
        //处理网关设备回复数据
        processNoSub(message, bo.getTopicName());
        //处理网关子设备回复数据
        processSub(message, bo.getTopicName());

    }


    /**
     * 构建消息
     *
     * @param bo
     */
    @Override
    public Device buildReport(DeviceReportBo bo) {
        // topic组成:  up/serialNumber/xxx  可自定义
        String serialNumber = topicsUtils.parseSerialNumber(bo.getTopicName());
        Device device = deviceService.selectDeviceBySerialNumber(serialNumber);
        Optional.ofNullable(device).orElseThrow(() -> new ServiceException("设备不存在"));
        //设置物模型
        String thingsModel = topicsUtils.getThingsModel(bo.getTopicName());
        ThingsModelType thingsModelType = ThingsModelType.getType(thingsModel);
        bo.setType(thingsModelType);
        //产品id
        bo.setProductId(device.getProductId());
        //设备编号
        bo.setSerialNumber(serialNumber);
        return device;
    }

    /**
     * 解析OTA升级回复消息,更新升级状态
     */
    private void otaUpgrade(DeviceReport message, String topicName) {


    }

    /**
     * 根据产品id获取协议处理器
     */
    @Override
    public IProtocol selectedProtocol(Long productId) {
        try {
            /* 查询产品获取协议编号*/
            String protocolCode = productService.getProtocolByProductId(productId);
            return protocolManagerService.getProtocolByProtocolCode(protocolCode);
        } catch (Exception e) {
            log.error("=>查询协议类型异常", e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 处理网关设备
     *
     * @param message
     * @param topicName
     */
    private void processNoSub(DeviceReport message, String topicName) {
        //处理设备上报数据
        handlerReportMessage(message, topicName);
        //处理设备回复数据
        //handlerDeviceReply(message, topicName);
    }

    /**
     * TODO-暂不使用 处理网关子设备
     *
     * @param message
     * @param topicName
     */
    private void processSub(DeviceReport message, String topicName) {
        if (!topicName.endsWith(DaConstant.TOPIC.SUB)) {
            return;
        }
        //网关子设备数据集合
        List<SubDeviceMessage> subDeviceMessages = message.getSubDeviceMessages();
        for (SubDeviceMessage item : subDeviceMessages) {
            //获取网关子设备信息
            Device device = deviceService.selectDeviceBySerialNumber(item.getSerialNumber());
            //拼接子设备的topic
            String subTopic = topicsUtils.topicSubDevice(topicName, device.getProductId(), device.getSerialNumber());
            //获取子设备的协议处理器
            IProtocol protocol = selectedProtocol(device.getProductId());
            DeviceData deviceSource = DeviceData
                    .builder()
                    .serialNumber(device.getSerialNumber())
                    .data(item.getData())
                    .topicName(subTopic)
                    .build();
            //子设备解析后数据
            DeviceReport subReportMessage = protocol.decode(deviceSource, null);
            // 子网关设备上报数据
            handlerReportMessage(subReportMessage, subTopic);
            //子网关设备回复数据处理
            handlerDeviceReply(subReportMessage, subTopic);
        }
    }

    /**
     * 处理设备主动上报属性
     *
     * @param topicName
     * @param message
     */
    @Override
    public void handlerReportMessage(DeviceReport message, String topicName) {

        if (message.getServerType().equals(ServerType.MQTT)){
            //处理topic以prop结尾上报的数据 (属性)
            if (message.getServerType().equals(ServerType.MQTT)) {
                if (!topicName.endsWith(TopicType.PROPERTY_POST.getTopicSuffix())
                        && !topicName.endsWith(TopicType.PROPERTY_POST_SIMULATE.getTopicSuffix())) {
                    return;
                }
            }
        }

        ReportDataBo report = new ReportDataBo();
        report.setSerialNumber(message.getSerialNumber());
        report.setProductId(message.getProductId());
        report.setDataList(message.getValuesInput().getThingsModelValueRemarkItem());
        report.setType(1);
        report.setSlaveId(message.getSlaveId());
        report.setUserId(message.getUserId());
        report.setUserName(message.getUserName());
        report.setDeviceName(message.getDeviceName());
        //属性上报执行规则引擎
        report.setRuleEngine(true);
        dataHandler.reportData(report);
    }



    /**
     * 处理设备回调信息，此处按照topic区分 prop上报和设备回调reply，
     * 如果模组可订阅的topic有限，不能区分prop上报和reply，自行根据上报数据来区分
     *
     * @param topicName
     * @param message
     */
    public void handlerDeviceReply(DeviceReport message, String topicName) {
        //发布设备回复消息，通知前端
        PushMessageBo pushMessageBo = new PushMessageBo();
        JSONObject jsonObject = new JSONObject();
        //处理mqtt设备的回复数据
        switch (message.getServerType()) {
            case MQTT:
                Long productId = topicsUtils.parseProductId(topicName);
                String serialNumber = topicsUtils.parseSerialNumber(topicName);
                pushMessageBo.setTopic(topicsUtils.buildTopic(productId, serialNumber, TopicType.SERVICE_INVOKE_REPLY));
                switch (message.getProtocolCode()) {
                    //银尔达开关设备回复处理
                    case DaConstant.PROTOCOL.YinErDa:
                        jsonObject.put("message", message.getReplyMessage());
                        pushMessageBo.setMessage(jsonObject.toJSONString());
                        remoteManager.pushCommon(pushMessageBo);
                        //更新设备值
                        String messageIdCacheKey = RedisKeyBuilder.buildDownMessageIdCacheKey(serialNumber);
                        DeviceReplyBo replyBo = redisCache.getCacheObject(messageIdCacheKey);
                        String cacheKey = RedisKeyBuilder.buildTSLVCacheKey(productId, serialNumber);
                        ValueItem valueItem = redisCache.getCacheMapValue(cacheKey, replyBo.getId());
                        valueItem.setShadow(replyBo.getValue());
                        valueItem.setValue(replyBo.getValue());
                        valueItem.setTs(DateUtils.getNowDate());
                        redisCache.setCacheMapValue(cacheKey, replyBo.getId(), com.alibaba.fastjson2.JSONObject.toJSONString(valueItem));
                        break;
                    //modbus-rtu设备回复数据处理
                    case DaConstant.PROTOCOL.ModbusRtu:
                        String mqttMsgCacheKey = RedisKeyBuilder.buildDownMessageIdCacheKey(message.getSerialNumber());
                        DeviceReplyBo mqttReply = redisCache.getCacheObject(mqttMsgCacheKey);
                        if (!Objects.isNull(mqttReply) && mqttReply.getId().equals(message.getAddress()+"")) {
                            FunctionLog functionLog = new FunctionLog();
                            functionLog.setResultCode(FunctionReplyStatus.SUCCESS.getCode());
                            functionLog.setResultMsg(FunctionReplyStatus.SUCCESS.getMessage());
                            functionLog.setReplyTime(DateUtils.getNowDate());
                            functionLog.setMessageId(mqttReply.getMessageId());
                            logService.updateByMessageId(functionLog);
                        }else {
                            break;
                        }
                        List<ThingsModelSimpleItem> result = new ArrayList<>();
                        ThingsModelSimpleItem item = new ThingsModelSimpleItem();
                        item.setId(message.getAddress()+"");
                        item.setSlaveId(message.getSlaveId());
                        item.setTs(DateUtils.getNowDate());
                        item.setValue(message.getReplyMessage());
                        item.setRemark(DateUtils.getTime());
                        result.add(item);
                        pushMessageBo.setMessage(JSON.toJSONString(result));
                        pushMessageBo.setTopic(topicsUtils.buildTopic(message.getProductId(), message.getSerialNumber(), TopicType.WS_SERVICE_INVOKE));
                        remoteManager.pushCommon(pushMessageBo);
                        String modbusCacheKey = RedisKeyBuilder.buildTSLVCacheKey(productId, serialNumber);
                        String hkey = message.getAddress() + "#" + message.getSlaveId();
                        String cacheMapValue = redisCache.getCacheMapValue(modbusCacheKey, hkey);
                        ValueItem modbusItem = com.alibaba.fastjson2.JSON.parseObject(cacheMapValue, ValueItem.class);
                        if (Objects.isNull(modbusItem)){
                            modbusItem = new ValueItem(message.getAddress()+"", modbusItem.getSlaveId(), null);
                        }
                        modbusItem.setShadow(message.getReplyMessage());
                        modbusItem.setValue(message.getReplyMessage());
                        modbusItem.setTs(DateUtils.getNowDate());
                        redisCache.setCacheMapValue(modbusCacheKey,hkey, com.alibaba.fastjson2.JSONObject.toJSONString(modbusItem));
                        break;
                }
                break;
            case TCP:
                switch (message.getProtocolCode()) {
                    //modbus-rtu设备回复数据处理
                    case DaConstant.PROTOCOL.ModbusRtu:
                        //更新指令下发日志
                        //更新设备值
                        String messageIdCacheKey = RedisKeyBuilder.buildDownMessageIdCacheKey(message.getSerialNumber());
                        DeviceReplyBo replyBo = redisCache.getCacheObject(messageIdCacheKey);
                        if (replyBo.getId().equals(message.getAddress()+"")) {
                            FunctionLog functionLog = new FunctionLog();
                            functionLog.setResultCode(FunctionReplyStatus.SUCCESS.getCode());
                            functionLog.setResultMsg(FunctionReplyStatus.SUCCESS.getMessage());
                            functionLog.setReplyTime(DateUtils.getNowDate());
                            functionLog.setMessageId(replyBo.getMessageId());
                            logService.updateByMessageId(functionLog);
                        }
                        List<ThingsModelSimpleItem> result = new ArrayList<>();
                        ThingsModelSimpleItem item = new ThingsModelSimpleItem();
                        item.setId(message.getAddress()+"");
                        item.setSlaveId(message.getSlaveId());
                        item.setTs(DateUtils.getNowDate());
                        item.setValue(message.getReplyMessage());
                        item.setRemark(DateUtils.getTime());
                        result.add(item);
                        pushMessageBo.setMessage(JSON.toJSONString(result));
                        pushMessageBo.setTopic(topicsUtils.buildTopic(message.getProductId(), message.getSerialNumber(), TopicType.WS_SERVICE_INVOKE));
                        remoteManager.pushCommon(pushMessageBo);
                        String modbusCacheKey = RedisKeyBuilder.buildTSLVCacheKey(message.getProductId(),  message.getSerialNumber());
                        String hkey = message.getAddress() + "#" + message.getSlaveId();
                        String cacheMapValue = redisCache.getCacheMapValue(modbusCacheKey, hkey);
                        ValueItem modbusItem = com.alibaba.fastjson2.JSON.parseObject(cacheMapValue, ValueItem.class);
                        if (Objects.isNull(modbusItem)){
                            modbusItem = new ValueItem(message.getAddress()+"", modbusItem.getSlaveId(), null);
                        }
                        modbusItem.setShadow(message.getReplyMessage());
                        modbusItem.setValue(message.getReplyMessage());
                        modbusItem.setTs(DateUtils.getNowDate());
                        redisCache.setCacheMapValue(modbusCacheKey,hkey, com.alibaba.fastjson2.JSONObject.toJSONString(modbusItem));
                        break;

                }
                break;

        }
    }

    /**
     * 组装数据存储TD
     *
     * @param message 回调数据
     * @return List<DeviceLog> 数据集合
     */
    private List<DeviceLog> processReportMsg(DeviceReport message) {
        return null;
    }


}
