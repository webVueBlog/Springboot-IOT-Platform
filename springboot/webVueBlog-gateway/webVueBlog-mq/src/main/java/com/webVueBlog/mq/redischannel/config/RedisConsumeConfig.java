package com.webVueBlog.mq.redischannel.config;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.mq.redischannel.consumer.RedisChannelConsume;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redisChannel配置
 * 
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConsumeConfig {

    @Bean
    @ConditionalOnProperty(prefix ="cluster", name = "type" ,havingValue = DaConstant.MQTT.REDIS_CHANNEL,matchIfMissing = true)
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener，配置不同的交换机
        container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.DEVICE_STATUS));
        container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.PROP_READ));
        container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.FUNCTION_INVOKE));
        /*推送消息不需要关联ClientId，只需要处理推送数据， 即使本地服务不存在该客户端*/
        //container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.PUBLISH));
        container.addMessageListener(listenerAdapter,new PatternTopic(DaConstant.CHANNEL.UPGRADE));
        //container.addMessageListener(listenerAdapter,new PatternTopic(DaConstant.CHANNEL.OTHER));
        //container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.PUBLISH_ACK));
        //container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.PUB_REC));
        //container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.PUB_REL));
        //container.addMessageListener(listenerAdapter, new PatternTopic(DaConstant.CHANNEL.PUB_COMP));
        return container;
    }

    /**配置消息监听类 默认监听方法onMessage*/
    @Bean
    @ConditionalOnProperty(prefix ="cluster", name = "type" ,havingValue = DaConstant.MQTT.REDIS_CHANNEL,matchIfMissing = true)
    MessageListenerAdapter listenerAdapter(RedisChannelConsume consume){
        return new MessageListenerAdapter(consume,"onMessage");
    }

    @Bean
    @ConditionalOnProperty(prefix ="cluster", name = "type" ,havingValue = DaConstant.MQTT.REDIS_CHANNEL,matchIfMissing = true)
    StringRedisTemplate template(RedisConnectionFactory connectionFactory){
        return new StringRedisTemplate(connectionFactory);
    }




}
