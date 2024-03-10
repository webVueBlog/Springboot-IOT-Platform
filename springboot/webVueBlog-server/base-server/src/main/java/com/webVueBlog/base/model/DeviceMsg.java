package com.webVueBlog.base.model;

import lombok.Data;

/**
 * 
 */
@Data
public class DeviceMsg {

    protected String clientId;

    protected Long deviceId;

    private int protocolVersion;

    private Long productId;
}
