package com.webVueBlog.mq.config;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.mq.redischannel.service.RedisPublishServiceImpl;
import com.webVueBlog.mq.service.IMessagePublishService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mq集群配置
 * 
 */
@Configuration
//是否开启集群，默认不开启
@ConditionalOnExpression("${cluster.enable:false}")
public class MqConfig {

    @Bean
    @ConditionalOnProperty(prefix ="cluster", name = "type" ,havingValue = DaConstant.MQTT.REDIS_CHANNEL,matchIfMissing = true)
    public IMessagePublishService redisChannelPublish(){
       return new RedisPublishServiceImpl();
    }

}
