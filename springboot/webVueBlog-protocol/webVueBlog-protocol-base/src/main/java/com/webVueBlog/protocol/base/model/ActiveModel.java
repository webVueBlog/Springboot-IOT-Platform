package com.webVueBlog.protocol.base.model;

import com.webVueBlog.protocol.base.struc.BaseStructure;
import com.webVueBlog.protocol.util.ExplainUtils;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

/**
 * Java中model文件夹是用来存放运行时根据Class生成的消息结构model，序列化对象
 * 运行时根据Class生成的消息结构model，序列化对象
 */
public class ActiveModel<T> implements WModel<T>{

    protected int version;//版本号
    protected int length;//消息长度
    protected Class<T> typeClass;//消息类型
    protected BaseStructure[] structures;//消息结构
    protected Constructor<T> constructor;//构造函数

    public ActiveModel(Class<T> typeClass, int version, BaseStructure[] structures) {//构造函数
        this.typeClass = typeClass;//消息类型
        this.version = version;//版本号
        this.structures = structures;//消息结构
        int length = 0;//消息长度
        for (BaseStructure structure : structures) {//遍历消息结构
            length += structure.length();//累加消息结构长度
        }
        this.length = length;//消息长度
        try {
            this.constructor = typeClass.getDeclaredConstructor((Class[]) null);//获取构造函数
            // getDeclaredConstructor表示获取当前类声明的构造函数，getConstructor表示获取当前类声明的公共构造函数
            // 返回一个 Constructor 对象，它反映此 Class 对象所表示的类的指定公共构造方法。
        } catch (Exception e) {
            throw new RuntimeException(e);//抛出运行时异常
        }
    }

    public T newInstance() {//创建消息对象
        try {
            return constructor.newInstance((Object[]) null);//使用构造函数创建消息对象
        } catch (Exception e) {
            throw new RuntimeException("newInstance failed " + typeClass.getName(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result) {//合并消息
        int i = 0;//消息索引
        try {
            for (; i < structures.length; i++) {//遍历消息结构
                structures[i].readAndSet(input, result);//readAndSet是消息结构读取和设置方法
            }
            return result;//返回消息对象
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    public T mergeFrom(ByteBuf input, T result, ExplainUtils explain) {//合并消息
        int i = 0;
        try {
            if (explain == null) {//如果explain为空
                for (; i < structures.length; i++) {
                    structures[i].readAndSet(input, result);//读取和设置消息结构
                }
            } else {
                for (; i < structures.length; i++) {
                    structures[i].readAndSet(input, result, explain);//读取和设置消息结构，并使用explain进行解释
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public T readFrom(ByteBuf input) {//读取消息
        int i = 0;
        try {
            T result = constructor.newInstance((Object[]) null);//创建消息对象
            for (; i < structures.length; i++) {
                structures[i].readAndSet(input, result);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public T readFrom(ByteBuf input, ExplainUtils explain) {
        int i = 0;
        try {
            T result = constructor.newInstance((Object[]) null);//创建消息对象
            if (explain == null) {
                for (; i < structures.length; i++) {
                    structures[i].readAndSet(input, result);
                }
            } else {
                for (; i < structures.length; i++) {
                    structures[i].readAndSet(input, result, explain);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Read failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message) {//将消息写入ByteBuf
        int i = 0;
        try {
            for (; i < structures.length; i++) {
                structures[i].getAndWrite(output, message);//获取字段值并写入ByteBuf
            }
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    @Override
    public void writeTo(ByteBuf output, T message, ExplainUtils explain) {
        int i = 0;
        try {
            if (explain == null) {
                for (; i < structures.length; i++) {
                    structures[i].getAndWrite(output, message);
                }
            } else {
                for (; i < structures.length; i++) {
                    structures[i].getAndWrite(output, message, explain);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Write failed " + i + " " + typeClass.getName() + " " + structures[i].filedName(), e);
        }
    }

    public Class<T> typeClass() {//获取消息类型
        return typeClass;
    }

    public int version() {//获取消息版本
        return version;
    }

    @Override
    public int length() {//获取消息长度
        return length;
    }

    @Override
    public String toString() {//获取消息信息
        final StringBuilder sb = new StringBuilder(48);//创建 StringBuilder 对象
        sb.append("{typeClass=").append(typeClass.getSimpleName());//添加消息类型到 StringBuilder 对象
        sb.append(", version=").append(version);//添加消息版本到 StringBuilder 对象
        sb.append(", length=").append(length);//添加消息长度到 StringBuilder 对象
        sb.append('}');//添加结束符到 StringBuilder 对象
        return sb.toString();//返回 StringBuilder 对象的字符串表示形式
    }
}
