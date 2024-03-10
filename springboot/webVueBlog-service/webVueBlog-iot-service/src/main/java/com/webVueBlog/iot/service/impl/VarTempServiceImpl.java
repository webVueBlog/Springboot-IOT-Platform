package com.webVueBlog.iot.service.impl;

import com.webVueBlog.common.core.iot.response.IdentityAndName;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.iot.domain.Product;
import com.webVueBlog.iot.domain.ThingsModel;
import com.webVueBlog.iot.domain.VarTemp;
import com.webVueBlog.iot.domain.VarTempSalve;
import com.webVueBlog.iot.mapper.ThingsModelTemplateMapper;
import com.webVueBlog.iot.mapper.VarTempMapper;
import com.webVueBlog.iot.mapper.VarTempSalveMapper;
import com.webVueBlog.iot.model.varTemp.DeviceSlavePoint;
import com.webVueBlog.iot.service.IThingsModelService;
import com.webVueBlog.iot.service.IVarTempSalveService;
import com.webVueBlog.iot.service.IVarTempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备采集变量模板Service业务层处理
 *
 */
@Service
public class VarTempServiceImpl implements IVarTempService {
    @Autowired
    private VarTempMapper varTempMapper;
    @Autowired
    private IVarTempSalveService salveService;
    @Autowired
    private IThingsModelService modelService;

    @Autowired
    private VarTempSalveMapper varTempSalveMapper;

    @Autowired
    private ThingsModelTemplateMapper thingsModelTemplateMapper;

    /**
     * 查询设备采集变量模板
     *
     * @param templateId 设备采集变量模板主键
     * @return 设备采集变量模板
     */
    @Override
    public VarTemp selectVarTempByTemplateId(Long templateId) {
        return varTempMapper.selectVarTempByTemplateId(templateId);
    }

    /**
     * 查询设备采集变量模板列表
     *
     * @param varTemp 设备采集变量模板
     * @return 设备采集变量模板
     */
    @Override
    public List<VarTemp> selectVarTempList(VarTemp varTemp) {
        return varTempMapper.selectVarTempList(varTemp);
    }

    /**
     * 新增设备采集变量模板
     *
     * @param varTemp 设备采集变量模板
     * @return 结果
     */
    @Override
    public int insertVarTemp(VarTemp varTemp) {
        varTemp.setCreateTime(DateUtils.getNowDate());
        varTempMapper.insertVarTemp(varTemp);
        return varTemp.getTemplateId().intValue();
    }

    /**
     * 修改设备采集变量模板
     *
     * @param varTemp 设备采集变量模板
     * @return 结果
     */
    @Override
    public int updateVarTemp(VarTemp varTemp) {
        varTemp.setUpdateTime(DateUtils.getNowDate());
        return varTempMapper.updateVarTemp(varTemp);
    }

    /**
     * 批量删除设备采集变量模板
     *
     * @param templateIds 需要删除的设备采集变量模板主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteVarTempByTemplateIds(Long[] templateIds) {
        if(templateIds.length>1){
            // 目前界面中都是单条删除，templateIds长度都是1
            return 0;
        }
        // 删除模板下的从机
        varTempSalveMapper.deleteVarTempSalveByTempIds(templateIds);
        // 删除模板下通用物模型
        thingsModelTemplateMapper.deleteThingsModelTemplateByVarTemplateId(templateIds[0]);
        return varTempMapper.deleteVarTempByTemplateIds(templateIds);
    }

    /**
     * 删除设备采集变量模板信息
     *
     * @param templateId 设备采集变量模板主键
     * @return 结果
     */
    @Override
    public int deleteVarTempByTemplateId(Long templateId) {
        return varTempMapper.deleteVarTempByTemplateId(templateId);
    }

    /**
     * 根据模板id，查询从机采集点集合
     *
     * @param templeId 模板id
     * @return 结果
     */
    @Override
    public List<DeviceSlavePoint> selectPointsByTempleId(Long productId,Long templeId) {
        List<DeviceSlavePoint> result = new ArrayList<>();
        VarTempSalve tempSalve = new VarTempSalve();
        tempSalve.setDeviceTempId(templeId);
        /**从机列表*/
        List<VarTempSalve> salveList = salveService.selectVarTempSalveList(tempSalve);
        ThingsModel model = new ThingsModel();
        model.setProductId(productId);
        Map<Integer, List<IdentityAndName>> listMap = modelService.selectThingsModelListCache(model);
        if (!CollectionUtils.isEmpty(salveList)) {
            for (VarTempSalve salve : salveList) {
                DeviceSlavePoint slavePoint = new DeviceSlavePoint();
                slavePoint.setSlaveId(salve.getSlaveAddr());
                List<IdentityAndName> list = listMap.get(salve.getSlaveAddr());
                slavePoint.setPointList(list);
                slavePoint.setPacketLength(salve.getPacketLength());
                result.add(slavePoint);
            }
        }
        return result;
    }

    /**
     * 根据产品id获取模板详情
     * @param productId 产品id
     * @return 模板详情
     */
    @Override
    public VarTemp selectVarTempByProductId(Long productId){
       return varTempMapper.selectVarTempByProductId(productId);
    }


    /**
     * 根据产品id和
     */
    @Override
    public Long selectByProductAndSlaveId(Long productId, Integer slaveId){
        return varTempMapper.selectByProductAndSlaveId(productId,slaveId);
    }
}
