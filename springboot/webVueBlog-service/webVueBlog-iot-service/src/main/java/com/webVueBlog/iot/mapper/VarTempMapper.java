package com.webVueBlog.iot.mapper;

import java.util.List;

import com.webVueBlog.iot.domain.Product;
import com.webVueBlog.iot.domain.VarTemp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 设备采集变量模板Mapper接口
 * 
 * 
 */
@Repository
public interface VarTempMapper 
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
     * 删除设备采集变量模板
     * 
     * @param templateId 设备采集变量模板主键
     * @return 结果
     */
    public int deleteVarTempByTemplateId(Long templateId);

    /**
     * 批量删除设备采集变量模板
     * 
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVarTempByTemplateIds(Long[] templateIds);

    /**
     * 根据产品id获取模板详情
     * @param productId 产品id
     * @return 模板详情
     */
    public VarTemp selectVarTempByProductId(Long productId);

    /**
     * 根据产品id和
     */
    public Long selectByProductAndSlaveId(@Param("productId") Long productId, @Param("slaveId") Integer slaveId);
}
