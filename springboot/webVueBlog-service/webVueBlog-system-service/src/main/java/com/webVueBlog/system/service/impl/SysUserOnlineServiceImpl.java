package com.webVueBlog.system.service.impl;

import org.springframework.stereotype.Service;
import com.webVueBlog.common.core.domain.model.LoginUser;
import com.webVueBlog.common.utils.StringUtils;
import com.webVueBlog.system.domain.SysUserOnline;
import com.webVueBlog.system.service.ISysUserOnlineService;

/**
 * 在线用户 服务层处理
 * 
 *
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user)
    {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUser()))// 用户对象为空
        {
            return null;// 用户对象为空则返回空
        }
        SysUserOnline sysUserOnline = new SysUserOnline();// 在线用户信息
        sysUserOnline.setTokenId(user.getToken());// 用户登录地址
        sysUserOnline.setUserName(user.getUsername());// 用户账号
        sysUserOnline.setIpaddr(user.getIpaddr());// 用户登录IP地址
        sysUserOnline.setLoginLocation(user.getLoginLocation());// 用户登录地点
        sysUserOnline.setBrowser(user.getBrowser());// 浏览器类型
        sysUserOnline.setOs(user.getOs());// 操作系统
        sysUserOnline.setLoginTime(user.getLoginTime());// 登录时间
        if (StringUtils.isNotNull(user.getUser().getDept()))// 获取部门名称
        {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());// 部门名称
        }
        return sysUserOnline;// 在线用户信息
    }
}
