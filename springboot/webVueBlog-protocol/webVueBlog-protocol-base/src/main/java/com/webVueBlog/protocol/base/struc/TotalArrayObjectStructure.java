package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import com.webVueBlog.protocol.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 */
public class TotalArrayObjectStructure<T> extends BaseStructure<T[]> {

    private final WModel<T> model;
    private final int totalUnit;// 总单位数
    private final IntTool intTool;// 整数工具类
    private final Class<T> arrayClass;// 数组类型

    public TotalArrayObjectStructure(WModel<T> model, int totalUnit, Class<T> arrayClass) {// 构造函数
        this.model = model;// 模型
        this.totalUnit = totalUnit;// 总单位数
        this.intTool = IntTool.getInstance(totalUnit);// 整数工具类
        this.arrayClass = arrayClass;// 数组类型
    }

    @Override
    public T[] readFrom(ByteBuf in) {
        int total = intTool.read(in);// 读取总单位数
        if (total <= 0) {
            return null;// 如果为0，则返回null
        }
        T[] value = (T[]) Array.newInstance(arrayClass, total);// 创建数组
        for (int i = 0; i < total; i++) {// 循环读取
            T t = model.readFrom(in);// 读取模型
            value[i] = t;// 赋值
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf out, T[] value) {// 写入
        if (value == null) {
            intTool.write(out, 0);// 如果为null，则写入0
        } else {
            int length = value.length;// 获取长度
            intTool.write(out, length);// 写入长度
            for (int i = 0; i < length; i++) {
                T t = value[i];
                model.writeTo(out, t);// 写入模型
            }
        }
    }

    @Override
    public T[] readFrom(ByteBuf in, ExplainUtils explain) {
        int total = intTool.read(in);
        explain.lengthField(in.readerIndex() - totalUnit, description + "数量", total, totalUnit);
        if (total <= 0) {
            return null;
        }
        T[] value = (T[]) Array.newInstance(arrayClass, total);
        for (int i = 0; i < total; i++) {
            T t = model.readFrom(in, explain);
            value[i] = t;
        }
        return value;
    }

    @Override
    public void writeTo(ByteBuf out, T[] value, ExplainUtils explain) {
        if (value == null) {
            explain.lengthField(out.writerIndex(), description + "数量", 0, totalUnit);// 写入长度
            intTool.write(out, 0);
        } else {
            int total = value.length;// 获取长度
            explain.lengthField(out.writerIndex(), description + "数量", total, totalUnit);// 写入长度
            intTool.write(out, total);// 写入长度
            for (int i = 0; i < total; i++) {
                T t = value[i];
                model.writeTo(out, t, explain);
            }
        }
    }
}
