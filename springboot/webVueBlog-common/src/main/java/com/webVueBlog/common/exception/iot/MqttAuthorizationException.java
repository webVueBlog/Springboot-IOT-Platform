package com.webVueBlog.common.exception.iot;

import com.webVueBlog.common.exception.GlobalException;
import lombok.NoArgsConstructor;

/**
 * mqtt客户端权限校验异常
 * 
 */
@NoArgsConstructor
public class MqttAuthorizationException extends GlobalException {

    public MqttAuthorizationException(String messageId){
        super(messageId);
    }
}
