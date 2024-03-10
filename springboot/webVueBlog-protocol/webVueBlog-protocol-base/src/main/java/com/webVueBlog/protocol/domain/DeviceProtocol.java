package com.webVueBlog.protocol.domain;

import com.webVueBlog.protocol.base.protocol.IProtocol;
import lombok.Data;

/**
 * 设备协议model
 */
@Data
public class DeviceProtocol {

    /**协议实例*/
    private IProtocol protocol;

    /**产品id*/
    private Long productId;

    /**设备编号*/
    private String serialNumber;
}
