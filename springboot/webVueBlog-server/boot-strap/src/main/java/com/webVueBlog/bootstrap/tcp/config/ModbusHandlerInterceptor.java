package com.webVueBlog.bootstrap.tcp.config;

import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.base.core.HandlerInterceptor;
import com.webVueBlog.base.session.Session;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息拦截应答
 * 
 */
@Slf4j
public class ModbusHandlerInterceptor implements HandlerInterceptor<Message> {
    @Override
    public Message notSupported(Message request, Session session) {
        return null;
    }

    @Override
    public boolean beforeHandle(Message request, Session session) {
        int messageId = Integer.parseInt(request.getMessageId());
        if (!session.isRegistered()){
            log.warn("设备未注册,session={}",session);
            return true;
        }
        return false;
    }

    @Override
    public Message successful(Message request, Session session) {
        return null;
    }

    /**
     * 调用之后
     */
    @Override
    public void afterHandle(Message request, Message response, Session session) {
        if (response != null){
            //response.setSerialNo(session.nextSerialNO());
        }
    }

    @Override
    public Message exceptional(Message request, Session session, Exception e) {
        return null;
    }
}
