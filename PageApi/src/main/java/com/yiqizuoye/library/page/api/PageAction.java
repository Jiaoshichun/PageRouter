package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
public interface PageAction<T> {
    void handleEvent(T data);
}
