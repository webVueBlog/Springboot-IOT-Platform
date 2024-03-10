package com.webVueBlog.data.service.impl;

import com.webVueBlog.common.core.iot.response.IdentityAndName;
import com.webVueBlog.common.core.mq.message.DeviceDownMessage;
import com.webVueBlog.common.core.mq.message.InstructionsMessage;
import com.webVueBlog.common.core.mq.message.PropRead;
import com.webVueBlog.common.core.protocol.modbus.ModbusCode;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.core.redis.RedisKeyBuilder;
import com.webVueBlog.common.enums.ServerType;
import com.webVueBlog.common.enums.TopicType;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.common.utils.gateway.mq.TopicsUtils;
import com.webVueBlog.data.service.IPropGetService;
import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.domain.DeviceTemplate;
import com.webVueBlog.iot.model.varTemp.DeviceSlavePoint;
import com.webVueBlog.iot.service.IDeviceService;
import com.webVueBlog.iot.service.IDeviceTemplateService;
import com.webVueBlog.mq.redischannel.producer.MessageProducer;
import com.webVueBlog.mq.service.IMqttMessagePublish;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 属性读取
 *
 */
@Slf4j
@Service
public class PropGetServiceImpl implements IPropGetService {

    @Autowired
    private IDeviceTemplateService templateService;// 设备模板
    @Resource
    private IMqttMessagePublish sendService;// 消息发送
    @Resource
    private RedisCache redisCache;// redis缓存
    @Resource
    private IDeviceService deviceService;// 设备
    @Resource
    private TopicsUtils topicsUtils;// 主题工具类

    /**
     * 属性读取
     */
    @Override
    public void fetchProperty() {
        //查询在线的modbus设备
        List<Device> devices = deviceService.selectOnlineModbusDevices();
        //从redis中获取产品对应的modbus轮询指令，没有则组装
        for (Device device : devices) {
            Map<String, Object> maps = this.combine(device.getProductId(),device.getTransport());
            maps.forEach((slaveId,list) ->{
                //组装topic
                String topic = topicsUtils.buildTopic(device.getProductId(), device.getSerialNumber(), TopicType.FUNCTION_GET);
                if (null != device.getIsSimulate() && device.getIsSimulate() ==1) {
                    topic = topicsUtils.buildTopic(device.getProductId(), device.getSerialNumber(), TopicType.PROPERTY_GET_SIMULATE);
                }
                String subCode = device.getSerialNumber() +"_"+slaveId;
                DeviceDownMessage downMessage = new DeviceDownMessage((List<PropRead>) list,topic,subCode,device.getTransport());
                downMessage.setSerialNumber(device.getSerialNumber());
                MessageProducer.sendPropFetch(downMessage);
            });
        }

    }

    /**
     * 根据产品id匹配 modbus轮询指令
     */
    private Map<String, Object> combine(Long productId, String serverType) {
        Map<String, Object> result;
        String cacheKey = RedisKeyBuilder.buildModbusCacheKey(productId);
        result = redisCache.getCacheMap(cacheKey);
        if (StringUtils.isNotEmpty(result)) {
            return result;
        }
        DeviceTemplate template = templateService.selectPointsByProductId(productId);
        String slaveId = "";
        /*modbus指令集合*/
        for (DeviceSlavePoint slavePoint : template.getPointList()) {
            List<PropRead> instructionList = new ArrayList<>();
            slaveId = slavePoint.getSlaveId().toString();
            /*当前的寄存器地址值，用于标记批量读取的位置*/
            int currentAddr = 0;
            List<String> addressList = slavePoint.getPointList().stream().map(IdentityAndName::getId).collect(Collectors.toList());
            for (IdentityAndName model : slavePoint.getPointList()) {
                /*当前读取的起始寄存器*/
                int readAddr = Integer.parseInt(model.getId());
                if (readAddr < currentAddr) {
                    continue;
                }
                PropRead prop = new PropRead();
                prop.setAddress(readAddr);
                //批量读取的个数，默认32个
                if ((null != model.getQuantity() && model.getQuantity() > 1) ||
                        (null != model.getCode() && (model.getCode().equals("1")|| model.getCode().equals("2")))){
                    prop.setCount(model.getQuantity());
                }else {
                    int packetLength = 1;
                    while (true){
                        readAddr += 1;
                        int finalReadAddr = readAddr;
                        IdentityAndName val = slavePoint.getPointList().stream().filter(item -> item.getId().equals(finalReadAddr + "")).findFirst().orElse(null);
                        if (!addressList.contains(readAddr+"") || (null != val.getQuantity() && val.getQuantity() > 1)){
                            break;
                        }
                        packetLength++;
                    }
                    if (slavePoint.getPacketLength() < packetLength){
                        packetLength = slavePoint.getPacketLength();
                    }
                    prop.setCount(packetLength);
                }
                prop.setLength(5 + prop.getCount() * 2);
                prop.setSlaveId(slavePoint.getSlaveId());
                prop.setCode(model.getCode() != null ? ModbusCode.getInstance(Integer.parseInt(model.getCode())) : ModbusCode.Read03);
                /*组装下发指令*/
                DeviceDownMessage downMessage = new DeviceDownMessage();
                downMessage.setBody(prop);
                downMessage.setCode(prop.getCode());
                downMessage.setProtocolCode("MODBUS-RTU");
                downMessage.setServerType(ServerType.explain(serverType));
                InstructionsMessage instruction = sendService.buildMessage(downMessage, TopicType.FUNCTION_GET);
                String hexDump = ByteBufUtil.hexDump(instruction.getMessage());
                prop.setData(hexDump);
                instructionList.add(prop);
                if (null != model.getCode() && (model.getCode().equals("1")|| model.getCode().equals("2"))){
                    currentAddr = prop.getAddress() + 1;
                }else {
                    currentAddr = prop.getAddress() + prop.getCount();
                }
            }
            result.put(slaveId, instructionList);
        }
        redisCache.hashPutAllObj(cacheKey, result);
        return result;
    }

}
