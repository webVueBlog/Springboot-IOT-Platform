package com.webVueBlog.iot.mapper;

import java.util.List;
import com.webVueBlog.iot.domain.DeviceTemplate;
import org.springframework.stereotype.Repository;

/**
 * 设备采集点模板关联Mapper接口
 * 
 * 
 * 
 */
@Repository
public interface DeviceTemplateMapper 
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
     * 删除设备采集点模板关联
     * 
     * @param id 设备采集点模板关联主键
     * @return 结果
     */
    public int deleteDeviceTemplateById(Long id);

    /**
     * 批量删除设备采集点模板关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDeviceTemplateByIds(Long[] ids);

    /**
     * 根据产品id查询采集点模板
     * @param productId
     * @return
     */
    public DeviceTemplate selectDeviceTemplateByProduct(Long productId);
}
