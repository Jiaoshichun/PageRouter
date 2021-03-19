package com.yiqizuoye.library.page.api;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 页面拦截器
 */
public interface PageInterceptor {
    /**
     * 拦截器
     * @param pageData
     * @param data
     * @return
     */
    boolean intercept(PageData pageData, Object data, @Nullable Bundle otherData);
}
