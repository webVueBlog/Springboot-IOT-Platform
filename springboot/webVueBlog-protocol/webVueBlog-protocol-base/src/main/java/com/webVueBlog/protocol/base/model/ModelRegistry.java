package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.annotation.Column;
import com.webVueBlog.protocol.base.struc.*;
import com.webVueBlog.protocol.util.DateTool;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 消息编码类注册
 *
 */
public class ModelRegistry {

    private static final Map<String, Function<DateTool, BaseStructure>> TIME_MODEL = new HashMap<>(6);//时间类型

    private static final Map<String, Supplier<BaseStructure>> NO_ARGS = new HashMap<>(128);//无参构造函数

    private static final Map<String, Integer> NUMBER = new HashMap<>(12);//基本类型

    static {
        NUMBER.put(boolean.class.getName(), 1);// 示例数据为 1
        NUMBER.put(char.class.getName(), 2);
        NUMBER.put(byte.class.getName(), 1);
        NUMBER.put(short.class.getName(), 2);
        NUMBER.put(int.class.getName(), 4);
        NUMBER.put(long.class.getName(), 8);
        NUMBER.put(float.class.getName(), 4);
        NUMBER.put(double.class.getName(), 8);

        NUMBER.put(Boolean.class.getName(), 1);
        NUMBER.put(Character.class.getName(), 2);
        NUMBER.put(Byte.class.getName(), 1);
        NUMBER.put(Short.class.getName(), 2);
        NUMBER.put(Integer.class.getName(), 4);
        NUMBER.put(Long.class.getName(), 8);
        NUMBER.put(Float.class.getName(), 4);
        NUMBER.put(Double.class.getName(), 8);

        register(short.class, NumberPModel.WORD2ShortLE::new, 2, "LE");
        register(int.class, NumberPModel.WORD2IntLE::new, 2, "LE");
        register(int.class, NumberPModel.DWORD2IntLE::new, 4, "LE");
        register(long.class, NumberPModel.DWORD2LongLE::new, 4, "LE");
        register(long.class, NumberPModel.QWORD2LongLE::new, 8, "LE");
        register(short.class, NumberPModel.WORD2ShortLE::new, "LE");
        register(int.class, NumberPModel.DWORD2IntLE::new, "LE");
        register(long.class, NumberPModel.QWORD2LongLE::new, "LE");
        register(float.class, NumberPModel.DWORD2FloatLE::new, "LE");
        register(double.class, NumberPModel.QWORD2DoubleLE::new, "LE");
        register(byte.class, NumberPModel.BYTE2Byte::new, 1);
        register(short.class, NumberPModel.BYTE2Short::new, 1);
        register(int.class, NumberPModel.BYTE2Int::new, 1);
        register(short.class, NumberPModel.WORD2Short::new, 2);
        register(int.class, NumberPModel.WORD2Int::new, 2);
        register(int.class, NumberPModel.DWORD2Int::new, 4);
        register(long.class, NumberPModel.DWORD2Long::new, 4);
        register(long.class, NumberPModel.QWORD2Long::new, 8);
        register(boolean.class, NumberPModel.BOOL::new);
        register(char.class, NumberPModel.CHAR::new);
        register(byte.class, NumberPModel.BYTE2Byte::new);
        register(short.class, NumberPModel.WORD2Short::new);
        register(int.class, NumberPModel.DWORD2Int::new);
        register(long.class, NumberPModel.QWORD2Long::new);
        register(float.class, NumberPModel.DWORD2Float::new);
        register(double.class, NumberPModel.QWORD2Double::new);

        register(Short.class, NumberModel.WORD2ShortLE::new, 2, "LE");
        register(Integer.class, NumberModel.WORD2IntLE::new, 2, "LE");
        register(Integer.class, NumberModel.DWORD2IntLE::new, 4, "LE");
        register(Long.class, NumberModel.DWORD2LongLE::new, 4, "LE");
        register(Long.class, NumberModel.QWORD2LongLE::new, 8, "LE");
        register(Short.class, NumberModel.WORD2ShortLE::new, "LE");
        register(Integer.class, NumberModel.DWORD2IntLE::new, "LE");
        register(Long.class, NumberModel.QWORD2LongLE::new, "LE");
        register(Float.class, NumberModel.DWORD2FloatLE::new, "LE");
        register(Double.class, NumberModel.QWORD2DoubleLE::new, "LE");
        register(Byte.class, NumberModel.BYTE2Byte::new, 1);
        register(Short.class, NumberModel.BYTE2Short::new, 1);
        register(Integer.class, NumberModel.BYTE2Int::new, 1);
        register(Short.class, NumberModel.WORD2Short::new, 2);
        register(Integer.class, NumberModel.WORD2Int::new, 2);
        register(Integer.class, NumberModel.DWORD2Int::new, 4);
        register(Long.class, NumberModel.DWORD2Long::new, 4);
        register(Long.class, NumberModel.QWORD2Long::new, 8);
        register(Boolean.class, NumberModel.BOOL::new);
        register(Character.class,    /**/NumberModel.CHAR::new);
        register(Byte.class, NumberModel.BYTE2Byte::new);
        register(Short.class, NumberModel.WORD2Short::new);
        register(Integer.class, NumberModel.DWORD2Int::new);
        register(Long.class, NumberModel.QWORD2Long::new);
        register(Float.class, NumberModel.DWORD2Float::new);
        register(Double.class, NumberModel.QWORD2Double::new);

        register(byte[].class, ArrayModel.ByteArray::new);
        register(char[].class, ArrayModel.CharArray::new);
        register(short[].class, ArrayModel.ShortArray::new);
        register(int[].class, ArrayModel.IntArray::new);
        register(long[].class, ArrayModel.LongArray::new);
        register(float[].class, ArrayModel.FloatArray::new);
        register(double[].class, ArrayModel.DoubleArray::new);
        register(ByteBuffer.class, BufferModel.ByteBufferSchema::new);
        register(ByteBuf.class, BufferModel.ByteBufSchema::new);

        TIME_MODEL.put(LocalTime.class.getName(), DateTimeModel.Time::new);
        TIME_MODEL.put(LocalDate.class.getName(), DateTimeModel.Date::new);
        TIME_MODEL.put(LocalDateTime.class.getName(), DateTimeModel.DateTime::new);
    }

    public static void register(Class typeClass, Supplier<BaseStructure> supplier, int length, String charset) {
        NO_ARGS.put(typeClass.getName() + "/" + length + "/" + charset, supplier);
    }

    public static void register(Class typeClass, Supplier<BaseStructure> supplier, int length) {
        NO_ARGS.put(typeClass.getName() + "/" + length, supplier);
    }

    public static void register(Class typeClass, Supplier<BaseStructure> supplier, String charset) {
        NO_ARGS.put(typeClass.getName() + "/" + charset, supplier);
    }

    public static void register(Class typeClass, Supplier model) {
        NO_ARGS.put(typeClass.getName(), model);
    }

    public static WModel getCustom(Class<? extends WModel> clazz) {
        try {
            return clazz.getDeclaredConstructor((Class[]) null).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static BaseStructure get(Column column, java.lang.reflect.Field f) {
        Class typeClass = f.getType();// 类型
        String name = typeClass.getName();// 类型名
        String charset = column.charset().toUpperCase();// 编码
        int length = column.length();// 长度

        if (NUMBER.containsKey(name)) {
            if (length > 0) {
                name += "/" + length;
            }
            if (charset.equals("LE")) {
                name += "/LE";
            }
            return NO_ARGS.get(name).get();
        }

        if (String.class.isAssignableFrom(typeClass)) {
            return StringModel.getInstance(charset, length, column.lengthUnit());
        }
        if (Temporal.class.isAssignableFrom(typeClass)) {
            return TIME_MODEL.get(name).apply(charset.equals("BCD") ? DateTool.BCD : DateTool.BYTE);
        }

        if (WModel.class != column.converter()) {
            return get(column, f, getCustom(column.converter()));
        }

        Supplier<BaseStructure> supplier = NO_ARGS.get(name);
        if (supplier != null) {
            return get(column, f, supplier.get());
        }
        return null;
    }

    public static BaseStructure get(Column column, java.lang.reflect.Field f, WModel model) {
        Class typeClass = f.getType();
        if (column.totalUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass)) {
                return new TotalCollectionStructure(model, column.totalUnit());
            }
            if (Map.class.isAssignableFrom(typeClass)) {
                return new TotalMapStructure((MapModel) model, column.totalUnit(), typeClass);
            }
            if (typeClass.isArray()) {
                typeClass = typeClass.getComponentType();
                if (typeClass.isPrimitive()) {
                    return new TotalArrayPrimitiveStructure(model, column.totalUnit(), typeClass);
                }
                return new TotalArrayObjectStructure(model, column.totalUnit(), typeClass);
            }
        }

        if (column.lengthUnit() > 0) {
            if (Collection.class.isAssignableFrom(typeClass)) {
                return new LengthUnitCollectionStructure(model, column.lengthUnit());
            }
            return new LengthStructure(model, column.lengthUnit());
        }

        if (column.length() > 0) {
            return new LengthStructure(model, column.length());
        }
        if (Collection.class.isAssignableFrom(typeClass)) {
            return new CollectionStructure(model);
        } else if (Map.class.isAssignableFrom(typeClass)) {
            return new MapStructure((MapModel) model, typeClass);
        }
        return (BaseStructure) model;
    }

    public static int getLength(Class typeClass) {
        Integer len = NUMBER.get(typeClass.getName());
        if (len == null) {
            return -1;
        }
        return len;
    }
}
