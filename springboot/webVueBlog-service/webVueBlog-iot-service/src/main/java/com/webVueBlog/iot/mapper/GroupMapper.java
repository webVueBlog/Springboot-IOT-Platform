package com.webVueBlog.iot.mapper;

import java.util.List;
import com.webVueBlog.iot.domain.Group;
import com.webVueBlog.iot.model.DeviceGroupInput;
import com.webVueBlog.iot.model.IdOutput;
import org.springframework.stereotype.Repository;

/**
 * 设备分组Mapper接口
 * 
 * 
 * 
 */
@Repository
public interface GroupMapper 
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
    public List<IdOutput> selectDeviceIdsByGroupId(Long groupId);

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
     * 分组下批量增加设备分组
     * @param input
     * @return
     */
    public int insertDeviceGroups(DeviceGroupInput input);

    /**
     * 修改设备分组
     * 
     * @param group 设备分组
     * @return 结果
     */
    public int updateGroup(Group group);

    /**
     * 删除设备分组
     * 
     * @param groupId 设备分组主键
     * @return 结果
     */
    public int deleteGroupByGroupId(Long groupId);

    /**
     * 批量删除分组
     * 
     * @param groupIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGroupByGroupIds(Long[] groupIds);

    /**
     * 批量删除设备分组
     * @param groupIds
     * @return
     */
    public int deleteDeviceGroupByGroupIds(Long[] groupIds);

}
