package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 集合域，位于消息末尾
 */
@AllArgsConstructor
public class CollectionStructure<T> extends BaseStructure<Collection<T>> {

    private final WModel<T> schema;// 集合中元素的schema


    /**
     * 写入集合域
     * @param input
     * @return
     */
    @Override
    public Collection<T> readFrom(ByteBuf input) {
        Collection list = new ArrayList<>();// 集合
        while (input.isReadable()) {// 循环读取
            T t = schema.readFrom(input);// 读取元素
            list.add(t);// 添加到集合
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {// 写入集合域
        if (list != null) {// 集合不为空
            for (T t : list) {// 循环写入
                schema.writeTo(output, t);// 写入元素
            }
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, ExplainUtils explain) {// 读取集合域
        Collection list = new ArrayList<>();// 集合
        while (input.isReadable()) {// 循环读取
            T t = schema.readFrom(input, explain);// 读取元素
            list.add(t);// 添加到集合
        }
        return list;// 返回集合
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, ExplainUtils explain) {// 写入集合域
        if (list != null) {
            for (T t : list) {
                schema.writeTo(output, t, explain);// 写入元素
            }
        }
    }
}
