package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.PrepareLoadStore;
import com.webVueBlog.protocol.base.struc.BaseStructure;
import com.webVueBlog.protocol.util.IntTool;
import com.webVueBlog.protocol.util.KeyValuePair;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
//MapModel是一个抽象类，它实现了Map.Entry接口。
@Slf4j
public abstract class MapModel<K,V> extends BaseStructure<Map.Entry<K, V>> {
    //MapModel是一个抽象类，它继承了BaseStructure类，并实现了Map.Entry接口。

    public final WModel<K> kwModel;//WModel是一个泛型类，它表示一个键值对的模型。
    public final int lengthUnit;//lengthUnit是一个整数，表示模型中每个元素的长度单位。
    public final IntTool intTool;//IntTool是一个工具类，用于处理整数操作。
    public final Map<K, WModel> valueSchema;//valueSchema是一个Map，用于存储键值对的模型。

    public MapModel(WModel<K> keySchema, int lengthUnit) {//构造函数，初始化模型。
        this.kwModel = keySchema;//初始化键值对的模型。
        this.lengthUnit = lengthUnit;//初始化模型中每个元素的长度单位。
        this.intTool = IntTool.getInstance(lengthUnit);//初始化IntTool工具类，用于处理整数操作。
        PrepareLoadStore<K> loadStrategy = new PrepareLoadStore<>();//创建一个准备加载和存储的策略。
        addSchemas(loadStrategy);//添加键值对的模型到准备加载和存储的策略中。
        this.valueSchema = loadStrategy.build();//构建键值对的模型。
    }

    protected abstract void addSchemas(PrepareLoadStore<K> schemaRegistry);//添加键值对的模型到准备加载和存储的策略中。

    @Override
    public KeyValuePair<K, V> readFrom(ByteBuf in) {//从输入流中读取数据，并返回一个键值对模型。
        K key = kwModel.readFrom(in);//从输入流中读取键值对的键，并返回一个键值对模型的键。
        KeyValuePair<K, V> result = new KeyValuePair<>(key);//创建一个键值对模型，并将键设置为读取到的键。

        int length = intTool.read(in);//从输入流中读取键值对的值的长度，并返回一个整数，表示值的长度。
        if (length > 0) {
            int writerIndex = in.writerIndex();//获取输入流的写索引。
            in.writerIndex(in.readerIndex() + length);//将输入流的写索引设置为读索引加上值的长度。

            WModel<V> model = valueSchema.get(key);//从键值对的模型中获取值对应的模型。
            if (model != null) {//如果模型不为空，则从输入流中读取值，并返回一个值。
                V value = model.readFrom(in, length);//从输入流中读取值，并返回一个值。
                result.setValue(value);//将读取到的值设置为键值对模型的值。
            } else {
                byte[] bytes = new byte[length];//创建一个字节数组，用于存储值。
                in.readBytes(bytes);//从输入流中读取值，并存储到字节数组中。
                result.setValue((V) bytes);//将字节数组设置为键值对模型的值。
            }
            in.writerIndex(writerIndex);//将输入流的写索引设置为之前保存的写索引。

        } else if (length < 0) {//如果值的长度小于0，则表示值的长度未知，需要从输入流中读取值的长度。
            WModel<V> model = valueSchema.get(key);//从键值对的模型中获取值对应的模型。
            if (model != null) {//如果模型不为空，则从输入流中读取值的长度，并返回一个值的长度。
                V value = model.readFrom(in);//从输入流中读取值的长度，并返回一个值的长度。
                result.setValue(value);//将读取到的值的长度设置为键值对模型的值的长度。
            } else {//如果模型为空，则从输入流中读取值的长度，并返回一个值的长度。
                byte[] bytes = new byte[in.readableBytes()];//创建一个字节数组，用于存储值的长度。
                in.readBytes(bytes);//从输入流中读取值的长度，并存储到字节数组中。
                result.setValue((V) bytes);//将字节数组设置为键值对模型的值的长度。
            }
        }
        return result;
    }

    @Override
    public void writeTo(ByteBuf out, Map.Entry<K, V> entry) {
        if (entry == null) {
            return;
        }
        K key = entry.getKey();//获取键值对的键。
        kwModel.writeTo(out, key);//将键写入输出流中。

        WModel model = valueSchema.get(key);//从键值对的模型中获取值对应的模型。
        if (model != null) {//如果模型不为空，则将值写入输出流中。
            int begin = out.writerIndex();//保存输出流的写索引。
            intTool.write(out, 0);//写入一个长度为0的占位符，用于后续写入值的长度。

            Object value = entry.getValue();//获取键值对的值。
            if (value != null) {//如果值不为空，则将值写入输出流中。
                model.writeTo(out, value);//将值写入输出流中。
                int length = out.writerIndex() - begin - lengthUnit;//计算值的长度。
                intTool.set(out, begin, length);//将值的长度写入输出流中。
            }
        } else {
            log.warn("未注册的信息:ID[{}], Value[{}]", key, entry.getValue());
        }
    }
}