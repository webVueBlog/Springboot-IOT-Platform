package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import com.webVueBlog.protocol.base.struc.LengthStructure;
import com.webVueBlog.protocol.util.CharsBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class StringModel {
    public static final WModel<String> HEX = new HEX(-1);//16进制
    public static final WModel<String> BCD = new BCD(-1);//BCD码
    public static final WModel<String> GBK = new STR(Charset.forName("GBK"), -1);//GBK编码
    public static final WModel<String> UTF8 = new STR(StandardCharsets.UTF_8, -1);//UTF-8编码
    public static final WModel<String> ASCII = new STR(StandardCharsets.US_ASCII, -1);//ASCII编码

    public static BaseStructure<String> getInstance(String charset, int length, int lengthUnit) {
        final String cs = charset.toUpperCase();//编码
        BaseStructure<String> model;//模型
        if ("BCD".equals(cs)) {//BCD码
            model = new BCD(length);//BCD码
        } else if ("HEX".equals(cs)) {//16进制
            model = new HEX(length);//16进制
        } else {
            model = new STR(Charset.forName(charset), length);//其他编码
        }

        if (lengthUnit > 0) {
            model = new LengthStructure<>(model, lengthUnit);//长度结构
        }

        return model;
    }

    public static class STR extends BaseStructure<String> {
        private static final ByteBuffer EMPTY = ByteBuffer.allocate(0);//空字节缓冲
        private final Charset charset;//字符集
        private final int length;//长度
        private final boolean fixed;//是否固定长度

        private STR(Charset charset, int length) {//构造方法
            this.charset = charset;//字符集
            this.length = length;//长度
            this.fixed = length > -1;//是否固定长度
        }

        @Override
        public String readFrom(ByteBuf input) {
            int len = input.readableBytes();//可读字节数

            if (fixed && len > length)//固定长度，但可读字节数大于长度
            {
                len = length;//截取固定长度
            }

            byte[] bytes = new byte[len];//字节数组
            input.readBytes(bytes);//读取字节数组

            int st = 0;
            while ((st < len) && (bytes[st] == 0)) {//跳过前导0
                st++;
            }
            while ((st < len) && (bytes[len - 1] == 0)) {//跳过后导0
                len--;
            }
            return new String(bytes, st, len - st, charset);//字符串
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            if (fixed) {
                ByteBuffer buffer;//字节缓冲
                if (value == null) {//值为null
                    buffer = EMPTY;//空字节缓冲
                } else {
                    buffer = charset.encode(value);//编码为字节数组
                }

                int srcPos = length - buffer.limit();//可读字节数

                if (srcPos > 0) {
                    output.writeBytes(buffer);//写入字节数组
                    output.writeBytes(new byte[srcPos]);
                } else if (srcPos < 0) {
                    buffer.position(-srcPos);//跳过前导0
                    output.writeBytes(buffer);//写入字节数组
                } else {
                    output.writeBytes(buffer);//写入字节数组
                }
            } else {
                if (value != null) {
                    output.writeBytes(charset.encode(value));//编码为字节数组
                }
            }
        }
    }

    public static class HEX extends BaseStructure<String> {
        protected final int length;//长度
        protected final int charSize;//字符长度
        protected final boolean fixed;//是否固定长度

        public HEX(int length) {//构造函数
            this.length = length;//长度
            this.charSize = length << 1;//字符长度
            this.fixed = length > -1;//是否固定长度
        }

        @Override
        public String readFrom(ByteBuf input) {//读取
            return readCharsBuilder(input).toString();//转换为字符串
        }

        protected CharsBuilder readCharsBuilder(ByteBuf input) {//读取
            int len = fixed ? length : input.readableBytes();//长度
            byte[] bytes = new byte[len];//字节数组
            input.readBytes(bytes);//读取字节数组

            CharsBuilder cb = new CharsBuilder(charSize);//构建字符串构建器
            StringUtil.toHexStringPadded(cb, bytes);//转换为16进制字符串
            return cb;//返回
        }

        @Override
        public void writeTo(ByteBuf output, String value) {
            if (value == null) {//如果值为空
                if (fixed) {//如果固定长度
                    output.writeBytes(new byte[length]);//写入空字节数组
                }
                return;
            }

            int charSize = this.charSize;//字符长度
            int strLength = value.length();//字符串长度
            if (!fixed) {
                charSize = strLength + (strLength & 1);//如果非固定长度，计算字符长度
            }

            char[] chars = new char[charSize];
            int i = charSize - strLength;//计算需要填充的字符数量
            if (i >= 0) {
                value.getChars(0, charSize - i, chars, i);//复制字符串到数组
                while (i > 0) {//如果需要填充
                    chars[--i] = '0';//填充字符
                }
            } else {
                value.getChars(-i, charSize - i, chars, 0);//复制字符串到数组
            }
            byte[] src = StringUtil.decodeHexDump(new CharsBuilder(chars));//转换为字节数组
            output.writeBytes(src);//写入字节数组
        }
    }

    public static class BCD extends HEX {
        public BCD(int length) {//构造函数
            super(length);
        }

        @Override
        public String readFrom(ByteBuf input) {//读取函数
            return readCharsBuilder(input).leftStrip('0');//移除前导0
        }
    }
}
