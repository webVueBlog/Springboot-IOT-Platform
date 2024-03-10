package com.webVueBlog.server.handler;


import com.webVueBlog.base.codec.Delimiter;
import com.webVueBlog.base.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.internal.ObjectUtil;

import java.util.List;

import static io.netty.util.internal.ObjectUtil.checkPositive;

/**
 * 分隔符报文解码器 -消息进站处理步骤1 可选
 * 
 */
public class DelimiterBasedFrameDecoder extends ByteToMessageDecoder {

    /*分隔符 例如报文头 0xFF 报文尾 0x0D*/
    private final Delimiter[] delimiters;
    /*最大帧长度*/
    private final int maxFrameLength;
    private final boolean failFast;
    /*是否丢弃超过固定长度的报文*/
    private boolean discardingTooLongFrame;
    /*最长帧长度*/
    private int tooLongFrameLength;

    /*构造分隔符解码器*/
    public DelimiterBasedFrameDecoder(int maxFrameLength, Delimiter... delimiters) {
        this(maxFrameLength, true, delimiters);
    }

    public DelimiterBasedFrameDecoder(int maxFrameLength, boolean failFast, Delimiter... delimiters) {
        validateMaxFrameLength(maxFrameLength);
        ObjectUtil.checkNonEmpty(delimiters, "delimiters");

        this.delimiters = delimiters;
        this.maxFrameLength = maxFrameLength;
        this.failFast = failFast;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /*根据分隔符处理粘包报文*/
        Object decoded = decode(ctx, in);
        if (decoded != null) {
            /*报文出站，流入下一个处理器*/
            out.add(decoded);
        }
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) {
        // 使用所有分隔符并选择产生最短帧的分隔符
        int minFrameLength = Integer.MAX_VALUE;
        Delimiter minDelim = null;
        for (Delimiter delim : delimiters) {
            int frameLength = ByteBufUtils.indexOf(buffer, delim.value);
            if (frameLength >= 0 && frameLength < minFrameLength) {
                /*最小报文长度*/
                minFrameLength = frameLength;
                minDelim = delim;
            }
        }

        if (minDelim != null) {
            int minDelimLength = minDelim.value.length;
            ByteBuf frame = null;

            if (discardingTooLongFrame) {
                // 如果true，将长度不符合报文丢弃
                // 初始化原来的值
                discardingTooLongFrame = false;
                buffer.skipBytes(minFrameLength + minDelimLength);

                int tooLongFrameLength = this.tooLongFrameLength;
                this.tooLongFrameLength = 0;
                if (!failFast) {
                    fail(tooLongFrameLength);
                }
                return null;
            }
            /*小于最小长度帧处理*/
            if (minFrameLength > maxFrameLength) {
                //放弃读取帧
                buffer.skipBytes(minFrameLength + minDelimLength);
                fail(minFrameLength);
                return null;
            }
            /*是否需要跳过某字节*/
            if (minDelim.strip) {
                //忽略长度等于0的报文
                if (minFrameLength != 0) {
                    frame = buffer.readRetainedSlice(minFrameLength);
                }
                buffer.skipBytes(minDelimLength);
            } else {
                if (minFrameLength != 0) {
                    frame = buffer.readRetainedSlice(minFrameLength + minDelimLength);
                } else {
                    buffer.skipBytes(minDelimLength);
                }
            }

            return frame;
        } else {
            if (!discardingTooLongFrame) {
                if (buffer.readableBytes() > maxFrameLength) {
                    // Discard the content of the buffer until a delimiter is found.
                    tooLongFrameLength = buffer.readableBytes();
                    buffer.skipBytes(buffer.readableBytes());
                    discardingTooLongFrame = true;
                    if (failFast) {
                        fail(tooLongFrameLength);
                    }
                }
            } else {
                // Still discarding the buffer since a delimiter is not found.
                tooLongFrameLength += buffer.readableBytes();
                buffer.skipBytes(buffer.readableBytes());
            }
            return null;
        }
    }

    private void fail(long frameLength) {
        if (frameLength > 0) {
            throw new TooLongFrameException("frame length exceeds " + maxFrameLength + ": " + frameLength + " - discarded");
        } else {
            throw new TooLongFrameException("frame length exceeds " + maxFrameLength + " - discarding");
        }
    }

    private static void validateMaxFrameLength(int maxFrameLength) {
        checkPositive(maxFrameLength, "maxFrameLength");
    }
}
