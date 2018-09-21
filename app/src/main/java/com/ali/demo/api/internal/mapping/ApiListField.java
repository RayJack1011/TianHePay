package com.ali.demo.api.internal.mapping;

/**
 * 数据结构列表属性注解。
 */
public @interface ApiListField {
    /**
     * 列表属性映射名称
     **/
    String value() default "";
}
