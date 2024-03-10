package com.webVueBlog.base.core.annotation;

import java.lang.annotation.*;

/**
 * 异步处理设备数据
 *
 * 
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Async {

}
