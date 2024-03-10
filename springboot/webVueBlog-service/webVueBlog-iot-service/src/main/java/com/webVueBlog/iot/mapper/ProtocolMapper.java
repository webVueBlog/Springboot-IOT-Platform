package com.webVueBlog.iot.mapper;

import com.webVueBlog.iot.domain.Protocol;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 协议管理Mapper
 * 
 */
@Repository
public interface ProtocolMapper {


    /**
     * 查询协议
     *
     * @param id 协议主键
     * @return 协议
     */
    public Protocol selectProtocolById(Long id);

    /**
     * 查询协议列表
     *
     * @param protocol 协议
     * @return 协议集合
     */
    public List<Protocol> selectProtocolList(Protocol protocol);

    /**
     * 新增协议
     *
     * @param protocol 协议
     * @return 结果
     */
    public int insertProtocol(Protocol protocol);

    /**
     * 修改协议
     *
     * @param protocol 协议
     * @return 结果
     */
    public int updateProtocol(Protocol protocol);

    /**
     * 删除协议
     *
     * @param id 协议主键
     * @return 结果
     */
    public int deleteProtocolById(Long id);

    /**
     * 批量删除协议
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProtocolByIds(Long[] ids);

    /**
     * 获取所有唯一协议
     * @param protocol
     * @return
     */
   public List<Protocol> selectByUnion(Protocol protocol);

    /**
     * 获取所有可用协议
     * @param status
     * @param delFlag
     * @return
     */
   public List<Protocol> selectAll(@Param("status") Integer status, @Param("delFlag") Integer delFlag);
}
