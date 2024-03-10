package com.webVueBlog.iot.service;

import com.webVueBlog.iot.domain.Group;
import com.webVueBlog.iot.model.DeviceGroupInput;

import java.util.List;

/**
 * 设备分组Service接口
 * 
 * 
 * 
 */
public interface IGroupService 
{
    /**
     * 查询设备分组
     * 
     * @param groupId 设备分组主键
     * @return 设备分组
     */
    public Group selectGroupByGroupId(Long groupId);

    /**
     * 通过分组ID查询关联的设备ID数组
     * @param groupId
     * @return
     */
    public Long[] selectDeviceIdsByGroupId(Long groupId);

    /**
     * 查询设备分组列表
     * 
     * @param group 设备分组
     * @return 设备分组集合
     */
    public List<Group> selectGroupList(Group group);

    /**
     * 新增设备分组
     * 
     * @param group 设备分组
     * @return 结果
     */
    public int insertGroup(Group group);

    /**
     * 分组下批量添加设备分组
     * @return
     */
    public int updateDeviceGroups(DeviceGroupInput input);

    /**
     * 修改设备分组
     * 
     * @param group 设备分组
     * @return 结果
     */
    public int updateGroup(Group group);

    /**
     * 批量删除分组
     * 
     * @param groupIds 需要删除的设备分组主键集合
     * @return 结果
     */
    public int deleteGroupByGroupIds(Long[] groupIds);

    /**
     * 删除分组信息
     * 
     * @param groupId 设备分组主键
     * @return 结果
     */
    public int deleteGroupByGroupId(Long groupId);
}
