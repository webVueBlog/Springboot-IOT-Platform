package com.webVueBlog.iot.mapper;

import com.webVueBlog.iot.domain.VarTempSalve;
import com.webVueBlog.iot.model.varTemp.SlaveIdAndId;

import java.util.List;

/**
 * 变量模板设备从机Mapper接口
 * 
 * 
 */
public interface VarTempSalveMapper 
{
    /**
     * 查询变量模板设备从机
     * 
     * @param id 变量模板设备从机主键
     * @return 变量模板设备从机
     */
    public VarTempSalve selectVarTempSalveById(Long id);

    /**
     * 查询变量模板设备从机列表
     * 
     * @param varTempSalve 变量模板设备从机
     * @return 变量模板设备从机集合
     */
    public List<VarTempSalve> selectVarTempSalveList(VarTempSalve varTempSalve);

    /**
     * 新增变量模板设备从机
     * 
     * @param varTempSalve 变量模板设备从机
     * @return 结果
     */
    public int insertVarTempSalve(VarTempSalve varTempSalve);

    /**
     * 修改变量模板设备从机
     * 
     * @param varTempSalve 变量模板设备从机
     * @return 结果
     */
    public int updateVarTempSalve(VarTempSalve varTempSalve);

    /**
     * 删除变量模板设备从机
     * 
     * @param id 变量模板设备从机主键
     * @return 结果
     */
    public int deleteVarTempSalveById(Long id);

    /**
     * 批量删除变量模板设备从机
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVarTempSalveByIds(Long[] ids);

    /**
     * 通过采集点模板ID批量删除变量模板设备从机
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteVarTempSalveByTempIds(Long[] ids);

    /**
     * 根据产品id查询设备从机列表
     *
     * @param productId 产品id
     * @return 变量模板设备从机集合
     */
    public List<VarTempSalve> selectVarTempSalveListByProductId(Long productId);

    /**
     * 根据id批量查询数据
     */
    public List<SlaveIdAndId> selectByIds(List<Long> ids);
}
