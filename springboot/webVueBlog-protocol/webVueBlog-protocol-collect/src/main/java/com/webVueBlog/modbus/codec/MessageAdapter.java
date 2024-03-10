package com.webVueBlog.modbus.codec;

import com.webVueBlog.base.codec.MessageDecoder;
import com.webVueBlog.base.codec.MessageEncoder;
import com.webVueBlog.common.ProtocolColl;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.message.DeviceData;
import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.iot.model.ProductCode;
import com.webVueBlog.iot.service.IProductService;
import com.webVueBlog.protocol.base.protocol.IProtocol;
import com.webVueBlog.protocol.service.IProtocolManagerService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息编解码适配器
 *
 */
@Slf4j
@Component
public class MessageAdapter implements MessageDecoder, MessageEncoder{

    @Autowired
    private IProtocolManagerService managerService;
    @Autowired
    private IProductService productService;

    /**
     * modbus消息解码
     *
     * @param buf     原数据
     * @param  clientId 客户端id
     * @return 解析后bean
     */
    @Override
    public DeviceReport decode(ByteBuf buf, String clientId) {
        IProtocol protocol = null;
        String dump = null;
        String devNum = new String(ByteBufUtil.getBytes(buf));
        log.info("=>上报数据:{}", devNum);
        try {
            dump = ByteBufUtil.hexDump(buf);
            log.info("=>上报hex数据:{}", dump);
        }catch (Exception e){

        }
        //这里兼容一下TCP整包发送的数据(整包==设备编号，数据一起发送)
        if (clientId == null){
            if (devNum.contains(",") && !devNum.startsWith("{")&& !devNum.startsWith("]")){
                String[] split = devNum.split(",");
                devNum = split[1];
                DeviceReport report = new DeviceReport();
                report.setClientId(devNum);
                report.setSerialNumber(devNum);
                report.setMessageId("128");
                return report;
            }else {
                if (StringUtils.isNotEmpty(dump)) {
                    assert dump != null;
                    if (dump.startsWith("68") && dump.endsWith("16")) {
                        protocol = managerService.getProtocolByProtocolCode(DaConstant.PROTOCOL.FlowMeter);
                    }else if (dump.startsWith("7e")&& dump.endsWith("7e")){
                        protocol = managerService.getProtocolByProtocolCode(DaConstant.PROTOCOL.ModbusRtu);
                    }else if (devNum.startsWith("{") && devNum.endsWith("}")){
                        protocol = managerService.getProtocolByProtocolCode(DaConstant.PROTOCOL.JsonObject);
                    }else if (devNum.startsWith("[") && devNum.endsWith("]")){
                        protocol = managerService.getProtocolByProtocolCode(DaConstant.PROTOCOL.JsonArray);
                    }
                }
            }
        }else {
            ProtocolColl coll = selectedProtocol(clientId);
            protocol = coll.getProtocol();
        }
        DeviceData deviceData = DeviceData.builder()
                .buf(buf)
                .serialNumber(clientId)
                .data(ByteBufUtil.getBytes(buf))
                .build();
        return protocol.decode(deviceData, clientId);
    }


    /**
     * modbus消息编码
     *
     * @param message modbusbean
     * @return 编码指令
     */
    @Override
    public ByteBuf encode(Message message, String clientId) {
        ProtocolColl coll = selectedProtocol(clientId);
        IProtocol protocol = coll.getProtocol();
        DeviceData data = DeviceData.builder()
                .body(message)
                .type(2).build();
        byte[] out = protocol.encode(data, clientId);
        log.info("下发指令,clientId=[{}],指令=[{}]", clientId, ByteBufUtil.hexDump(out));
        return Unpooled.wrappedBuffer(out);
    }

    /**
     *  获取协议
     * @param serialNumber
     * @return
     */
    private ProtocolColl selectedProtocol(String serialNumber) {//获取协议
        ProtocolColl protocolColl = new ProtocolColl();//协议集合
        try {
            ProductCode protocol = productService.getProtocolBySerialNumber(serialNumber);//获取协议
            if (protocol == null || StringUtils.isEmpty(protocol.getProtocolCode())) {//协议为空
                protocol.setProtocolCode(DaConstant.PROTOCOL.ModbusRtu);//默认modbus
            }
            IProtocol code = managerService.getProtocolByProtocolCode(protocol.getProtocolCode());//获取协议
            protocolColl.setProtocol(code);//协议
            protocolColl.setProductId(protocol.getProductId());//产品id
            return protocolColl;//返回协议集合
        } catch (Exception e) {//异常
            throw new ServiceException(e.getMessage());//抛出异常
        }
    }
}
