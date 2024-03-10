package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import io.netty.buffer.ByteBuf;

/**
 *  数组模型
 */
public class ArrayModel {
    public static final WModel<char[]> CHARS = new CharArray();// 字符数组
    public static final WModel<byte[]> BYTES = new ByteArray();// 字节数组
    public static final WModel<short[]> SHORTS = new ShortArray();// 短整型数组
    public static final WModel<int[]> INTS = new IntArray();// 整型数组
    public static final WModel<float[]> FLOATS = new FloatArray();// 单精度浮点型数组
    public static final WModel<long[]> LONGS = new LongArray();// 长整型数组
    public static final WModel<double[]> DOUBLES = new DoubleArray();// 双精度浮点型数组

    protected static class ByteArray extends BaseStructure<byte[]> {// 字节数组
        @Override
        public byte[] readFrom(ByteBuf input) {// 读取字节数组
            byte[] array = new byte[input.readableBytes()];// 读取可读字节数
            input.readBytes(array);// 读取可读字节到数组
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, byte[] array) {// 写入字节数组
            if (array == null) {// 如果数组为空
                return;
            }
            output.writeBytes(array);// 写入数组到输出
        }
    }

    protected static class CharArray extends BaseStructure<char[]> {// 字符数组
        @Override
        public char[] readFrom(ByteBuf input) {// 读取字符数组
            int total = input.readableBytes() >> 1;// 读取可读字节数，除以2得到字符数
            char[] array = new char[total];// 创建字符数组
            for (int i = 0; i < total; i++) {// 读取可读字节到数组
                array[i] = input.readChar();// 读取可读字节到数组
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, char[] array) {// 写入字符数组
            if (array == null) {// 如果数组为空
                return;
            }
            for (int i = 0; i < array.length; i++) {// 写入数组到输出
                output.writeChar(array[i]);// 写入数组到输出
            }
        }
    }

    protected static class ShortArray extends BaseStructure<short[]> {// 短整型数组
        @Override
        public short[] readFrom(ByteBuf input) {// 读取短整型数组
            int total = input.readableBytes() >> 1;// 读取可读字节数，除以2得到短整型数组的长度
            short[] array = new short[total];// 创建短整型数组
            for (int i = 0; i < total; i++) {// 读取可读字节到数组
                array[i] = input.readShort();// 读取可读字节到数组
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, short[] array) {// 写入短整型数组
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {// 写入数组到输出
                output.writeShort(array[i]);// 写入数组到输出
            }
        }
    }

    protected static class IntArray extends BaseStructure<int[]> {// 整型数组
        @Override
        public int[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            int[] array = new int[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readInt();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, int[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeInt(array[i]);
            }
        }
    }

    protected static class LongArray extends BaseStructure<long[]> {
        @Override
        public long[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            long[] array = new long[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readLong();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, long[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeLong(array[i]);
            }
        }
    }

    protected static class FloatArray extends BaseStructure<float[]> {
        @Override
        public float[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 2;
            float[] array = new float[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readFloat();
            }
            return array;
        }


        @Override
        public void writeTo(ByteBuf output, float[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeFloat(array[i]);
            }
        }
    }

    protected static class DoubleArray extends BaseStructure<double[]> {
        @Override
        public double[] readFrom(ByteBuf input) {
            int total = input.readableBytes() >> 3;
            double[] array = new double[total];
            for (int i = 0; i < total; i++) {
                array[i] = input.readDouble();
            }
            return array;
        }

        @Override
        public void writeTo(ByteBuf output, double[] array) {
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.length; i++) {
                output.writeDouble(array[i]);
            }
        }
    }
}
