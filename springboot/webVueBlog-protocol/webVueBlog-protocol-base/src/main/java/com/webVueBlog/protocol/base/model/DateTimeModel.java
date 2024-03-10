package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import com.webVueBlog.protocol.util.DateTool;
import io.netty.buffer.ByteBuf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DateTimeModel是一个模型，用于处理日期和时间相关的数据。
 */
public class DateTimeModel {

    public static final WModel<LocalTime> BYTE_TIME = new Time(DateTool.BYTE);// 创建一个新的Time模型，使用BYTE模式。
    public static final WModel<LocalDate> BYTE_DATE = new Date(DateTool.BYTE);// 创建一个新的Date模型，使用BYTE模式。
    public static final WModel<LocalDateTime> BYTE_DATETIME = new DateTime(DateTool.BYTE);// 创建一个新的DateTime模型，使用BYTE模式。

    public static final WModel<LocalTime> BCD_TIME = new Time(DateTool.BCD);// 创建一个新的Time模型，使用BCD模式。
    public static final WModel<LocalDate> BCD_DATE = new Date(DateTool.BCD);// 创建一个新的Date模型，使用BCD模式。
    public static final WModel<LocalDateTime> BCD_DATETIME = new DateTime(DateTool.BCD);

    protected static class DateTime extends BaseStructure<LocalDateTime> {
        protected final DateTool tool;

        protected DateTime(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDateTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[6];// 读取6个字节的数据。
            input.readBytes(bytes);
            return tool.toDateTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDateTime value) {
            output.writeBytes(tool.from(value));
        }
    }

    protected static class Date extends BaseStructure<LocalDate> {
        protected final DateTool tool;// 创建一个新的Date模型，使用BYTE模式。

        protected Date(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalDate readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];
            input.readBytes(bytes);
            return tool.toDate(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalDate value) {
            output.writeBytes(tool.from(value));
        }
    }

    /**
     *  16进制表示的秒级时间戳
     */
    protected static class Time extends BaseStructure<LocalTime> {
        protected final DateTool tool;

        protected Time(DateTool tool) {
            this.tool = tool;
        }

        @Override
        public LocalTime readFrom(ByteBuf input) {
            byte[] bytes = new byte[3];// 3 bytes
            input.readBytes(bytes);// 读取3个字节
            return tool.toTime(bytes);
        }

        @Override
        public void writeTo(ByteBuf output, LocalTime value) {
            output.writeBytes(tool.from(value));// 写入3个字节
        }
    }
}
