package com.webVueBlog.iot.service;

import java.util.List;
import com.webVueBlog.iot.domain.DeviceTemplate;
import com.webVueBlog.iot.model.varTemp.DeviceSlavePoint;
import com.webVueBlog.iot.model.varTemp.DeviceTemp;

/**
 * 设备采集点模板关联Service接口
 * 
 * 
 * 
 */
public interface IDeviceTemplateService 
{
    /**
     * 查询设备采集点模板关联
     * 
     * @param id 设备采集点模板关联主键
     * @return 设备采集点模板关联
     */
    public DeviceTemplate selectDeviceTemplateById(Long id);

    /**
     * 查询设备采集点模板关联列表
     * 
     * @param deviceTemplate 设备采集点模板关联
     * @return 设备采集点模板关联集合
     */
    public List<DeviceTemplate> selectDeviceTemplateList(DeviceTemplate deviceTemplate);

    /**
     * 新增设备采集点模板关联
     * 
     * @param deviceTemplate 设备采集点模板关联
     * @return 结果
     */
    public int insertDeviceTemplate(DeviceTemplate deviceTemplate);

    /**
     * 修改设备采集点模板关联
     * 
     * @param deviceTemplate 设备采集点模板关联
     * @return 结果
     */
    public int updateDeviceTemplate(DeviceTemplate deviceTemplate);

    /**
     * 批量删除设备采集点模板关联
     * 
     * @param ids 需要删除的设备采集点模板关联主键集合
     * @return 结果
     */
    public int deleteDeviceTemplateByIds(Long[] ids);

    /**
     * 删除设备采集点模板关联信息
     * 
     * @param id 设备采集点模板关联主键
     * @return 结果
     */
    public int deleteDeviceTemplateById(Long id);

    /**
     * 根据产品id查询采集点数据
     * @return
     */
    public DeviceTemplate selectPointsByProductId(Long productId);

    /**
     * 根据产品id查询采集点模板
     * @param productId
     * @return
     */
    public DeviceTemplate selectDeviceTemplateByProduct(Long productId);
}
