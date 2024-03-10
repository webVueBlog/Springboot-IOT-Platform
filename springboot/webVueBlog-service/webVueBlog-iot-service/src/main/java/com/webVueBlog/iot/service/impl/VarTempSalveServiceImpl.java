package com.webVueBlog.iot.service.impl;

import java.util.List;
import java.util.Objects;

import com.webVueBlog.common.exception.ServiceException;
import com.webVueBlog.common.utils.DateUtils;
import com.webVueBlog.iot.domain.ThingsModelTemplate;
import com.webVueBlog.iot.model.varTemp.SlaveIdAndId;
import com.webVueBlog.iot.service.IThingsModelTemplateService;
import org.springframework.stereotype.Service;
import com.webVueBlog.iot.mapper.VarTempSalveMapper;
import com.webVueBlog.iot.domain.VarTempSalve;
import com.webVueBlog.iot.service.IVarTempSalveService;

import javax.annotation.Resource;

/**
 * 变量模板设备从机Service业务层处理
 *
 */
@Service
public class VarTempSalveServiceImpl implements IVarTempSalveService
{
    @Resource
    private VarTempSalveMapper varTempSalveMapper;
    @Resource
    private IThingsModelTemplateService thingsModelTemplateService;

    /**
     * 查询变量模板设备从机
     *
     * @param id 变量模板设备从机主键
     * @return 变量模板设备从机
     */
    @Override
    public VarTempSalve selectVarTempSalveById(Long id)
    {
        return varTempSalveMapper.selectVarTempSalveById(id);
    }

    /**
     * 查询变量模板设备从机列表
     *
     * @param varTempSalve 变量模板设备从机
     * @return 变量模板设备从机
     */
    @Override
    public List<VarTempSalve> selectVarTempSalveList(VarTempSalve varTempSalve)
    {
        return varTempSalveMapper.selectVarTempSalveList(varTempSalve);
    }

    /**
     * 新增变量模板设备从机
     *
     * @param varTempSalve 变量模板设备从机
     * @return 结果
     */
    @Override
    public int insertVarTempSalve(VarTempSalve varTempSalve)
    {
        varTempSalve.setCreateTime(DateUtils.getNowDate());
        if (varTempSalve.getPollingMethod() == 0){
            //默认轮询长度为32
            if (varTempSalve.getPacketLength() == null || varTempSalve.getPacketLength() == 0){
                varTempSalve.setPacketLength(32);
            }
            //这里判断一下如果 寄存器的起始地址和终止地址，小于 每次轮询的长度，那么将轮询长度值改为 addr_end - addr_start
            Long addrStart = varTempSalve.getAddrStart();
            Long addrEnd = varTempSalve.getAddrEnd();
            if (addrEnd - addrStart < varTempSalve.getPacketLength()){
                varTempSalve.setPacketLength((int) (addrEnd - addrStart)+1);
            }
        }
        return varTempSalveMapper.insertVarTempSalve(varTempSalve);
    }

    /**
     * 修改变量模板设备从机
     *
     * @param varTempSalve 变量模板设备从机
     * @return 结果
     */
    @Override
    public int updateVarTempSalve(VarTempSalve varTempSalve)
    {
        //查询
        VarTempSalve oldTempSlave = this.selectVarTempSalveById(varTempSalve.getId());
        varTempSalve.setUpdateTime(DateUtils.getNowDate());
        if (varTempSalve.getPollingMethod() == 0){
            //这里判断一下如果 寄存器的起始地址和终止地址，小于 每次轮询的长度，那么将轮询长度值改为 addr_end - addr_start
            Long addrStart = varTempSalve.getAddrStart();
            Long addrEnd = varTempSalve.getAddrEnd();
            if (addrEnd - addrStart < varTempSalve.getPacketLength()){
                varTempSalve.setPacketLength((int) (addrEnd - addrStart)+1);
            }
        }
        VarTempSalve selectTemp = new VarTempSalve();
        selectTemp.setDeviceTempId(varTempSalve.getDeviceTempId());
        List<VarTempSalve> salveList = this.selectVarTempSalveList(selectTemp);
        //查询从机地址是否已经存在
        if (varTempSalve.getSlaveAddr() != null){
            for (VarTempSalve tempSalve : salveList) {
                if (Objects.equals(tempSalve.getSlaveAddr(), varTempSalve.getSlaveAddr()) && !Objects.equals(varTempSalve.getId(), tempSalve.getId())){
                    throw new ServiceException("从机编号:"+ varTempSalve.getSlaveAddr()+"已经存在!,设备名:"+ tempSalve.getSlaveName());
                }
            }
        }
        //更新物模型模板
        ThingsModelTemplate updateTemp = new ThingsModelTemplate();
        updateTemp.setOldTempSlaveId(oldTempSlave.getDeviceTempId() +"#"+oldTempSlave.getSlaveAddr());
        updateTemp.setTempSlaveId(oldTempSlave.getDeviceTempId() +"#"+varTempSalve.getSlaveAddr());
        thingsModelTemplateService.updateTemplateByTempSlaveId(updateTemp);
        return varTempSalveMapper.updateVarTempSalve(varTempSalve);
    }

    /**
     * 批量删除变量模板设备从机
     *
     * @param ids 需要删除的变量模板设备从机主键
     * @return 结果
     */
    @Override
    public int deleteVarTempSalveByIds(Long[] ids)
    {
        return varTempSalveMapper.deleteVarTempSalveByIds(ids);
    }

    /**
     * 删除变量模板设备从机信息
     *
     * @param id 变量模板设备从机主键
     * @return 结果
     */
    @Override
    public int deleteVarTempSalveById(Long id)
    {
        return varTempSalveMapper.deleteVarTempSalveById(id);
    }

    /**
     * 根据产品id查询设备从机列表
     *
     * @param productId 产品id
     * @return 变量模板设备从机集合
     */
    @Override
    public List<VarTempSalve> selectVarTempSalveListByProductId(Long productId){
        return varTempSalveMapper.selectVarTempSalveListByProductId(productId);
    }

    /**
     * 根据id批量查询数据
     */
    @Override
    public List<SlaveIdAndId> selectByIds(List<Long> ids){
        return varTempSalveMapper.selectByIds(ids);
    }
}
