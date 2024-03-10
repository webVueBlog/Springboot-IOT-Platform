package com.webVueBlog.iot.model.login;

import lombok.Data;

/**
 * 
 */
@Data
public class WeChatLoginQrRes {

    private String appid;

    private String scope;//

    private String redirectUri;//

    private String state;//
}
