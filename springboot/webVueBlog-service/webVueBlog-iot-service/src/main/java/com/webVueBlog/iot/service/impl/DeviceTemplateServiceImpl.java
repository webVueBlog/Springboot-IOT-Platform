package com.webVueBlog.iot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.webVueBlog.iot.domain.Device;
import com.webVueBlog.iot.model.varTemp.DeviceSlavePoint;
import com.webVueBlog.iot.model.varTemp.DeviceTemp;
import com.webVueBlog.iot.service.IDeviceService;
import com.webVueBlog.iot.service.IVarTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.webVueBlog.iot.mapper.DeviceTemplateMapper;
import com.webVueBlog.iot.domain.DeviceTemplate;
import com.webVueBlog.iot.service.IDeviceTemplateService;
import org.springframework.util.CollectionUtils;

/**
 * 设备采集点模板关联Service业务层处理
 *
 *
 * 
 */
@Service
public class DeviceTemplateServiceImpl implements IDeviceTemplateService {
    @Autowired
    private DeviceTemplateMapper deviceTemplateMapper;
    @Autowired
    private IVarTempService tempService;
    @Autowired
    private IDeviceService deviceService;

    /**
     * 查询设备采集点模板关联
     *
     * @param id 设备采集点模板关联主键
     * @return 设备采集点模板关联
     */
    @Override
    public DeviceTemplate selectDeviceTemplateById(Long id) {
        return deviceTemplateMapper.selectDeviceTemplateById(id);
    }

    /**
     * 查询设备采集点模板关联列表
     *
     * @param deviceTemplate 设备采集点模板关联
     * @return 设备采集点模板关联
     */
    @Override
    public List<DeviceTemplate> selectDeviceTemplateList(DeviceTemplate deviceTemplate) {
        return deviceTemplateMapper.selectDeviceTemplateList(deviceTemplate);
    }

    /**
     * 新增设备采集点模板关联
     *
     * @param deviceTemplate 设备采集点模板关联
     * @return 结果
     */
    @Override
    public int insertDeviceTemplate(DeviceTemplate deviceTemplate) {
        return deviceTemplateMapper.insertDeviceTemplate(deviceTemplate);
    }

    /**
     * 修改设备采集点模板关联
     *
     * @param deviceTemplate 设备采集点模板关联
     * @return 结果
     */
    @Override
    public int updateDeviceTemplate(DeviceTemplate deviceTemplate) {
        return deviceTemplateMapper.updateDeviceTemplate(deviceTemplate);
    }

    /**
     * 批量删除设备采集点模板关联
     *
     * @param ids 需要删除的设备采集点模板关联主键
     * @return 结果
     */
    @Override
    public int deleteDeviceTemplateByIds(Long[] ids) {
        return deviceTemplateMapper.deleteDeviceTemplateByIds(ids);
    }

    /**
     * 删除设备采集点模板关联信息
     *
     * @param id 设备采集点模板关联主键
     * @return 结果
     */
    @Override
    public int deleteDeviceTemplateById(Long id) {
        return deviceTemplateMapper.deleteDeviceTemplateById(id);
    }

    /**
     * 根据产品id查询采集点数据
     *
     * @return
     */
    @Override
    public DeviceTemplate selectPointsByProductId(Long productId) {
        DeviceTemplate template = this.selectDeviceTemplateByProduct(productId);
        /*根据采集点编号查询采集点模板数据*/
        List<DeviceSlavePoint> slavePointList = tempService.selectPointsByTempleId(template.getProductId(), template.getTemplateId());
        template.setPointList(slavePointList);
        return template;
    }

    /**
     * 根据产品id查询采集点模板
     *
     * @param productId
     * @return
     */
    @Override
    public DeviceTemplate selectDeviceTemplateByProduct(Long productId) {
        return deviceTemplateMapper.selectDeviceTemplateByProduct(productId);
    }
}
