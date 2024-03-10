package com.webVueBlog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 
 */
@Getter
@AllArgsConstructor
public enum ExceptionCode {

    SUCCESS(200,"成功"),
    TIMEOUT(400,"超时"),
    OFFLINE(404,"设备断线"),
    FAIL(500,"失败");
    ;

    public int code;
    public String desc;
}
