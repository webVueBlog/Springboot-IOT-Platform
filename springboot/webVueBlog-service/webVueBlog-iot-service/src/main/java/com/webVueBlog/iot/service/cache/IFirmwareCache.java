package com.webVueBlog.iot.service.cache;

/**
 * 固件版本缓存
 * 
 */
public interface IFirmwareCache {

    /**
     * 固件版本缓存
     * @param firmwareId 固件ID
     * @param urlName 升级URL
     * @return
     */
    public void setFirmwareCache(Long firmwareId,String urlName);

    /**
     * 获取固件版本信息
     * @param firmwareId 固件ID
     * @return 升级URL
     */
    public String getFirmwareCache(Long firmwareId);

    /**
     * 移除固件版本
     * @param firmwareId 固件id
     */
    public void remove(Long firmwareId);
}
