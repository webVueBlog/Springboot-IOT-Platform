package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 *
 */
public interface WModel<T> {

    /**
     * 读缓存
     */
    T readFrom(ByteBuf in);

    void writeTo(ByteBuf out, T value);

    default T readFrom(ByteBuf in, int length) {
        /*输入的报文长度*/
        int readLength = in.readerIndex() + length;
        /*读索引位*/
        int writerIndex = in.writerIndex();
        in.writerIndex(readLength);//设置写索引位
        T value = readFrom(in);//读取报文
        in.setIndex(readLength, writerIndex);//设置读索引位
        return value;//返回报文
    }

    default void writeTo(ByteBuf out, int length, T value) {
        int writeLength = out.writerIndex() + length;//设置写索引位
        writeTo(out, value);//写入报文
        out.writerIndex(writeLength);//设置写索引位
    }

    default T readFrom(ByteBuf in, ExplainUtils explain) {//读取报文
        int start = in.readerIndex();//读索引位
        T value = readFrom(in);//读取报文
        explain.readField(start,desc(), value,in);//读取字段
        return value;//返回报文
    }

    default void writeTo(ByteBuf out, T value, ExplainUtils explain) {
        int begin = out.writerIndex();
        writeTo(out, value);
        explain.writeField(begin, desc(), value, out);
    }

    default T readFrom(ByteBuf in, int length, ExplainUtils explain) {
        int readerLength = in.readerIndex() + length;
        int writerIndex = in.writerIndex();
        in.writerIndex(readerLength);
        T value = readFrom(in, explain);
        in.setIndex(readerLength, writerIndex);
        return value;
    }

    default void writeTo(ByteBuf out, int length, T value, ExplainUtils explain) {
        int writerLength = out.writerIndex() + length;
        writeTo(out, value, explain);
        out.writerIndex(writerLength);
    }

    /** 内存分配 */
    default int length() {
        return 32;
    }

    default String desc() {
        return "";
    }
}
