package com.webVueBlog.protocol.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.util.LinkedList;

/**
 * 编解码分析
 *
 */
public class ExplainUtils extends LinkedList<Msg> {// 存储分析信息

    public void readField(int index, String desc, Object value, ByteBuf input) {// 读取分析信息
        if (value != null) {
            this.add(Msg.field(index, desc, value, ByteBufUtil.hexDump(input, index, input.readerIndex() - index)));
            // 添加分析信息
        }
    }

    public void writeField(int index, String desc, Object value, ByteBuf output) {// 写入分析信息
        if (value != null) {
            this.add(Msg.field(index, desc, value, ByteBufUtil.hexDump(output, index, output.writerIndex() - index)));
            // 添加分析信息
        }
    }

    public Msg lengthField(int index, String desc, int length, int lengthUnit) {// 长度分析信息
        Msg info = Msg.lengthField(index, desc, length, lengthUnit);// 创建分析信息
        this.add(info);// 添加分析信息
        return info;// 返回分析信息
    }

    public void setLastDesc(String desc) {
        this.get(this.size() - 1).desc = desc;
    }// 设置最后一个分析信息的描述

    public void println() {
        for (Msg info : this) {
            System.out.println(info);// 打印分析信息
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.size() << 5);// 创建字符串构建器
        for (Msg info : this) {
            sb.append(info).append('\n');
        }
        return sb.toString();
    }
}
