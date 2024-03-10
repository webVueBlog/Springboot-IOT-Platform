package com.webVueBlog.protocol.base.struc;

import com.webVueBlog.protocol.base.model.MapModel;
import com.webVueBlog.protocol.base.model.WModel;
import com.webVueBlog.protocol.util.ExplainUtils;
import com.webVueBlog.protocol.util.IntTool;
import com.webVueBlog.protocol.util.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * map类型，位于报文末尾
 */
@Slf4j
public class MapStructure<K,V> extends BaseStructure<Map<K,V>> {

    private final WModel<K> kwModel;
    private final Map<K, WModel<V>> valuesModel;
    private final int lengthUnit;
    private final IntTool valueIntTool;
    private final boolean treeMap;

    public MapStructure(MapModel mapModel, Class typeClass) {
        this.kwModel = mapModel.kwModel;
        this.valuesModel = mapModel.valueSchema;
        this.lengthUnit = mapModel.lengthUnit;
        this.valueIntTool = mapModel.intTool;
        this.treeMap = !HashMap.class.isAssignableFrom(typeClass);
    }

    @Override
    public Map<K, V> readFrom(ByteBuf in) {
        if (!in.isReadable()) {
            return null;
        }

        Map map;
        if (treeMap) {
            map = new TreeMap();
        }
        else{ map = new HashMap(8);}

        K key = null;
        int length = 0;
        try {
            do {
                key = kwModel.readFrom(in);

                length = valueIntTool.read(in);
                if (length <= 0) {
                    continue;
                }

                int writerIndex = in.writerIndex();
                int readerIndex = in.readerIndex() + length;
                if (writerIndex > readerIndex) {
                    in.writerIndex(readerIndex);
                    Object value = readValue(key, in);
                    map.put(key, value);
                    in.setIndex(readerIndex, writerIndex);
                } else {
                    Object value = readValue(key, in);
                    map.put(key, value);
                    break;
                }
            } while (in.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:ID[{}], LENGTH[{}], {}", key, length, e.getMessage());
        }
        return map;
    }

    public Object readValue(Object key, ByteBuf in) {
        WModel model = valuesModel.get(key);
        if (model != null) {
            return model.readFrom(in);
        }
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        return bytes;
    }

    @Override
    public void writeTo(ByteBuf output, Map<K, V> map) {
        if (map == null) {
            return;
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            kwModel.writeTo(output, key);

            V value = entry.getValue();
            WModel<V> schema = valuesModel.get(key);
            if (schema != null) {
                int begin = output.writerIndex();
                valueIntTool.write(output, 0);
                schema.writeTo(output, value);
                int length = output.writerIndex() - begin - lengthUnit;
                valueIntTool.set(output, begin, length);
            } else {
                log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
            }
        }
    }

    @Override
    public Map<K, V> readFrom(ByteBuf in, ExplainUtils explain) {
        if (!in.isReadable()) {
            return null;
        }

        Map map;
        if (treeMap) {
            map = new TreeMap();
        } else {
            map = new HashMap(8);
        }

        K key = null;
        int length = 0;
        try {
            do {
                key = kwModel.readFrom(in, explain);
                explain.setLastDesc(description + "ID");

                length = valueIntTool.read(in);
                explain.lengthField(in.readerIndex() - lengthUnit, description + "长度", length, lengthUnit);
                if (length <= 0) {
                    continue;
                }

                int writerIndex = in.writerIndex();
                int readerIndex = in.readerIndex() + length;
                if (writerIndex > readerIndex) {
                    in.writerIndex(readerIndex);
                    Object value = readValue(key, in, explain);
                    map.put(key, value);
                    in.setIndex(readerIndex, writerIndex);
                } else {
                    Object value = readValue(key, in, explain);
                    map.put(key, value);
                    break;
                }
            } while (in.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:ID[{}], LENGTH[{}], {}", key, length, e.getMessage());
        }
        return map;
    }

    public Object readValue(Object key, ByteBuf in, ExplainUtils explain) {
        WModel model = valuesModel.get(key);
        if (model != null) {
            Object value = model.readFrom(in, explain);
            return value;
        }
        int begin = in.readerIndex();
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        explain.readField(begin, description, ByteBufUtil.hexDump(bytes), in);
        return bytes;
    }

    @Override
    public void writeTo(ByteBuf output, Map<K, V> map, ExplainUtils explain) {
        if (map == null) {
            return;
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            kwModel.writeTo(output, key, explain);
            explain.setLastDesc(description + "ID");

            V value = entry.getValue();
            WModel<V> model = valuesModel.get(key);
            if (model != null) {
                int begin = output.writerIndex();
                Msg msg = explain.lengthField(begin, description + "长度", 0, lengthUnit);
                valueIntTool.write(output, 0);
                model.writeTo(output, value, explain);
                int length = output.writerIndex() - begin - lengthUnit;
                valueIntTool.set(output, begin, length);
                msg.setLength(length, lengthUnit);
            } else {
                log.warn("未注册的信息:ID[{}], VALUE[{}]", key, value);
            }
        }
    }
}