package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.ModelRegistry;
import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import com.webVueBlog.protocol.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Array;

/**
 * 指定前置数量的数组域
 *
 */
public class TotalArrayPrimitiveStructure extends BaseStructure {

    private final WModel model;// 模型
    private final int totalUnit;// 总单位
    private final int valueUnit;// 单个元素单位
    private final IntTool intTool;// 整数工具

    public TotalArrayPrimitiveStructure(WModel model, int totalUnit, Class arrayClass) {// 构造
        this.model = model;// 模型
        this.totalUnit = totalUnit;// 总单位
        this.valueUnit = ModelRegistry.getLength(arrayClass);// 单个元素单位
        this.intTool = IntTool.getInstance(totalUnit);// 整数工具
    }

    @Override
    public Object readFrom(ByteBuf in) {// 读取
        int total = intTool.read(in);// 总数量
        if (total <= 0) {// 数量为0
            return null;// 返回null
        }
        int length = valueUnit * (total/2);// 长度
        return model.readFrom(in, length);// 读取
    }

    @Override
    public void writeTo(ByteBuf out, Object value) {// 写入
        if (value == null) {
            intTool.write(out, 0);// 写入0
        } else {
            int total = Array.getLength(value);// 数量
            intTool.write(out, total);// 写入数量
            model.writeTo(out, value);// 写入值
        }
    }

    @Override
    public Object readFrom(ByteBuf in, ExplainUtils explain) {// 读取
        int total = intTool.read(in);// 总数量
        explain.lengthField(in.readerIndex() - totalUnit, description + "数量", total, totalUnit);// 数量字段说明
        if (total <= 0) {
            return null;
        }
        int length = valueUnit * total;// 长度
        return model.readFrom(in, length, explain);// 读取值
    }

    @Override
    public void writeTo(ByteBuf out, Object value, ExplainUtils explain) {
        if (value == null) {
            explain.lengthField(out.writerIndex(), description + "数量", 0, totalUnit);
            intTool.write(out, 0);
        } else {
            int total = Array.getLength(value);
            explain.lengthField(out.writerIndex(), description + "数量", total, totalUnit);
            intTool.write(out, total);
            model.writeTo(out, value, explain);
        }
    }
}
