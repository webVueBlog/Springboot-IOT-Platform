package com.webVueBlog.common.core.mq;

import lombok.Data;

/**
 * 
 */
@Data
public class DeviceReplyBo {

    /*设备下发消息id*/
    private String messageId;
    /*标识符*/
    private String id;
    /**下发值*/
    private String value;
}
