package com.webVueBlog.iot.tdengine.service.impl;

import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.domain.DeviceLog;
import com.webVueBlog.iot.model.DeviceStatistic;
import com.webVueBlog.iot.model.HistoryModel;
import com.webVueBlog.iot.tdengine.service.ILogService;
import com.webVueBlog.iot.model.MonitorModel;
import com.webVueBlog.iot.tdengine.config.TDengineConfig;
import com.webVueBlog.iot.tdengine.dao.TDDeviceLogDAO;
import com.webVueBlog.iot.tdengine.service.model.TdLogDto;
import com.webVueBlog.iot.util.SnowflakeIdWorker;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类名: TdengineLogServiceImpl
 * 描述: TDengine存储日志数据实现类
 */
public class TdengineLogServiceImpl implements ILogService {

    private ApplicationContext applicationContext;

    private TDDeviceLogDAO tdDeviceLogDAO;

    private TDengineConfig tDengineConfig;

    private SnowflakeIdWorker snowflakeIdWorker;

    private String dbName;

    public TdengineLogServiceImpl(TDengineConfig _tDengineConfig, TDDeviceLogDAO _tdDeviceLogDAO) {
        this.tdDeviceLogDAO = _tdDeviceLogDAO;
        this.tDengineConfig = _tDengineConfig;
        snowflakeIdWorker=new SnowflakeIdWorker(1);
        this.dbName=_tDengineConfig.getDbName();
    }

    /***
     * 新增设备日志
     * @return
     */
    @Override
    public int saveDeviceLog(DeviceLog deviceLog) {
        long logId = snowflakeIdWorker.nextId();
        deviceLog.setLogId(logId);
        return tdDeviceLogDAO.save(dbName,deviceLog);
    }

    /**
     * 批量保存日志
     */
    @Override
    public int saveBatch(TdLogDto dto){
        return tdDeviceLogDAO.saveBatch(dbName,dto);
    }

    /***
     * 设备属性、功能、事件和监测数据总数
     * @return
     */
    @Override
    public DeviceStatistic selectCategoryLogCount(Device device){
        DeviceStatistic statistic=new DeviceStatistic();
        Long property=tdDeviceLogDAO.selectPropertyLogCount(dbName,device);
        Long function=tdDeviceLogDAO.selectFunctionLogCount(dbName,device);
        Long event=tdDeviceLogDAO.selectEventLogCount(dbName,device);
        Long monitor=tdDeviceLogDAO.selectMonitorLogCount(dbName,device);
        statistic.setPropertyCount(property==null?0:property);
        statistic.setFunctionCount(function==null?0:function);
        statistic.setEventCount(event==null?0:event);
        statistic.setMonitorCount(monitor==null?0:monitor);
        return  statistic;
    }

    /***
     * 日志列表
     * @return
     */
    @Override
    public List<DeviceLog> selectDeviceLogList(DeviceLog deviceLog) {
        return tdDeviceLogDAO.selectDeviceLogList(dbName,deviceLog);
    }

    /***
     * 监测数据列表
     * @return
     */
    @Override
    public List<MonitorModel> selectMonitorList(DeviceLog deviceLog) {
        if(deviceLog.getIdentity()!=null){
            deviceLog.setIdentity("%"+deviceLog.getIdentity()+"%");
        }
        return tdDeviceLogDAO.selectMonitorList(dbName,deviceLog);
    }

    /***
     * 根据设备ID删除设备日志
     * @return
     */
    @Override
    public int deleteDeviceLogByDeviceNumber(String deviceNumber) {
        return tdDeviceLogDAO.deleteDeviceLogByDeviceNumber(dbName,deviceNumber);
    }

    /**
     * 查询历史数据
     * is_Montor=1 或 is_history=1
     */
    @Override
    public Map<String, List<HistoryModel>> selectHistoryList(DeviceLog deviceLog){
        List<HistoryModel> historyList = tdDeviceLogDAO.selectHistoryList(dbName,deviceLog);
        return historyList.stream().collect(Collectors.groupingBy(HistoryModel::getIdentity));
    }
}
