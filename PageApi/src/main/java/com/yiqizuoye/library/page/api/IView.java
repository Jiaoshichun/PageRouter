package com.yiqizuoye.library.page.api;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 * View接口的父类
 * 针对每个Page配置一个View接口，Presenter持有对应View接口。通过View接口和Page交互
 */
public interface IView<T> {
    void onCreate(T data, @Nullable Bundle otherData);


    void onDestroy();

    void onNewOpen(T data, @Nullable Bundle otherData);
}
