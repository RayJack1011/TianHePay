package com.tianhe.pay.data.crm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标注天和CRM系统中xml数据中的key.
 * 只是为了方便后期对比数据
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CrmDataName {
    String value();
}
