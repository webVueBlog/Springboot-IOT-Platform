package com.webVueBlog.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.webVueBlog.common.enums.BusinessType;
import com.webVueBlog.common.enums.OperatorType;

/**
 * 自定义操作日志记录注解
 * 
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })//方法注解 ElementType.PARAMETER 方法参数注解
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * 模块 
     */
    public String title() default "";

    /**
     * 功能
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    public OperatorType operatorType() default OperatorType.MANAGE;// manage 管理员  other 其他

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    public boolean isSaveResponseData() default true;
}
