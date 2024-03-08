package com.webVueBlog.protocol.base.message;

/**
 * 通用消息体
 */
public interface MessageBody {

    /**
     * 消息体
     * @return
     */
    byte[] getPayload();// 消息体

    /**
     * 消息体长度
     * @return
     */
    default int getLength(){
        return getPayload().length;// 消息体长度
    }
}
