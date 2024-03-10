package com.webVueBlog.mq.service.impl;

import com.webVueBlog.base.session.Session;
import com.webVueBlog.base.session.SessionManager;
import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 
 */
@Component
@Slf4j
public class MessageManager {

    private static final Mono<Void> NEVER = Mono.never();
    private static final Mono OFFLINE_EXCEPTION = Mono.error( new ServiceException("离线的客户端",4000));
    private static final Mono OFFLINE_RESULT = Mono.just(new AjaxResult(4000, "离线的客户端"));
    private static final Mono SEND_FAIL_RESULT = Mono.just(new AjaxResult(4001, "消息发送失败"));

    private SessionManager sessionManager;

    public MessageManager(SessionManager sessionManager){
        this.sessionManager = sessionManager;
    }

    public <T> Mono<AjaxResult> requestR(String sessionId, Message request, Class<T> responseClass){
        Session session = sessionManager.getSession(sessionId);
        if (session == null){
            return OFFLINE_RESULT;
        }
        return session.request(request,responseClass)
                .map(message -> AjaxResult.success(message))
                .onErrorResume(e ->{
                    log.warn("消息发送失败:{}",e);
                    return SEND_FAIL_RESULT;
                });
    }


}
