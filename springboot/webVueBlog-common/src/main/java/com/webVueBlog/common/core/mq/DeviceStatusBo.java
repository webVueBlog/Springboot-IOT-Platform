package com.webVueBlog.common.core.mq;

import com.webVueBlog.common.enums.DeviceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 设备状态
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DeviceStatusBo {
    /**
     * 设备客户端id
     */
    private String serialNumber;
    /**是否活跃*/
    private DeviceStatus status;
    /**消息时间*/
    private Date timestamp;
    /*host*/
    private String hostName;
    /*port*/
    private Integer port;

    private String ip;

}
