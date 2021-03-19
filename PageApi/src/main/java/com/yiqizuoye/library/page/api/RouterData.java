package com.yiqizuoye.library.page.api;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.yiqizuoye.library.page.annotation.PageDataTransform;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 * 跳转数据
 */
public class RouterData {
    public RouterData(int type, String key, Object data) {
        this.type = type;
        this.key = key;
        this.data = data;
    }

    //是否被执行过，避免队列消息重复执行
    public boolean isExecuted;
    private final int type;
    private final String key;
    private final Object data;
    //页面结果回调 在页面调用setResult时会回调该方法
    @Nullable
    private PageResultCallBack pageResultCallBack;
    //其他传递的数据
    @Nullable
    private Bundle otherData;
    //拦截器
    @Nullable
    private PageInterceptor interceptor;
    //格式转换
    @Nullable
    private PageDataTransform transform;

    public int getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public Object getData() {
        return data;
    }

    @Nullable
    public PageResultCallBack getPageResultCallBack() {
        return pageResultCallBack;
    }

    public void setPageResultCallBack(@Nullable PageResultCallBack pageResultCallBack) {
        this.pageResultCallBack = pageResultCallBack;
    }

    @Nullable
    public Bundle getOtherData() {
        return otherData;
    }

    public void setOtherData(@Nullable Bundle otherData) {
        this.otherData = otherData;
    }

    @Nullable
    public PageInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(@Nullable PageInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Nullable
    public PageDataTransform getTransform() {
        return transform;
    }

    public void setTransform(@Nullable PageDataTransform transform) {
        this.transform = transform;
    }
}
