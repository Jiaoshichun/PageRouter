package com.yiqizuoye.library.page.api;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: jiao
 * Date: 2021/3/22
 * Description:
 */
public class PageConfig {
    public static void init(AppCompatActivity activity, PageCreator pageCreator) {
        PageConfigImpl.INSTANCE.init(activity, pageCreator);
    }

    public static void setTypeFactory(TypeFactory typeFactory) {
        PageConfigImpl.INSTANCE.setTypeFactory(typeFactory);
    }

    public static void addGlobalInterceptor(PageInterceptor pageInterceptor) {
        PageConfigImpl.INSTANCE.addGlobalInterceptor(pageInterceptor);
    }

    public static void removeGlobalInterceptor(PageInterceptor pageInterceptor) {
        PageConfigImpl.INSTANCE.removeGlobalInterceptor(pageInterceptor);
    }

}
