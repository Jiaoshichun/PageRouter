package com.yiqizuoye.library.page.api;

import androidx.annotation.Nullable;

import com.yiqizuoye.library.page.annotation.PageDataTransform;

import java.util.List;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 页面创建器，通过注解处理器自动生成实现类
 */
public interface PageCreator {
    /**
     * 扫描{@link com.yiqizuoye.library.page.annotation.PageRule}
     * @return
     */
    List<PageData> createPageData();

    List<ActionData> createActionData();

    @Nullable
    PageDataTransform createTransform(String clazzName);

    @Nullable
    BasePresenter createPresenter(String clazzName);

    @Nullable
    PageInterceptor createInterceptor(String clazzName);

    @Nullable
    BasePage createPage(String clazzName);

    @Nullable
    BaseAction createAction(String clazzName);
}
