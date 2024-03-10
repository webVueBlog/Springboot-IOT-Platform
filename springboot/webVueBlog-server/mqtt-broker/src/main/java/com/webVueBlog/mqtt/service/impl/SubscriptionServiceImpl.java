package com.webVueBlog.mqtt.service.impl;

import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.mqtt.model.Subscribe;
import com.webVueBlog.mqtt.service.ISubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 
 */
@Slf4j
@Component
public class SubscriptionServiceImpl implements ISubscriptionService {

    @Autowired
    private RedisCache redisCache;// Redis缓存

    /**
     * 保存客户订阅的主题
     *
     * @param subscribeList 主题列表
     */
    @Override
    public void subscribe(List<Subscribe> subscribeList, String clientId) {
        redisCache.setCacheList(clientId, subscribeList);// 保存订阅的主题
    }

    /**
     * 解除订阅
     *
     * @param clientId  客户id
     * @param topicName 主题
     */
    @Override
    public void unsubscribe(String clientId, String topicName) {
        redisCache.delHashValue(topicName, clientId);
    }

    /**
     * 获取订阅了 topic 的客户id
     *
     * @param topic 主题
     * @return 订阅了主题的客户id列表
     */
    @Override
    public List<Subscribe> searchSubscribeClientList(String topic) {
          return null;
    }


}
