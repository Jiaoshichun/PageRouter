package com.yiqizuoye.library.page.api;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 * Presenter的父类。
 * {@link #mData} 需要需要的数据类型
 * {@link #mView} page实现的View接口
 */
public abstract class BasePresenter<DATA, V extends IView<DATA>> implements IView<DATA> {
    protected V mView;
    protected DATA mData;

    final void setView(V view) {
        this.mView = view;
    }

    @Override
    public void onCreate(DATA data,@Nullable Bundle otherData) {

    }


    @Override
    public void onNewOpen(DATA data, @Nullable Bundle otherData) {

    }


    @Override
    public void onDestroy() {

    }


}
