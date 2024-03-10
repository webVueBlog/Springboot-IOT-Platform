package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import com.webVueBlog.protocol.util.IntTool;
import com.webVueBlog.protocol.util.Msg;
import io.netty.buffer.ByteBuf;

/**
 * 指定长度单位域
 */
public class LengthUnitStructure<T> extends BaseStructure<T> {

    private final WModel<T> schema;
    private final int lengthUnit;
    private final IntTool intTool;

    public LengthUnitStructure(WModel<T> model, int lengthUnit) {
        this.schema = model;
        this.lengthUnit = lengthUnit;
        this.intTool = IntTool.getInstance(lengthUnit);
    }

    @Override
    public T readFrom(ByteBuf in) {
        int length = intTool.read(in);
        return schema.readFrom(in, length);
    }

    @Override
    public void writeTo(ByteBuf out, T value) {
        int begin = out.writerIndex();
        intTool.write(out, 0);
        if (value != null) {
            schema.writeTo(out, value);
            int length = out.writerIndex() - begin - lengthUnit;
            intTool.set(out, begin, length);
        }
    }

    @Override
    public T readFrom(ByteBuf in, ExplainUtils explain) {
        int length = intTool.read(in);
        explain.lengthField(in.readerIndex() - lengthUnit, description + "长度", length, lengthUnit);
        T value = schema.readFrom(in, length, explain);
        explain.setLastDesc(description);
        return value;
    }

    @Override
    public void writeTo(ByteBuf out, T value, ExplainUtils explain) {
        int begin = out.writerIndex();
        Msg msg = explain.lengthField(begin, description + "长度", 0, lengthUnit);
        intTool.write(out, 0);
        if (value != null) {
            schema.writeTo(out, value, explain);
            explain.setLastDesc(description);
            int length = out.writerIndex() - begin - lengthUnit;
            intTool.set(out, begin, length);
            msg.setLength(length, lengthUnit);
        }
    }
}
