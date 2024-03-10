package com.webVueBlog.mq.service.impl;

import com.webVueBlog.common.core.mq.DeviceReplyBo;
import com.webVueBlog.common.core.mq.InvokeReqDto;
import com.webVueBlog.common.core.mq.MQSendMessageBo;
import com.webVueBlog.common.core.mq.MessageReplyBo;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.common.core.redis.RedisKeyBuilder;
import com.webVueBlog.common.enums.ThingsModelType;
import com.webVueBlog.common.utils.bean.BeanUtils;
import com.webVueBlog.iot.util.SnowflakeIdWorker;
import com.webVueBlog.mq.redischannel.producer.MessageProducer;
import com.webVueBlog.mq.service.IFunctionInvoke;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 */
@Slf4j
@Service
public class FunctionInvokeImpl implements IFunctionInvoke {

    @Resource
    private RedisCache redisCache;

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(2);

    /**
     * 服务调用，等待设备响应
     * @param reqDto 服务下发对象
     * @return 数据结果
     */
    @Override
    public Map<String ,Object> invokeReply(InvokeReqDto reqDto){
       Map<String,Object> result = new HashMap<>();
       invokeNoReply(reqDto);
       //TODO- 根据消息id查询回执，暂时没有消息Id回执
       return result;
    }

    /**
     * 服务调用,设备不响应
     * @param reqDto 服务下发对象
     * @return 消息id messageId
     */
    @Override
    public String invokeNoReply(InvokeReqDto reqDto){
        log.debug("=>下发指令请求：[{}]",reqDto);
        MQSendMessageBo bo = new MQSendMessageBo();
        BeanUtils.copyBeanProp(bo,reqDto);
        long id = snowflakeIdWorker.nextId();
        String messageId = id+"";
        bo.setMessageId(messageId+"");
        bo.setType(ThingsModelType.getType(reqDto.getType()));
        MessageProducer.sendFunctionInvoke(bo);
        //10s，设备不回复，认为指令下发失败
        DeviceReplyBo replyBo = new DeviceReplyBo();
        replyBo.setId(reqDto.getIdentifier());
        replyBo.setMessageId(messageId);
        replyBo.setValue(reqDto.getValue().get(reqDto.getIdentifier()).toString());
        String cacheKey = RedisKeyBuilder.buildDownMessageIdCacheKey(reqDto.getSerialNumber());
        redisCache.setCacheObject(cacheKey, replyBo, 10000, TimeUnit.MILLISECONDS);
        return messageId;
    }

    /**
     * TODO- 轮询拿返回值
     */
    private MessageReplyBo queryResult(InvokeReqDto reqDto){
        MessageReplyBo replyBo = null;
        return replyBo;
    }
}
