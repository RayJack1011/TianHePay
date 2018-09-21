package com.tianhe.pay.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface PerFragment {
}
