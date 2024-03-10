package com.webVueBlog.iot.model;

import lombok.Data;

/**
 * 
 */
@Data
public class RegisterUserOutput {

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 用户id
     */
    private Long sysUserId;
}
