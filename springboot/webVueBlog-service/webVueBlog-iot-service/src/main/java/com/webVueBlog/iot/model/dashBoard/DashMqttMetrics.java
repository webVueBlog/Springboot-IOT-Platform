package com.webVueBlog.iot.model.dashBoard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DashMqttMetrics是DashMqttMetrics的实体类，用于存储MQTT仪表板指标的值。
 */
@Data
@Builder
public class DashMqttMetrics {

    /**接收总数*/
    private Integer receive_total;
    /**发送总数*/
    private Integer send_total;
    /**认证总数*/
    private Integer auth_total;
    /**连接总数*/
    private Integer connect_total;
    /**订阅总数*/
    private Integer subscribe_total;
    /**今日接收总数*/
    private Integer today_received;
    /**今日发送总数*/
    private Integer today_send;
}
