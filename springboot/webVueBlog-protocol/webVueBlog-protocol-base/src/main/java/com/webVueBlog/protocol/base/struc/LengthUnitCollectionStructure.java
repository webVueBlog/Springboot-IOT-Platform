package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import com.webVueBlog.protocol.util.IntTool;
import com.webVueBlog.protocol.util.Msg;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 每个元素长度单位的集合域，位于消息末尾
 */
public class LengthUnitCollectionStructure<T> extends BaseStructure<Collection<T>> {

    private final WModel<T> model;// 元素模型
    private final int lengthUnit;// 元素长度单位
    private final IntTool intTool;// 工具类


    public LengthUnitCollectionStructure(WModel<T> model, int lengthUnit) {
        this.model = model;// 元素模型
        this.lengthUnit = lengthUnit;// 元素长度单位
        this.intTool = IntTool.getInstance(lengthUnit);// 工具类
    }

    @Override
    public Collection<T> readFrom(ByteBuf input) {// 读取消息
        Collection list = new ArrayList<>();// 集合
        while (input.isReadable()) {// 循环读取
            int length = intTool.read(input);// 读取长度
            T t = model.readFrom(input, length);// 读取元素
            list.add(t);// 添加元素
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {// 写入消息
        if (list != null) {
            for (T t : list) {// 循环写入
                if (t != null) {// 非空才写入
                    int begin = output.writerIndex();// 写入位置
                    intTool.write(output, 0);// 写入长度
                    model.writeTo(output, t);// 写入元素
                    int length = output.writerIndex() - begin - lengthUnit;// 计算长度
                    intTool.set(output, begin, length);// 设置长度
                }
            }
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, ExplainUtils explain) {
        Collection list = new ArrayList<>();
        while (input.isReadable()) {
            int length = intTool.read(input);
            explain.lengthField(input.readerIndex() - lengthUnit, description + "长度", length, lengthUnit);
            T t = model.readFrom(input, length, explain);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, ExplainUtils explain) {
        if (list != null) {
            for (T t : list) {
                if (t != null) {
                    int begin = output.writerIndex();// 写入位置
                    Msg msg = explain.lengthField(begin, description + "长度", 0, lengthUnit);
                    intTool.write(output, 0);
                    model.writeTo(output, t, explain);
                    int length = output.writerIndex() - begin - lengthUnit;
                    intTool.set(output, begin, length);
                    msg.setLength(length, lengthUnit);
                }
            }
        }
    }
}
