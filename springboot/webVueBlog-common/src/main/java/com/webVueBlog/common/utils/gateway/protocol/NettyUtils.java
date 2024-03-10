package com.webVueBlog.common.utils.gateway.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 
 */
public class NettyUtils {

    /**
     * ByteBuf转 byte[]
     * @param buf buffer
     * @return byte[]
     */
    public static byte[] readBytesFromByteBuf(ByteBuf buf){
        return ByteBufUtil.getBytes(buf);
    }


}
