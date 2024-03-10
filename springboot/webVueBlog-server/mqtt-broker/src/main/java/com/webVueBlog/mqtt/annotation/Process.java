package com.webVueBlog.mqtt.annotation;

import io.netty.handler.codec.mqtt.MqttMessageType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注mqtt消息处理
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/*自动注入bean*/
@Component
public @interface Process {


    /*消息类型*/
    MqttMessageType type() default MqttMessageType.PUBLISH;
}
