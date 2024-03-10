package com.webVueBlog.bootstrap.udp;

import com.webVueBlog.bootstrap.tcp.config.ModbusHandlerInterceptor;
import com.webVueBlog.common.enums.ServerType;
import com.webVueBlog.modbus.codec.MessageAdapter;
import com.webVueBlog.server.Server;
import com.webVueBlog.server.config.NettyConfig;
import com.webVueBlog.base.core.HandlerMapping;
import com.webVueBlog.base.session.SessionManager;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * UDP服务端启动
 * 
 */
@Order(12)
@Configuration
@ConditionalOnProperty(value = "server.udp.enabled", havingValue = "true")//设置是否启动
@ConfigurationProperties(value = "server.udp")
@Data
public class UDPBootStrap {

    private int port;
    private byte delimiter;

    private MessageAdapter messageAdapter;
    private HandlerMapping handlerMapping;
    private ModbusHandlerInterceptor handlerInterpolator;
    private SessionManager sessionManager;

    public UDPBootStrap(MessageAdapter messageAdapter, HandlerMapping handlerMapping, ModbusHandlerInterceptor interpolator, SessionManager sessionManager){
        this.messageAdapter = messageAdapter;
        this.handlerMapping = handlerMapping;
        this.handlerInterpolator = interpolator;
        this.sessionManager = sessionManager;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public Server UDPServer(){
        return NettyConfig.custom()
                .setPort(port)
                //.setDelimiters(new Delimiter(new byte[]{0x0D},false)) //分隔符配置
                .setDecoder(messageAdapter)
                .setEncoder(messageAdapter)
                .setHandlerMapping(handlerMapping)
                .setHandlerInterceptor(handlerInterpolator)
                .setSessionManager(sessionManager)
                .setName(ServerType.UDP.getDes())
                .setType(ServerType.UDP)
                .build();
    }

}
