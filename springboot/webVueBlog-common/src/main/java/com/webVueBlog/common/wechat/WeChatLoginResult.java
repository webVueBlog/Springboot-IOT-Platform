package com.webVueBlog.common.wechat;

import lombok.Data;

/**
 * 微信登录返回结果
 * 
 */
@Data
public class WeChatLoginResult {

    /**
     * 登录成功返回token
     */
    private String token;

    /**
     * 绑定账号跳转页面
     */
    private String bindId;
}
