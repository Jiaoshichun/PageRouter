package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 * 事件路由基类
 */
public interface BaseAction<T> {
    void handleEvent(T data);
}
