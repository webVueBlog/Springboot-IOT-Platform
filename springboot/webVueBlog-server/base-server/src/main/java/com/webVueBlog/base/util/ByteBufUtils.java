package com.webVueBlog.base.util;

import io.netty.buffer.ByteBuf;

/**
 * byteBuf操作工具
 * 
 */
public class ByteBufUtils {

    /**
     * 返回报文的readerIndex和报文中找到的第一个指针之间的字节数-如果在报文中找不到针，则返回1
     */
    public static int indexOf(ByteBuf haystack, byte[] needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.length; needleIndex++) {
                if (haystack.getByte(haystackIndex) != needle[needleIndex]) {
                    break;
                } else {
                    haystackIndex++;
                    if (haystackIndex == haystack.writerIndex() && needleIndex != needle.length - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.length) {
                // 找到读取的index
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }

    public static boolean startsWith(ByteBuf haystack, byte[] prefix) {
        for (int i = 0, j = haystack.readerIndex(); i < prefix.length; )
            if (prefix[i++] != haystack.getByte(j++))
                return false;
        return true;
    }
}
