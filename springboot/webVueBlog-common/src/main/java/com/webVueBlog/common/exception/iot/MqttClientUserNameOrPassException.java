package com.webVueBlog.common.exception.iot;

import com.webVueBlog.common.exception.GlobalException;
import lombok.NoArgsConstructor;

/**
 * mqtt客户端校验 用户名或密码错误
 * 
 */
@NoArgsConstructor
public class MqttClientUserNameOrPassException extends GlobalException {

    public MqttClientUserNameOrPassException(String message){
        super(message);
    }
}
