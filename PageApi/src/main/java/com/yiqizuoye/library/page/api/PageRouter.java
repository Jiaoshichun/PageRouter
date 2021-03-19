package com.yiqizuoye.library.page.api;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.yiqizuoye.library.page.annotation.PageDataTransform;

import java.util.List;
import java.util.Map;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
public class PageRouter {
    private RouterData routerData;
    private Launcher launcher;

    private PageRouter(RouterData routerData, Launcher launcher) {
        this.routerData = routerData;
        this.launcher = launcher;
    }

    public static PageRouter create(String key, Object data) {
        PageLauncher launcher = new PageLauncher();
        RouterData routerData = new RouterData(PageConfig.INSTANCE.getTypeFactory().getType(), key, data);
        return new PageRouter(routerData, launcher);
    }


    /**
     * 设置拦截器
     */
    public PageRouter setInterceptor(PageInterceptor interceptor) {
        routerData.setInterceptor(interceptor);
        return this;
    }

    /**
     * 设置附加数据
     */
    public PageRouter setOtherData(Bundle otherData) {
        routerData.setOtherData(otherData);
        return this;
    }

    /**
     * 设置页面结果回调
     * 在启动后的页面调用setResult时会回调该方法
     */
    public PageRouter setResultCallBack(PageResultCallBack callBack) {
        routerData.setPageResultCallBack(callBack);
        return this;
    }

    /**
     * 设置格式转换类
     */
    public PageRouter setTransform(PageDataTransform transform) {
        routerData.setTransform(transform);
        return this;
    }

    /**
     * 开启界面操作
     *
     * @param context
     * @return
     */
    public int open(final Activity context) {
        return LauncherManager.start(launcher, context, routerData);
    }

}

