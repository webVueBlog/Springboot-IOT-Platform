package com.webVueBlog.common.annotation;

import java.lang.annotation.*;

/**
 * 字典格式化
 *
 * 实现将字典数据的值，格式化成字典数据的标签
 */
@Target({ElementType.FIELD})// Target Type.FIELD 表示注解可以用于属性（字段）
@Retention(RetentionPolicy.RUNTIME)// Retention Policy.RUNTIME 表示注解在程序运行期间存在
@Inherited
public @interface DictFormat {

    /**
     * 例如说，SysDictTypeConstants、InfDictTypeConstants
     *
     * @return 字典类型
     */
    String value();// 字典类型

}
