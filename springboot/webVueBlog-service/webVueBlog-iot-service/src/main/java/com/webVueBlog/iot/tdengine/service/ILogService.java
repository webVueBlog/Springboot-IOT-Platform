package com.webVueBlog.iot.tdengine.service;

import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.domain.DeviceLog;

import com.webVueBlog.iot.model.DeviceStatistic;
import com.webVueBlog.iot.model.HistoryModel;
import com.webVueBlog.iot.model.MonitorModel;
import com.webVueBlog.iot.tdengine.service.model.TdLogDto;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @package iot.iot.log
 * 类名: LogService
 * 描述: 设备日志记录接口
 */
public interface ILogService {

    /** 保存设备日志 **/
    int saveDeviceLog(DeviceLog deviceLog);

    /**
     * 批量保存日志
     */
    int saveBatch(TdLogDto dto);

    /** 根据设备编号删除设备日志 **/
    int deleteDeviceLogByDeviceNumber(String deviceNumber);

    /** 设备属性、功能、事件总数 **/
    DeviceStatistic selectCategoryLogCount(Device device);

    /** 查询物模型日志列表 **/
    List<DeviceLog> selectDeviceLogList(DeviceLog deviceLog);

    /** 查询监测数据列表 **/
    List<MonitorModel> selectMonitorList(DeviceLog deviceLog);

    /**查询历史数据is_history=1*/
    Map<String, List<HistoryModel>> selectHistoryList(DeviceLog deviceLog);

}
