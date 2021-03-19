package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 */
public interface PageCode {
    //成功
    int SUCCESS = 0;
    //没有对应的key
    int ERROR_NO_FOUND_KEY = 1;
    //没有对应的类型
    int ERROR_NO_FOUND_TYPE = 2;
    //等待队列
    int QUEUE_WAITING = 5;
    //数据格式失败
    int ERROR_DATA_FORMAT = 3;
    //被拦截器中断
    int INTERRUPTED = 4;
}
