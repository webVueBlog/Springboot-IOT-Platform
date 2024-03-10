package com.webVueBlog.iot.wechat.vo;

import lombok.Data;

/**
 * 微信解绑传参类
 * 
 */
@Data
public class WxCancelBindReqVO {

    /**
     * 验证类型
     */
    private Integer verifyType;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 短信验证码
     */
    private String smsCode;
}
