package com.webVueBlog.mq.service;

import com.webVueBlog.common.core.mq.InvokeReqDto;

import java.util.Map;

/**
 * 设备指令下发接口
 * 
 */
public interface IFunctionInvoke {

    /**
     * 服务调用，等待设备响应
     * @param reqDto 服务下发对象
     * @return 数据结果
     */
    public Map<String ,Object> invokeReply(InvokeReqDto reqDto);

    /**
     * 服务调用,设备不响应
     * @param reqDto 服务下发对象
     * @return 消息id messageId
     */
    public String invokeNoReply(InvokeReqDto reqDto);
}
