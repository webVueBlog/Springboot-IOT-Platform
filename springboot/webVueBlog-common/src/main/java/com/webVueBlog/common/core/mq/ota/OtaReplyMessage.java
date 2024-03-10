package com.webVueBlog.common.core.mq.ota;

import lombok.Data;

/**
 * OTA升级回复model
 * 
 */
@Data
public class OtaReplyMessage {

    private String messageId;
    // 200成功 其他。。
    private int code;
    private String msg;
}
