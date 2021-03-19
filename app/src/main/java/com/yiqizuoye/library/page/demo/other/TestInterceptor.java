package com.yiqizuoye.library.page.demo.other;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.yiqizuoye.library.page.api.PageInterceptor;
import com.yiqizuoye.library.page.api.PageData;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
public class TestInterceptor implements PageInterceptor {
    @Override
    public boolean intercept(PageData pageData, Object data, @Nullable Bundle otherData) {
        Log.d("chun", "pageData:" + pageData + "  data:" + data);
        return false;
    }
}
