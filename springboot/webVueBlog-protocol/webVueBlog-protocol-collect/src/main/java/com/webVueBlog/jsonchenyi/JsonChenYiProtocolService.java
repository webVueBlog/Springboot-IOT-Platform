package com.webVueBlog.jsonchenyi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.webVueBlog.common.annotation.SysProtocol;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.message.DeviceData;
import com.webVueBlog.common.core.thingsModel.ThingsModelSimpleItem;
import com.webVueBlog.common.core.thingsModel.ThingsModelValuesInput;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.protocol.base.protocol.IProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@SysProtocol(name = "JSON-Data解析协议", protocolCode = DaConstant.PROTOCOL.JsonObject_ChenYi, description = "系统内置JSONObject解析协议")
public class JsonChenYiProtocolService implements IProtocol {

    /**
     * 解析json格式数据
     * 上报数据格式：
     */
    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        try {
            DeviceReport reportMessage = new DeviceReport();
            // bytep[] 转String
            String data = new String(deviceData.getData(), StandardCharsets.UTF_8);
            Map<String, Object> values = JSON.parseObject(data, Map.class);
            JSONArray array = (JSONArray) values.get("data");
            List<ThingsModelSimpleItem> result = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) array.get(0);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                ThingsModelSimpleItem item = new ThingsModelSimpleItem();
                item.setTs(DateUtils.getNowDate());
                item.setValue(entry.getValue() + "");
                item.setId(entry.getKey());
                result.add(item);
            }
            ThingsModelValuesInput valuesInput = new ThingsModelValuesInput();
            valuesInput.setThingsModelValueRemarkItem(result);
            reportMessage.setValuesInput(valuesInput);
            reportMessage.setClientId(clientId);
            reportMessage.setSerialNumber(clientId);
            return reportMessage;
        } catch (Exception e) {
            throw new ServiceException("数据解析异常" + e.getMessage());
        }
    }

    @Override
    public byte[] encode(DeviceData message, String clientId) {
        try {
            String msg = ((DeviceReport) message.getBody()).getBody().toString();
            return msg.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("=>指令编码异常,device={},data={}", message.getSerialNumber(),
                    message.getDownMessage().getBody());
            return null;
        }
    }
}
