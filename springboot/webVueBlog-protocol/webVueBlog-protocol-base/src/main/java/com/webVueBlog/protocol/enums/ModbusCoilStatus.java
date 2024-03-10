package com.webVueBlog.protocol.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 线圈状态
 */
@Getter
@AllArgsConstructor
public enum ModbusCoilStatus {

    //线圈开启
    ON(new byte[] {(byte) 0xFF, 0x00}),
    //线圈关闭
    OFF(new byte[] {0x00, 0x00})
    ;
    private byte[] data;
}
