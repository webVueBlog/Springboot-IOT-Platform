package com.webVueBlog.base.codec;


import com.webVueBlog.common.core.mq.DeviceReport;
import io.netty.buffer.ByteBuf;

/**
 * 基础消息解码类
 *
 * 
 */
public interface MessageDecoder {

    /**
     * TCP3.进站消息解码方法
     */
    DeviceReport decode(ByteBuf buf, String clientId);
}