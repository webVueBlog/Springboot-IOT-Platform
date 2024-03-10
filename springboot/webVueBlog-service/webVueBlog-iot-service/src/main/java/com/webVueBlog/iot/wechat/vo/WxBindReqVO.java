package com.webVueBlog.iot.wechat.vo;

import lombok.Data;

/**
 * WxBindReqVO是微信绑定请求的VO类
 */
@Data
public class WxBindReqVO {

    private String sourceClient;// 客户端来源

    private String openId;// 用户的唯一标识

    private String unionId;// 微信开放平台的唯一标识符

    private String code;// 微信登录时获取的code
}
