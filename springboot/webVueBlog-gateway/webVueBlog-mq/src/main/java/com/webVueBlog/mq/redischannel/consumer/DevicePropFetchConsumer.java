package com.webVueBlog.mq.redischannel.consumer;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.mq.message.DeviceDownMessage;
import com.webVueBlog.common.core.mq.message.PropRead;
import com.webVueBlog.common.core.protocol.Message;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.core.redis.RedisKeyBuilder;
import com.webVueBlog.common.utils.gateway.protocol.ByteUtils;
import com.webVueBlog.mq.mqttClient.PubMqttClient;
import com.webVueBlog.mq.service.impl.MessageManager;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 平台定时批量获取设备属性(或单个获取)
 * 
 */
@Slf4j
@Component
public class DevicePropFetchConsumer {


    @Autowired
    private PubMqttClient pubMqttClient;
    @Autowired
    private RedisCache redisCache;
    @Resource
    private MessageManager messageManager;


    @Async(DaConstant.TASK.DEVICE_FETCH_PROP_TASK)
    public void consume(DeviceDownMessage downMessage){
        execute(downMessage);
    }

    private void execute(DeviceDownMessage message){
        try {
            for (PropRead read : message.getValues()) {
                //缓存值
                String cacheKey = RedisKeyBuilder.buildPropReadCacheKey(message.getSubCode());
                redisCache.setCacheObject(cacheKey, read, 1500, TimeUnit.MILLISECONDS);
                switch (message.getServerType()){
                    //通过mqtt内部客户端 下发指令
                    case MQTT:
                        pubMqttClient.publish(message.getTopic(), ByteUtils.hexToByte(read.getData()), null);
                        log.info("=>MQTT-线程=[{}],轮询指令:[{}],主题:[{}]", Thread.currentThread().getName(), read.getData(), message.getTopic());
                        break;
                    //  下发TCP客户端
                    case TCP:
                        Message msg = new Message();
                        msg.setClientId(message.getSerialNumber());
                        msg.setPayload(Unpooled.wrappedBuffer(ByteBufUtil.decodeHexDump(read.getData())));
                        messageManager.requestR(message.getSerialNumber(), msg,Message.class);
                        log.info("=>TCP-线程=[{}],轮询指令:[{}]", Thread.currentThread().getName(), read.getData());
                        break;
                }
                Thread.sleep(1500);
            }
        }catch (Exception e){
            log.error("线程错误e",e);
        }
    }
}
