package com.webVueBlog.protocol.util;

import java.util.Arrays;

public class CharsBuilder implements CharSequence, Appendable{// 实现CharSequence和Appendable接口

    private char[] value;// 存储字符序列的数组
    private int pos;// 当前字符位置

    public CharsBuilder(int length) {// 构造函数，初始化数组大小为length
        this.value = new char[length];// 创建数组
    }

    public CharsBuilder(char[] chars) {
        this.value = chars;
    }// 构造函数，将传入的字符数组赋值给value

    @Override
    public Appendable append(CharSequence s) {
        return append(s, 0, s.length());
    }// 实现Appendable接口的append方法，将字符序列s追加到当前字符序列的末尾

    @Override
    public Appendable append(CharSequence s, int start, int end) {
        // 实现Appendable接口的append方法，将字符序列s的一部分追加到当前字符序列的末尾
        int len = end - start;
        for (int i = start, j = pos; i < end; i++, j++) {// 将字符序列s的一部分复制到value数组中
            value[j] = s.charAt(i);// 这里使用value数组来存储字符序列，所以需要将字符复制到value数组中
        }
        pos += len;
        return this;
    }

    @Override
    public Appendable append(char c) {
        value[pos++] = c;// 将字符c追加到当前字符序列的末尾
        return this;
    }

    @Override
    public char charAt(int index) {
        return value[index];
    }// 实现CharSequence接口的charAt方法，返回指定索引处的字符

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start == end) {
            return new CharsBuilder(Math.min(16, value.length));// 如果start和end相等，则返回一个长度为16的字符序列
        }
        return new CharsBuilder(Arrays.copyOfRange(value, start, end));// 否则，返回一个子字符序列
    }

    @Override
    public int length() {
        return value.length;
    }// 实现CharSequence接口的length方法，返回字符序列的长度

    @Override
    public String toString() {
        return new String(value);
    }// 实现CharSequence接口的toString方法，返回字符序列的字符串表示

    public String leftStrip(char c) {// 实现leftStrip方法，返回字符序列从左往右第一个不在字符c之前的子字符序列
        int i = leftOf(value, c);// 实现leftOf方法，返回字符序列从左往右第一个不在字符c之前的索引
        return new String(value, i, value.length - i);// 返回一个新的字符序列，从索引i开始，到字符序列的末尾
    }

    public String rightStrip(char c) {
        int i = rightOf(value, c);// 实现rightOf方法，返回字符序列从右往左第一个不在字符c之前的索引
        return new String(value, 0, i);// 返回一个新的字符序列，从字符序列的起始位置开始，到索引i结束
    }

    public static int leftOf(char[] chars, char pad) {
        int i = 0, len = chars.length;// 实现leftOf方法，返回字符序列从左往右第一个不在字符c之前的索引
        while (i < len && chars[i] == pad) {// 实现leftOf方法，返回字符序列从左往右第一个不在字符c之前的索引
            i++;
        }
        return i;
    }

    public static int rightOf(char[] chars, char pad) {
        int i = 0, len = chars.length;// 实现rightOf方法，返回字符序列从右往左第一个不在字符c之前的索引
        while ((i < len) && (chars[len - 1] <= pad)) {// 实现rightOf方法，返回字符序列从右往左第一个不在字符c之前的索引
            len--;
        }
        return len;
    }
}