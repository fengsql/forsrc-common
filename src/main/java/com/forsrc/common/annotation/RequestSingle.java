package com.forsrc.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestSingle {

  String value();

  boolean required() default true;

  String defaultValue() default "";
}