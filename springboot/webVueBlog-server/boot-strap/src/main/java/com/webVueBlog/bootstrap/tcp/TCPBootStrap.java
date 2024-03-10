package com.webVueBlog.bootstrap.tcp;

import com.webVueBlog.bootstrap.tcp.config.ModbusHandlerInterceptor;
import com.webVueBlog.common.enums.ServerType;
import com.webVueBlog.modbus.codec.MessageAdapter;
import com.webVueBlog.server.Server;
import com.webVueBlog.base.codec.Delimiter;
import com.webVueBlog.server.config.NettyConfig;
import com.webVueBlog.base.core.HandlerMapping;
import com.webVueBlog.base.session.SessionManager;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * TCP服务端启动
 * 
 */
@Order(11)
@Configuration
@ConditionalOnProperty(value = "server.tcp.enabled", havingValue = "true")//设置是否启动
@ConfigurationProperties(value = "server.tcp")
@Data
public class TCPBootStrap {

    private int port;
    private int keepAlive;
    private byte delimiter;
    @Autowired
    private MessageAdapter messageAdapter;
    private HandlerMapping handlerMapping;
    private ModbusHandlerInterceptor handlerInterpolator;
    private SessionManager sessionManager;

    public TCPBootStrap( HandlerMapping handlerMapping, ModbusHandlerInterceptor interpolator, SessionManager sessionManager){
        this.handlerMapping = handlerMapping;
        this.handlerInterpolator = interpolator;
        this.sessionManager = sessionManager;
    }

    @Bean(initMethod = "start",destroyMethod = "stop")
    public Server TCPServer(){
        return NettyConfig.custom()
                .setIdleStateTime(keepAlive,0,0)
                .setPort(port)
                 //设置报文最大长度  modbus-rtu
                .setMaxFrameLength(100)
                .setDelimiters(new Delimiter(new byte[]{0x7e}, true))
                .setDecoder(messageAdapter)
                .setEncoder(messageAdapter)
                .setHandlerMapping(handlerMapping)
                .setHandlerInterceptor(handlerInterpolator)
                .setSessionManager(sessionManager)
                .setName(ServerType.TCP.getDes())
                .setType(ServerType.TCP)
                .build();
    }
}