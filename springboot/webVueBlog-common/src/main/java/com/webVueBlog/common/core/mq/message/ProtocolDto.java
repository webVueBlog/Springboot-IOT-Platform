package com.webVueBlog.common.core.mq.message;

import lombok.Data;

/**
 * 协议bean
 * 
 */
@Data
public class ProtocolDto {

    /**协议编号*/
    private String code;
    private String name;
    /*外部协议url*/
    private String protocolUrl;
    private String description;
    /**协议类型 协议类型 0:系统协议 1:jar，2.js,3.c*/
    private Integer protocolType;
}
