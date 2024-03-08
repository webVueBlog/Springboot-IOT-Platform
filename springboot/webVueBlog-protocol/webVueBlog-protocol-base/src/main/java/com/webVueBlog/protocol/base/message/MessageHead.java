package com.webVueBlog.protocol.base.message;

/**
 *  消息头
 */
public interface MessageHead {

    /**
     * 设备编号 modbus对应从机编号
     * @return
     */
    String getSerialNumber();

    /**
     * 设置设备编号
     * @param serialNumber
     */
    MessageHead setSerialNumber(String serialNumber);

    /**
     * 获取消息ID
     * @return
     */
    String getMessageId();

    /**
     * 设置消息id
     * @param messageId 消息ID
     * @return
     */
    MessageHead setMessageId(String messageId);

    /**
     * 消息头data
     * @return
     */
    byte[] getMessage();

    /**
     * 消息头长度
     * @return
     */
    default int getLength() {
        return getMessage().length;
    }
}
