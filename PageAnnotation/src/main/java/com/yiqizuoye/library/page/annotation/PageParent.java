package com.yiqizuoye.library.page.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface PageParent {
    int value();

    /**
     * 默认1000 越大在上层，越小在底层
     * @return
     */
    int index() default 1000;
}
