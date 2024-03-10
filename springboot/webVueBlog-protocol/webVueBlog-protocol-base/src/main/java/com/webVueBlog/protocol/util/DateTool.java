package com.webVueBlog.protocol.util;

import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 时间编码工具
 */
public class DateTool {

    public static final DateTool BYTE = new DateTool();// 字节
    public static final DateTool BCD = new BCD();// BCD

    public static final int YEAR = LocalDate.now().getYear();// 当前年份
    public static final int YEAR_RANGE = YEAR - 30;// 当前年份-30
    public static final int HUNDRED_YEAR = YEAR_RANGE / 100 * 100;// 当前年份-100

    public static int getYear(int year) {// 获取年份
        year += HUNDRED_YEAR;// 当前年份-100
        if (year < YEAR_RANGE) {// 当前年份-30 小于当前年份-100
            year += 100;// 当前年份-30 小于当前年份-100 则年份+100
        }
        return year;// 返回年份
    }

    /** 时间转byte[] (yyMMddHHmmss) */
    public final byte[] from(LocalDateTime dateTime) {// 获取byte[]
        byte[] bytes = new byte[6];// 6个字节
        bytes[0] = toByte(dateTime.getYear() % 100);// 年份
        bytes[1] = toByte(dateTime.getMonthValue());// 月份
        bytes[2] = toByte(dateTime.getDayOfMonth());// 日
        bytes[3] = toByte(dateTime.getHour());// 小时
        bytes[4] = toByte(dateTime.getMinute());// 分钟
        bytes[5] = toByte(dateTime.getSecond());// 秒
        return bytes;
    }

    /** byte[]转时间 (yyMMddHHmmss) */
    public final LocalDateTime toDateTime(byte[] bytes) {
        try {
            return LocalDateTime.of(
                    getYear(toInt(bytes[0])),
                    toInt(bytes[1]),
                    toInt(bytes[2]),
                    toInt(bytes[3]),
                    toInt(bytes[4]),
                    toInt(bytes[5]));
        } catch (Exception e) {
            return null;
        }
    }

    /** 日期转byte[] (yyMMdd) */
    public final byte[] from(LocalDate date) {
        return new byte[]{
                toByte((date.getYear() % 100)),
                toByte(date.getMonthValue()),
                toByte(date.getDayOfMonth())
        };
    }

    /** byte[]转日期 (yyMMdd) */
    public final LocalDate toDate(byte[] bytes) {
        return LocalDate.of(
                getYear(toInt(bytes[0])),
                toInt(bytes[1]),
                toInt(bytes[2])
        );
    }

    /** 时间转byte[] (HHmmss) */
    public final byte[] from(LocalTime time) {
        return new byte[]{
                toByte(time.getHour()),
                toByte(time.getMinute()),
                toByte(time.getSecond())
        };
    }

    /** byte[]转时间 (HHmmss) */
    public final LocalTime toTime(byte[] bytes) {
        return LocalTime.of(
                toInt(bytes[0]),
                toInt(bytes[1]),
                toInt(bytes[2])
        );
    }

    /** 写入2位时间(HHmm) */
    public final void writeTime2(ByteBuf output, LocalTime time) {
        output.writeByte(toByte(time.getHour())).writeByte(toByte(time.getMinute()));
    }

    /** 读取2位时间(HHmm) */
    public final LocalTime readTime2(ByteBuf input) {
        return LocalTime.of(toInt(input.readByte()), toInt(input.readByte()));
    }

    private DateTool() {
    }

    public byte toByte(int i) {
        return (byte) i;
    }

    public int toInt(byte b) {
        return b & 0xff;
    }

    private static class BCD extends DateTool {
        @Override
        public byte toByte(int i) {
            return (byte) ((i / 10 << 4) | (i % 10 & 0xf));
        }

        @Override
        public int toInt(byte b) {
            return (b >> 4 & 0xf) * 10 + (b & 0xf);
        }
    }
}
