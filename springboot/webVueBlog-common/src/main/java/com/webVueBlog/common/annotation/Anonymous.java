package com.webVueBlog.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 匿名访问不鉴权注解
 * 
 */
@Target({ ElementType.METHOD, ElementType.TYPE })//Target可以用于方法，类，接口 METHOD是用于方法，TYPE是用于类，接口
@Retention(RetentionPolicy.RUNTIME)//RetentionPolicy.RUNTIME是VM在运行时可以获取到，CLASS是VM加载时可以获取到，SOURCE是源码阶段
@Documented//表示生成javadoc时会包含注解信息
public @interface Anonymous//自定义注解
{
}
