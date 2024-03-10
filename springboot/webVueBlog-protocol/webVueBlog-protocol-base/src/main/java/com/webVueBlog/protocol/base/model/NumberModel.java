package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import io.netty.buffer.ByteBuf;

public class NumberModel {

    public static final WModel<Boolean> BOOL = new BOOL();// 布尔
    public static final WModel<Character> CHAR = new CHAR();// 字符
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
        public Boolean readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readBoolean();
        }

        @Override
        public void writeTo(ByteBuf output, Boolean value) {
            if (value != null) {
                output.writeBoolean(value);
            }
        }
    }

    protected static class CHAR extends BaseStructure<Character> {
        @Override
        public Character readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readChar();
        }

        @Override
        public void writeTo(ByteBuf output, Character value) {
            if (value != null) {
                output.writeChar(value);
            }
        }
    }

    protected static class BYTE2Byte extends BaseStructure<Byte> {
        @Override
        public Byte readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readByte();
        }

        @Override
        public void writeTo(ByteBuf output, Byte value) {
            if (value != null) {
                output.writeByte(value);
            }
        }
    }

    protected static class BYTE2Short extends BaseStructure<Short> {
        @Override
        public Short readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf output, Short value) {
            if (value != null) {
                output.writeByte(value);
            }
        }
    }

    protected static class BYTE2Int extends BaseStructure<Integer> {
        @Override
        public Integer readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return (int) input.readUnsignedByte();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            if (value != null) {
                output.writeByte(value);
            }
        }
    }

    protected static class WORD2Short extends BaseStructure<Short> {
        @Override
        public Short readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readShort();
        }

        @Override
        public void writeTo(ByteBuf output, Short value) {
            if (value != null) {
                output.writeShort(value);
            }
        }
    }

    protected static class WORD2Int extends BaseStructure<Integer> {

        @Override
        public void readAndSet(ByteBuf input, Object obj) throws Exception {
            super.readAndSet(input, obj);
        }

        @Override
        public void getAndWrite(ByteBuf output, Object obj) throws Exception {
            super.getAndWrite(output, obj);
        }

        @Override
        public Integer readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readUnsignedShort();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            if (value != null) {
                output.writeShort(value);
            }
        }
    }

    protected static class DWORD2Int extends BaseStructure<Integer> {
        @Override
        public Integer readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readInt();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            if (value != null) {
                output.writeInt(value);
            }
        }
    }

    protected static class DWORD2Long extends BaseStructure<Long> {
        @Override
        public Long readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readUnsignedInt();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            if (value != null) {
                output.writeInt(value.intValue());
            }
        }
    }

    protected static class DWORD2Float extends BaseStructure<Float> {
        @Override
        public Float readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readFloat();
        }

        @Override
        public void writeTo(ByteBuf output, Float value) {
            if (value != null) {
                output.writeFloat(value);
            }
        }
    }

    protected static class QWORD2Long extends BaseStructure<Long> {
        @Override
        public Long readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readLong();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            if (value != null) {
                output.writeLong(value);
            }
        }
    }

    protected static class QWORD2Double extends BaseStructure<Double> {
        @Override
        public Double readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readDouble();
        }

        @Override
        public void writeTo(ByteBuf output, Double value) {
            if (value != null) {
                output.writeDouble(value);
            }
        }
    }

    protected static class WORD2ShortLE extends BaseStructure<Short> {
        @Override
        public Short readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readShortLE();
        }

        @Override
        public void writeTo(ByteBuf output, Short value) {
            if (value != null) {
                output.writeShortLE(value);
            }
        }
    }

    protected static class WORD2IntLE extends BaseStructure<Integer> {
        @Override
        public Integer readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readUnsignedShortLE();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            if (value != null) {
                output.writeShortLE(value);
            }
        }
    }

    protected static class DWORD2IntLE extends BaseStructure<Integer> {
        @Override
        public Integer readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readIntLE();
        }

        @Override
        public void writeTo(ByteBuf output, Integer value) {
            if (value != null) {
                output.writeIntLE(value);
            }
        }
    }

    protected static class DWORD2LongLE extends BaseStructure<Long> {
        @Override
        public Long readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readUnsignedIntLE();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            if (value != null) {
                output.writeIntLE(value.intValue());
            }
        }
    }

    protected static class DWORD2FloatLE extends BaseStructure<Float> {
        @Override
        public Float readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readFloatLE();
        }

        @Override
        public void writeTo(ByteBuf output, Float value) {
            if (value != null) {
                output.writeFloatLE(value);
            }
        }
    }

    protected static class QWORD2LongLE extends BaseStructure<Long> {
        @Override
        public Long readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readLongLE();
        }

        @Override
        public void writeTo(ByteBuf output, Long value) {
            if (value != null) {
                output.writeLongLE(value);
            }
        }
    }

    protected static class QWORD2DoubleLE extends BaseStructure<Double> {
        @Override
        public Double readFrom(ByteBuf input) {
            if (!input.isReadable()) {
                return null;
            }
            return input.readDoubleLE();
        }

        @Override
        public void writeTo(ByteBuf output, Double value) {
            if (value != null) {
                output.writeDoubleLE(value);
            }
        }
    }
}
