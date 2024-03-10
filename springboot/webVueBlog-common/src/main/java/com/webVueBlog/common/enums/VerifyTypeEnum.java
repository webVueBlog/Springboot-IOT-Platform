package com.webVueBlog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 验证类型枚举
 * 
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum VerifyTypeEnum {

    PASSWORD(1, "账号密码验证"),
    SMS(2, "短信验证");

    private Integer verifyType;

    private String desc;
}
