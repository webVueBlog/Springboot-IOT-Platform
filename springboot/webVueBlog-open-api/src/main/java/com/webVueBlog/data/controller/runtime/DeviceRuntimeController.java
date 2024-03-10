package com.webVueBlog.data.controller.runtime;

import com.alibaba.fastjson2.JSONObject;
import com.webVueBlog.common.core.controller.BaseController;
import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.common.core.mq.InvokeReqDto;
import com.webVueBlog.common.core.page.TableDataInfo;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.enums.ThingsModelType;
import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.data.service.IPropGetService;
import com.webVueBlog.iot.domain.DeviceLog;
import com.webVueBlog.iot.domain.FunctionLog;
import com.webVueBlog.iot.service.IDeviceRuntimeService;
import com.webVueBlog.mq.service.IFunctionInvoke;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 设备运行时数据controller
 *
 */
@RestController
@RequestMapping("/iot/runtime")
@Api(tags = "设备运行数据")
public class DeviceRuntimeController extends BaseController {

    @Autowired
    private IFunctionInvoke functionInvoke;
    @Autowired
    private IPropGetService propGetService;
    @Resource
    private IDeviceRuntimeService runtimeService;
    @Resource
    private RedisCache redisCache;

    /**
     * 服务下发返回回执
     */
    @PostMapping(value = "/service/invokeReply")
    //@PreAuthorize("@ss.hasPermi('iot:service:invokereply')")
    @ApiOperation(value = "服务下发返回回执", httpMethod = "POST", response = AjaxResult.class, notes = "服务下发并返回回执")
    public AjaxResult invokeReply(@Valid @RequestBody InvokeReqDto reqDto) {
        reqDto.setValue(new JSONObject(reqDto.getRemoteCommand()));
        Map<String, Object> result = functionInvoke.invokeReply(reqDto);
        return AjaxResult.success(result);
    }

    /**
     * 服务下发
     * 例如modbus 格式如下
     *
     * @see InvokeReqDto#getRemoteCommand()
     * key = 寄存器地址
     * value = 寄存器地址值
     * <p>
     * 其他协议 key = identifier
     * value = 值
     * {
     * "serialNumber": "860061060282358",
     * "productId": "2",
     * "identifier": "temp",
     * "remoteCommand": {
     * "4": "4"
     * }
     * }
     */
    @PostMapping("/service/invoke")
    @PreAuthorize("@ss.hasPermi('iot:service:invoke')")
    @ApiOperation(value = "服务下发", httpMethod = "POST", response = AjaxResult.class, notes = "服务下发")
    public AjaxResult invoke(@Valid @RequestBody InvokeReqDto reqDto) {
        reqDto.setValue(new JSONObject(reqDto.getRemoteCommand()));
        String messageId = functionInvoke.invokeNoReply(reqDto);
        return AjaxResult.success(messageId);
    }


    /**
     * 根据messageId查询服务回执
     */
    @GetMapping(value = "fun/get")
    //@PreAuthorize("@ss.hasPermi('iot:service:get')")
    @ApiOperation(value = "根据messageId查询服务回执", httpMethod = "GET", response = AjaxResult.class, notes = "根据messageId查询服务回执")
    public AjaxResult reply(String serialNumber, String messageId) {
        if (StringUtils.isEmpty(messageId) || StringUtils.isEmpty(serialNumber)) {
            throw new ServiceException("消息id为空");
        }
        // TODO - 根据消息id查询
        //DeviceTdReq req = new DeviceTdReq();
        //req.setImei(serialNumber);
        //req.setMessageId(messageId);
        //DeviceTdData data = deviceTdService.selectReplyMsg(req);
        //return toAjax(data)
        return AjaxResult.success();
    }

    @GetMapping(value = "prop/get")
    @ApiOperation(value = "属性读取", httpMethod = "GET", response = AjaxResult.class, notes = "属性读取")
    public AjaxResult propertyGet() {
        propGetService.fetchProperty();
        return AjaxResult.success();
    }

    /**
     * 实时状态
     * @param serialNumber 设备类型
     * @param type 物模型类型
     * @return 结果
     */
    @GetMapping(value = "/runState")
    @ApiOperation(value = "实时状态")
    public AjaxResult runState(String serialNumber, Integer type,Long productId,Integer slaveId){
        ThingsModelType modelType = ThingsModelType.getType(type);
        List<DeviceLog> logList = runtimeService.runtimeBySerialNumber(serialNumber, modelType,productId,slaveId);
        return AjaxResult.success(logList);
    }

    /**
     * 设备服务下发日志
     */
    @GetMapping(value = "/funcLog")
    @ApiOperation(value = "设备服务下发日志")
    public TableDataInfo funcLog(String serialNumber){
        startPage();
        List<FunctionLog> logList = runtimeService.runtimeReply(serialNumber);
        return getDataTable(logList);
    }



}
