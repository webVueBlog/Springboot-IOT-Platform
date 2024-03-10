package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.annotation.Column;
import com.webVueBlog.protocol.base.model.ModelRegistry;
import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Field;

/**
 * 基础消息结构
 *
 */
public abstract class BaseStructure<T> implements WModel<T>, Comparable<BaseStructure> {

    protected Field field;// 字段
    protected Column column;// 注解
    protected int index;// 索引
    protected int length;// 长度
    protected String description;// 描述


    public void readAndSet(ByteBuf in, Object obj) throws Exception {
        T value = readFrom(in);
        field.set(obj, value);
    }

    public void getAndWrite(ByteBuf out, Object obj) throws Exception {
        T value = (T) field.get(obj);
        writeTo(out, value);
    }

    public void readAndSet(ByteBuf in, Object obj, ExplainUtils explain) throws Exception {
        T value = readFrom(in, explain);
        field.set(obj, value);
    }

    public void getAndWrite(ByteBuf out, Object obj, ExplainUtils explain) throws Exception {
        T value = (T) field.get(obj);
        writeTo(out, value, explain);
    }

    public BaseStructure<T> init(Column column, Field field, int position) {
        if (this.field == null && this.column == null) {
            this.field = field;
            this.column = column;
            length = column.length() > 0 ? column.length() : ModelRegistry.getLength(field.getType());
            length = length > 0 ? length : 16;
            description = column.desc();
            if (description.isEmpty()) {
                description = field.getName();
            }
            index = column.index();
            if (index == 0) {
                index = position;
            }
        }
        return this;
    }

    public String filedName(){
        return field.getName();
    }

    public String description(){
        return description;
    }


    @Override
    public int compareTo(BaseStructure that) {
        return Integer.compare(this.index, that.index);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BaseStructure)) {
            return false;
        }
        BaseStructure that = (BaseStructure) other;
        return field.equals(that.field);
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(12);
        sb.append(description).append(' ').append(field);
        return sb.toString();
    }
}
