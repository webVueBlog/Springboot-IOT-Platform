package com.webVueBlog.protocol.base.annotation;

import java.lang.annotation.*;

/**
 * 协议类型标注
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Protocol {

    int[] value() default {};//协议类型

    String desc() default "";
}
