package com.webVueBlog.iot.tdengine.service.impl;

import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.domain.DeviceLog;
import com.webVueBlog.iot.model.DeviceStatistic;
import com.webVueBlog.iot.model.HistoryModel;
import com.webVueBlog.iot.tdengine.service.ILogService;
import com.webVueBlog.iot.mapper.DeviceLogMapper;
import com.webVueBlog.iot.model.MonitorModel;
import com.webVueBlog.iot.tdengine.service.model.TdLogDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类名: MySqlLogServiceImpl
 * 描述: MySQL存储日志实现类
 */
public class MySqlLogServiceImpl implements ILogService {

    private DeviceLogMapper deviceLogMapper;//日志mapper

    public MySqlLogServiceImpl(DeviceLogMapper _deviceLogMapper){
        this.deviceLogMapper=_deviceLogMapper;//注入日志mapper
    }

    /***
     * 新增设备日志
     * @return
     */
    @Override
    public int saveDeviceLog(DeviceLog deviceLog) {
        return deviceLogMapper.insertDeviceLog(deviceLog);//插入日志
    }

    @Override
    public int saveBatch(TdLogDto dto){
        return deviceLogMapper.saveBatch(dto.getList());//批量插入
    }

    /***
     * 根据设备ID删除设备日志
     * @return
     */
    @Override
    public int deleteDeviceLogByDeviceNumber(String deviceNumber) {//删除设备日志
        return deviceLogMapper.deleteDeviceLogByDeviceNumber(deviceNumber);//删除设备日志
    }

    /***
     * 设备属性、功能、事件和监测数据总数
     * @return
     */
    @Override
    public DeviceStatistic selectCategoryLogCount(Device device){
        return deviceLogMapper.selectCategoryLogCount(device);//查询设备日志总数
    }

    /***
     * 日志列表
     * @return
     */
    @Override
    public List<DeviceLog> selectDeviceLogList(DeviceLog deviceLog) {
        return deviceLogMapper.selectDeviceLogList(deviceLog);//查询设备日志列表
    }

    /***
     * 监测数据列表
     * @return
     */
    @Override
    public List<MonitorModel> selectMonitorList(DeviceLog deviceLog) {
        return deviceLogMapper.selectMonitorList(deviceLog);//查询监测数据列表
    }

    /**查询历史数据  is_Montor=1 或 is_history=1*/
    @Override
    public Map<String, List<HistoryModel>> selectHistoryList(DeviceLog deviceLog){
        List<HistoryModel> historyList = deviceLogMapper.selectHistoryList(deviceLog);//查询历史数据列表
        return historyList.stream().collect(Collectors.groupingBy(HistoryModel::getIdentity));//根据设备ID分组
    }
}
