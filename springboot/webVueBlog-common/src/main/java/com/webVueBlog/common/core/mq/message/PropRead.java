package com.webVueBlog.common.core.mq.message;

import com.webVueBlog.common.core.protocol.modbus.ModbusCode;
import lombok.Data;

/**
 * 
 */
@Data
public class PropRead {

    /**设备编号*/
    private String serialNumber;
    /**寄存器起始地址*/
    private int address;
    /**
     * 读取寄存器个数
     */
    private int count;
    /**数据结果长度计算值*/
    private int length;
    /**
     * 从机地址
     */
    private int slaveId;
    /**
     * 读取个数
     */
    private int quantity;
    /**
     * 数据
     */
    private String data;
    /**
     * 功能码
     */
    private ModbusCode code;
}
