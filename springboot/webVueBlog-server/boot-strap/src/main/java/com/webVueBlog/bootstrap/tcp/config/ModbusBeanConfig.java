package com.webVueBlog.bootstrap.tcp.config;

import com.webVueBlog.protocol.WModelManager;
import com.webVueBlog.base.core.HandlerMapping;
import com.webVueBlog.base.core.SpringHandlerMapping;
import com.webVueBlog.base.session.SessionListener;
import com.webVueBlog.base.session.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 */
@Configuration
public class ModbusBeanConfig {

    @Bean
    public HandlerMapping handlerMapping(){
        return new SpringHandlerMapping();
    }

    @Bean
    public ModbusHandlerInterceptor handlerInterceptor(){
        return new ModbusHandlerInterceptor();
    }

    @Bean
    public SessionListener sessionListener(){
        return new ModbusSessionListener();
    }

    @Bean
    public SessionManager sessionManager(SessionListener sessionListener){
        return new SessionManager(sessionListener);
    }

    @Bean
    public WModelManager wModelManager(){
        return new WModelManager("com.webVueBlog.modbus");
    }


}
