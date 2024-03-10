package com.webVueBlog.iot.service.cache;

import com.webVueBlog.common.core.mq.DeviceStatusBo;
import com.webVueBlog.common.enums.DeviceStatus;
import com.webVueBlog.iot.domain.Device;

import java.util.List;

/**
 * 设备缓存
 * 
 */
public interface IDeviceCache {

    /**
     * 更新设备状态
     * @param dto dto
     */
    public Device updateDeviceStatusCache(DeviceStatusBo dto);

    /**
     * 获取设备在线总数
     * @return 设备在线总数
     */
    public long deviceOnlineTotal();


    /**
     * 批量更新redis缓存设备状态
     * @param serialNumbers 设备列表
     * @param status 状态
     */
    void updateBatchDeviceStatusCache(List<String> serialNumbers, DeviceStatus status);

    /**
     * 移除过期的设备
     */
    public List<String> removeExpiredDevice();

}
