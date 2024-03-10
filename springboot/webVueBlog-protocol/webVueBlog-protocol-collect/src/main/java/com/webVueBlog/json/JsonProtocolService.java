package com.webVueBlog.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.webVueBlog.common.annotation.SysProtocol;
import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.DeviceReport;
import com.webVueBlog.common.core.mq.message.DeviceData;
import com.webVueBlog.common.core.thingsModel.ThingsModelSimpleItem;
import com.webVueBlog.common.core.thingsModel.ThingsModelValuesInput;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.iot.model.ThingsModels.ValueItem;
import com.webVueBlog.protocol.base.protocol.IProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@SysProtocol(name = "JSONArray解析协议",protocolCode = DaConstant.PROTOCOL.JsonArray,description = "系统内置JSONArray解析协议")
public class JsonProtocolService implements IProtocol {

    /**
     * 解析json格式数据
     * 上报数据格式：  [{\"id\":\"switch\",\"value\":\"0\"},{\"id\":\"gear\",\"value\":\"0\"}]
     */
    @Override
    public DeviceReport decode(DeviceData deviceData, String clientId) {
        try {
            DeviceReport reportMessage = new DeviceReport();//上报数据
            // bytep[] 转String
            String data = new String(deviceData.getData(),StandardCharsets.UTF_8);//获取数据
            List<ThingsModelSimpleItem> values = JSON.parseArray(data, ThingsModelSimpleItem.class);
            //上报数据时间
            for (ThingsModelSimpleItem value : values) {
                value.setTs(DateUtils.getNowDate());//设置时间
            }
            ThingsModelValuesInput valuesInput = new ThingsModelValuesInput();//上报数据
            valuesInput.setThingsModelValueRemarkItem(values);//设置上报数据
            reportMessage.setValuesInput(valuesInput);//设置上报数据
            reportMessage.setClientId(clientId);//设置设备id
            reportMessage.setSerialNumber(clientId);//设置序列号
            return reportMessage;//返回上报数据
        }catch (Exception e){
            throw new ServiceException("数据解析异常"+e.getMessage());
        }
    }

    /**
     * 下发 [{"id":"switch","value":"0","remark":""}]
     * @param message
     * @param clientId
     * @return
     */
    @Override
    public byte[] encode(DeviceData message, String clientId) {
        try {
            JSONObject body = (JSONObject) message.getDownMessage().getBody();
            ValueItem valueItem = new ValueItem();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                valueItem.setId(entry.getKey());
                valueItem.setValue(entry.getValue()+"");
                valueItem.setRemark("");
            }
            String msg = "[" + JSONObject.toJSONString(valueItem) +"]";
            return msg.getBytes(StandardCharsets.UTF_8);
        }catch (Exception e){
            log.error("=>指令编码异常,device={},data={}",message.getSerialNumber(),
                    message.getDownMessage().getBody());
            return null;
        }
    }
}
