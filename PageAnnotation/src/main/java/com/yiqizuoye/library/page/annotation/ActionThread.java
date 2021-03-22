package com.yiqizuoye.library.page.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD})
public @interface ActionThread {
    Thread value() default Thread.defaultThread;

    enum Thread {
        /**
         * 工作线程
         */
        workThread,
        /**
         * 主线程
         */
        mainThread,
        /**
         * 主线程异步
         */
        mainThreadAsync,
        /**
         * 默认线程
         */
        defaultThread
    }
}
