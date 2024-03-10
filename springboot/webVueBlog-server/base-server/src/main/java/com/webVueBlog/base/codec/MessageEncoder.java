package com.webVueBlog.base.codec;

import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.base.session.Session;
import io.netty.buffer.ByteBuf;

/**
 * 基础消息编码类
 *
 * 
 */
public interface MessageEncoder{

    ByteBuf encode(Message message, String clientId);
}
