package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import io.netty.buffer.ByteBuf;


public class NumberPModel {

    public static final WModel<Boolean> BOOL = new BOOL();
    public static final WModel<Character> CHAR = new CHAR();
    public static final WModel<Byte> BYTE_BYTE = new BYTE2Byte();
    public static final WModel<Short> BYTE_SHORT = new BYTE2Short();
    public static final WModel<Integer> BYTE_INT = new BYTE2Int();

    public static final WModel<Short> WORD_SHORT = new WORD2Short();
    public static final WModel<Integer> WORD_INT = new WORD2Int();
    public static final WModel<Integer> DWORD_INT = new DWORD2Int();
    public static final WModel<Long> DWORD_LONG = new DWORD2Long();
    public static final WModel<Float> DWORD_FLOAT = new DWORD2Float();
    public static final WModel<Long> QWORD_LONG = new QWORD2Long();
    public static final WModel<Double> QWORD_DOUBLE = new QWORD2Double();

    public static final WModel<Short> WORD_SHORT_LE = new WORD2ShortLE();
    public static final WModel<Integer> WORD_INT_LE = new WORD2IntLE();
    public static final WModel<Integer> DWORD_INT_LE = new DWORD2IntLE();
    public static final WModel<Long> DWORD_LONG_LE = new DWORD2LongLE();
    public static final WModel<Float> DWORD_FLOAT_LE = new DWORD2FloatLE();
    public static final WModel<Long> QWORD_LONG_LE = new QWORD2LongLE();
    public static final WModel<Double> QWORD_DOUBLE_LE = new QWORD2DoubleLE();

    protected static class BOOL extends BaseStructure<Boolean> {

        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setBoolean(obj, in.readBoolean());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeBoolean(field.getBoolean(obj));
        }

        @Override
        public Boolean readFrom(ByteBuf in) {
            return in.readBoolean();
        }

        @Override
        public void writeTo(ByteBuf out, Boolean value) {
            out.writeBoolean(value);
        }
    }

    protected static class CHAR extends BaseStructure<Character> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setChar(obj, in.readChar());
        }


        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeChar(field.getChar(obj));
        }

        @Override
        public Character readFrom(ByteBuf in) {
            return in.readChar();
        }

        @Override
        public void writeTo(ByteBuf out, Character value) {
            out.writeChar(value);
        }
    }

    protected static class BYTE2Byte extends BaseStructure<Byte> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setByte(obj, in.readByte());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeByte(field.getByte(obj));
        }

        @Override
        public Byte readFrom(ByteBuf in) {
            return in.readByte();
        }

        @Override
        public void writeTo(ByteBuf out, Byte value) {
            out.writeByte(value);
        }
    }

    protected static class BYTE2Short extends BaseStructure<Short> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setShort(obj, in.readUnsignedByte());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeByte(field.getShort(obj));
        }

        @Override
        public Short readFrom(ByteBuf in) {
            return in.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf out, Short value) {
            out.writeByte(value);
        }
    }

    protected static class BYTE2Int extends BaseStructure<Integer> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setInt(obj, in.readUnsignedByte());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeByte(field.getInt(obj));
        }

        @Override
        public Integer readFrom(ByteBuf in) {
            return (int) in.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf out, Integer value) {
            out.writeByte(value);
        }
    }

    protected static class WORD2Short extends BaseStructure<Short> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setShort(obj, in.readShort());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeShort(field.getShort(obj));
        }

        @Override
        public Short readFrom(ByteBuf in) {
            return in.readShort();
        }

        @Override
        public void writeTo(ByteBuf out, Short value) {
            out.writeShort(value);
        }
    }

    protected static class WORD2Int extends BaseStructure<Integer> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setInt(obj, in.readUnsignedShort());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeShort(field.getInt(obj));
        }

        @Override
        public Integer readFrom(ByteBuf in) {
            return in.readUnsignedShort();
        }

        @Override
        public void writeTo(ByteBuf out, Integer value) {
            out.writeShort(value);
        }
    }

    protected static class DWORD2Int extends BaseStructure<Integer> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setInt(obj, in.readInt());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeInt(field.getInt(obj));
        }

        @Override
        public Integer readFrom(ByteBuf in) {
            return in.readInt();
        }

        @Override
        public void writeTo(ByteBuf out, Integer value) {
            out.writeInt(value);
        }
    }

    protected static class DWORD2Long extends BaseStructure<Long> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setLong(obj, in.readUnsignedInt());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeInt((int) field.getLong(obj));
        }

        @Override
        public Long readFrom(ByteBuf in) {
            return in.readUnsignedInt();
        }

        @Override
        public void writeTo(ByteBuf out, Long value) {
            out.writeInt(value.intValue());
        }
    }

    protected static class DWORD2Float extends BaseStructure<Float> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setFloat(obj, in.readFloat());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeFloat(field.getFloat(obj));
        }

        @Override
        public Float readFrom(ByteBuf in) {
            return in.readFloat();
        }

        @Override
        public void writeTo(ByteBuf out, Float value) {
            out.writeFloat(value);
        }
    }

    protected static class QWORD2Long extends BaseStructure<Long> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setLong(obj, in.readLong());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeLong(field.getLong(obj));
        }

        @Override
        public Long readFrom(ByteBuf in) {
            return in.readLong();
        }

        @Override
        public void writeTo(ByteBuf out, Long value) {
            out.writeLong(value);
        }
    }

    protected static class QWORD2Double extends BaseStructure<Double> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setDouble(obj, in.readDouble());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeDouble(field.getDouble(obj));
        }

        @Override
        public Double readFrom(ByteBuf in) {
            return in.readDouble();
        }

        @Override
        public void writeTo(ByteBuf out, Double value) {
            out.writeDouble(value);
        }
    }

    protected static class WORD2ShortLE extends BaseStructure<Short> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setShort(obj, in.readShortLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeShortLE(field.getShort(obj));
        }

        @Override
        public Short readFrom(ByteBuf in) {
            return in.readShortLE();
        }

        @Override
        public void writeTo(ByteBuf out, Short value) {
            out.writeShortLE(value);
        }
    }

    protected static class WORD2IntLE extends BaseStructure<Integer> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setInt(obj, in.readUnsignedShortLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeShortLE(field.getInt(obj));
        }

        @Override
        public Integer readFrom(ByteBuf in) {
            return in.readUnsignedShortLE();
        }

        @Override
        public void writeTo(ByteBuf out, Integer value) {
            out.writeShortLE(value);
        }
    }

    protected static class DWORD2IntLE extends BaseStructure<Integer> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setInt(obj, in.readIntLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeIntLE(field.getInt(obj));
        }

        @Override
        public Integer readFrom(ByteBuf in) {
            return in.readIntLE();
        }

        @Override
        public void writeTo(ByteBuf out, Integer value) {
            out.writeIntLE(value);
        }
    }

    protected static class DWORD2LongLE extends BaseStructure<Long> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setLong(obj, in.readUnsignedIntLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeIntLE((int) field.getLong(obj));
        }

        @Override
        public Long readFrom(ByteBuf in) {
            return in.readUnsignedIntLE();
        }

        @Override
        public void writeTo(ByteBuf out, Long value) {
            out.writeIntLE(value.intValue());
        }
    }

    protected static class DWORD2FloatLE extends BaseStructure<Float> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setFloat(obj, in.readFloatLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeFloatLE(field.getFloat(obj));
        }

        @Override
        public Float readFrom(ByteBuf in) {
            return in.readFloatLE();
        }

        @Override
        public void writeTo(ByteBuf out, Float value) {
            out.writeFloatLE(value);
        }
    }

    protected static class QWORD2LongLE extends BaseStructure<Long> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setLong(obj, in.readLongLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeLongLE(field.getLong(obj));
        }

        @Override
        public Long readFrom(ByteBuf in) {
            return in.readLongLE();
        }

        @Override
        public void writeTo(ByteBuf out, Long value) {
            out.writeLongLE(value);
        }
    }

    protected static class QWORD2DoubleLE extends BaseStructure<Double> {
        @Override
        public void readAndSet(ByteBuf in, Object obj) throws Exception {
            field.setDouble(obj, in.readDoubleLE());
        }

        @Override
        public void getAndWrite(ByteBuf out, Object obj) throws Exception {
            out.writeDoubleLE(field.getDouble(obj));
        }

        @Override
        public Double readFrom(ByteBuf in) {
            return in.readDoubleLE();
        }

        @Override
        public void writeTo(ByteBuf out, Double value) {
            out.writeDoubleLE(value);
        }
    }
    
}
