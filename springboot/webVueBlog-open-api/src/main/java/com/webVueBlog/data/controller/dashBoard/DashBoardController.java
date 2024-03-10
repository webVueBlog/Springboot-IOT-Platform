package com.webVueBlog.data.controller.dashBoard;

import com.webVueBlog.common.constant.DaConstant;
import com.webVueBlog.common.core.domain.AjaxResult;
import com.webVueBlog.common.core.redis.RedisCache;
import com.webVueBlog.iot.model.dashBoard.DashMqttMetrics;
import com.webVueBlog.iot.model.dashBoard.DashMqttStat;
import com.webVueBlog.mqtt.manager.ClientManager;
import com.webVueBlog.mqtt.manager.RetainMsgManager;
import com.webVueBlog.base.service.ISessionStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 首页面板和大屏数据
 *
 */
@Api(tags = "首页面板和大屏数据")
@RestController
@RequestMapping("/bashBoard")
public class DashBoardController {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ISessionStore sessionStore;

    @GetMapping("/stats")
    @ApiOperation("mqtt状态数据")
    public AjaxResult stats() {
        DashMqttStat stat = DashMqttStat.builder()
                .connection_count(sessionStore.getSessionMap().size())
                .connection_total(getTotal(DaConstant.REDIS.MESSAGE_CONNECT_TOTAL))
                .subscription_count(ClientManager.topicMap.size())
                .subscription_total(getTotal(DaConstant.REDIS.MESSAGE_SUBSCRIBE_TOTAL))
                .retain_count(RetainMsgManager.getSize())
                .retain_total(getTotal(DaConstant.REDIS.MESSAGE_RETAIN_TOTAL))
                .session_count(sessionStore.getSessionMap().size())
                .session_total(getTotal(DaConstant.REDIS.MESSAGE_CONNECT_TOTAL))
                .build();
        return AjaxResult.success(stat);

    }


    @GetMapping("/metrics")
    @ApiOperation("mqtt统计")
    public AjaxResult metrics() {
        DashMqttMetrics metrics = DashMqttMetrics.builder()
                .send_total(getTotal(DaConstant.REDIS.MESSAGE_SEND_TOTAL))
                .receive_total(getTotal(DaConstant.REDIS.MESSAGE_RECEIVE_TOTAL))
                .auth_total(getTotal(DaConstant.REDIS.MESSAGE_AUTH_TOTAL))
                .connect_total(getTotal(DaConstant.REDIS.MESSAGE_CONNECT_TOTAL))
                .subscribe_total(getTotal(DaConstant.REDIS.MESSAGE_SUBSCRIBE_TOTAL))
                .today_received(getTotal(DaConstant.REDIS.MESSAGE_RECEIVE_TODAY))
                .today_send(getTotal(DaConstant.REDIS.MESSAGE_SEND_TODAY))
                .build();
        return AjaxResult.success(metrics);
    }


    public Integer getTotal(String key) {
        return redisCache.getCacheObject(key) == null ? 0 : redisCache.getCacheObject(key);
    }
}
