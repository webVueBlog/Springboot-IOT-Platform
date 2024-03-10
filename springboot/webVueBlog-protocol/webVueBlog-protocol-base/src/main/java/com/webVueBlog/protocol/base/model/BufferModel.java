package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * BufferModel表示一个缓冲区模型，它包含了一些读取和写入缓冲区的方法。
 */
public class BufferModel {
    /**
     * ByteBufSchema是一个用于读取和写入ByteBuf的模型。
     */
    public static class ByteBufSchema extends BaseStructure<ByteBuf> {
        @Override
        public ByteBuf readFrom(ByteBuf input) {// 从输入中读取一个ByteBuf。
            return input.readSlice(input.readableBytes());// 读取可读字节数组。
        }

        @Override
        public void writeTo(ByteBuf output, ByteBuf value) {// 将一个ByteBuf写入输出。
            output.writeBytes(value);// 将value中的字节写入output。
        }
    }

    public static class ByteBufferSchema extends BaseStructure<ByteBuffer> {
        @Override
        public ByteBuffer readFrom(ByteBuf input) {
            ByteBuffer message = input.nioBuffer();
            input.skipBytes(input.readableBytes());
            return message;
        }

        @Override
        public void writeTo(ByteBuf output, ByteBuffer value) {
            output.writeBytes(value);
        }
    }
}
