package com.webVueBlog.iot.tdengine.dao;

import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.domain.DeviceLog;
import com.webVueBlog.iot.model.DeviceStatistic;
import com.webVueBlog.iot.model.HistoryModel;
import com.webVueBlog.iot.model.MonitorModel;
import com.webVueBlog.iot.tdengine.service.model.TdLogDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @package com.webVueBlog.mysql.mysql.tdengine
 * 类名: DatabaseMapper
 */
@Repository
public interface TDDeviceLogDAO {

    /***
     * 创建数据库
     */
    int  createDB( String database);

    /***
     * 创建超级表
     */
    int  createSTable(String database);


    /***
     * 新增设备日志
     */
    int  save(@Param("database") String database,@Param("device") DeviceLog deviceLog);

    /**
     * 批量插入数据
     * @param database 数据库名
     * @param list list集合
     */
    int saveBatch(@Param("database") String database, @Param("data")TdLogDto data);

    /***
     * 设备属性数据总数
     */
    Long selectPropertyLogCount(@Param("database") String database,@Param("device")  Device device);

    /***
     * 设备功能数据总数
     */
    Long selectFunctionLogCount(@Param("database") String database,@Param("device")  Device device);

    /***
     * 设备事件数据总数
     */
    Long selectEventLogCount(@Param("database") String database,@Param("device")  Device device);

    /***
     * 设备监测数据总数
     */
    Long selectMonitorLogCount(@Param("database") String database,@Param("device")  Device device);

    /***
     * 监测数据列表
     */
    List<MonitorModel> selectMonitorList(@Param("database")  String database, @Param("device") DeviceLog deviceLog);

    /***
     * 日志列表
     */
    List<DeviceLog> selectDeviceLogList(@Param("database")  String database,@Param("device") DeviceLog deviceLog);

    /***
     * 根据设备ID删除设备日志
     */
    int deleteDeviceLogByDeviceNumber(@Param("database")String dbName,@Param("serialNumber") String serialNumber);

    /***
     * 历史数据列表
     */
    List<HistoryModel> selectHistoryList(@Param("database")  String database, @Param("device") DeviceLog deviceLog);

}
