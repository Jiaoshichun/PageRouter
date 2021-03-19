package com.yiqizuoye.library.page.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface PageRules {
    PageRule[] value();
}
