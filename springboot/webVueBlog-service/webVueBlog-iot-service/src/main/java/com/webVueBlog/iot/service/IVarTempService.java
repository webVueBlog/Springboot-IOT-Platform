package com.webVueBlog.iot.service;

import java.util.List;

import com.webVueBlog.iot.domain.Product;
import com.webVueBlog.iot.domain.VarTemp;
import com.webVueBlog.iot.model.varTemp.DeviceSlavePoint;
import org.apache.ibatis.annotations.Param;

/**
 * 设备采集变量模板Service接口
 * 
 */
public interface IVarTempService 
{
    /**
     * 查询设备采集变量模板
     * 
     * @param templateId 设备采集变量模板主键
     * @return 设备采集变量模板
     */
    public VarTemp selectVarTempByTemplateId(Long templateId);

    /**
     * 查询设备采集变量模板列表
     * 
     * @param varTemp 设备采集变量模板
     * @return 设备采集变量模板集合
     */
    public List<VarTemp> selectVarTempList(VarTemp varTemp);

    /**
     * 新增设备采集变量模板
     * 
     * @param varTemp 设备采集变量模板
     * @return 结果
     */
    public int insertVarTemp(VarTemp varTemp);

    /**
     * 修改设备采集变量模板
     * 
     * @param varTemp 设备采集变量模板
     * @return 结果
     */
    public int updateVarTemp(VarTemp varTemp);

    /**
     * 批量删除设备采集变量模板
     * 
     * @param templateIds 需要删除的设备采集变量模板主键集合
     * @return 结果
     */
    public int deleteVarTempByTemplateIds(Long[] templateIds);

    /**
     * 删除设备采集变量模板信息
     * 
     * @param templateId 设备采集变量模板主键
     * @return 结果
     */
    public int deleteVarTempByTemplateId(Long templateId);

    /**
     * 根据模板id，查询从机采集点集合
     * @param templeId 模板id
     * @return 结果
     */
    public List<DeviceSlavePoint> selectPointsByTempleId(Long productId,Long templeId);

    /**
     * 根据产品id获取模板详情
     * @param productId 产品id
     * @return 模板详情
     */
    public VarTemp selectVarTempByProductId(Long productId);

    /**
     * 根据产品id和
     */
    public Long selectByProductAndSlaveId(Long productId, Integer slaveId);

}
