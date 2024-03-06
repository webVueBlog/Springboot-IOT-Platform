package com.webVueBlog.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解集用来定义Excel列的属性，示例：
 * @Excels(Excel(name="姓名",columnWidth = 20,orderNum = 1))
 * private String name;
 * @Excels(Excel(name="性别",columnWidth = 20,orderNum = 2))
 * private String sex;
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Excels
{
    public Excel[] value();// Excel集合
}
