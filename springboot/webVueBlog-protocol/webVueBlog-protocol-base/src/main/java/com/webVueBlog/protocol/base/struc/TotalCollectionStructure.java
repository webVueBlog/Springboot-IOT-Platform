package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import com.webVueBlog.protocol.util.IntTool;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 前置数量的集合报文结构
 */
public class TotalCollectionStructure<T> extends BaseStructure<Collection<T>> {

    private final WModel<T> model;// 数据模型
    private final int totalUnit;// 集合中元素的数量
    private final IntTool intTool;// 整型工具类

    public TotalCollectionStructure(WModel<T> model, int totalUnit){// 构造函数
        this.model =model;// 数据模型
        this.totalUnit =totalUnit;// 集合中元素的数量
        this.intTool = IntTool.getInstance(totalUnit);// 整型工具类
    }


    @Override
    public Collection<T> readFrom(ByteBuf input) {
        int total = intTool.read(input);// 读取数量
        if (total <= 0) {
            return null;
        }
        ArrayList<T> list = new ArrayList<>(total);// 集合
        for (int i = 0; i < total; i++) {// 循环读取
            T t = model.readFrom(input);// 读取元素
            list.add(t);// 添加元素
        }
        return list;// 返回集合
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list) {
        if (list != null) {
            intTool.write(output, list.size());
            for (T t : list) {
                model.writeTo(output, t);
            }
        } else {
            intTool.write(output, 0);
        }
    }

    @Override
    public Collection<T> readFrom(ByteBuf input, ExplainUtils explain) {
        int total = intTool.read(input);
        explain.lengthField(input.readerIndex() - totalUnit, description + "数量", total, totalUnit);
        if (total <= 0) {
            return null;
        }
        ArrayList<T> list = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            T t = model.readFrom(input, explain);
            list.add(t);
        }
        return list;
    }

    @Override
    public void writeTo(ByteBuf output, Collection<T> list, ExplainUtils explain) {
        if (list != null) {
            int total = list.size();
            explain.lengthField(output.writerIndex(), description + "数量", total, totalUnit);
            intTool.write(output, total);
            for (T t : list) {
                model.writeTo(output, t, explain);
            }
        } else {
            explain.lengthField(output.writerIndex(), description + "数量", 0, totalUnit);
            intTool.write(output, 0);
        }
    }
}