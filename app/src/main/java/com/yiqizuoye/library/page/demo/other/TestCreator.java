package com.yiqizuoye.library.page.demo.other;

import androidx.annotation.Nullable;

import com.yiqizuoye.library.page.annotation.PageAnnotationConstant;
import com.yiqizuoye.library.page.annotation.PageDataTransform;
import com.yiqizuoye.library.page.api.ActionData;
import com.yiqizuoye.library.page.api.BasePage;
import com.yiqizuoye.library.page.api.BasePresenter;
import com.yiqizuoye.library.page.api.PageAction;
import com.yiqizuoye.library.page.api.PageCreator;
import com.yiqizuoye.library.page.api.PageInterceptor;
import com.yiqizuoye.library.page.api.PageRule;
import com.yiqizuoye.library.page.api.PageData;
import com.yiqizuoye.library.page.api.PageParent;
import com.yiqizuoye.library.page.demo.page.TestPage;
import com.yiqizuoye.library.page.demo.page.TestPage2;
import com.yiqizuoye.library.page.demo.presenter.TestPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
public class TestCreator implements PageCreator {
    @Override
    public List<PageData> createPageData() {
        ArrayList<PageData> list = new ArrayList<>();
        PageRule pageRule = new PageRule();
        pageRule.key = "key";
        pageRule.pageClass = TestPage.class.getName();
        pageRule.type = new int[]{1};
        pageRule.dataFormatClass = String.class.getName();
        pageRule.presenterClass = TestPresenter.class.getName();
        pageRule.interceptors = new String[]{TestInterceptor.class.getName()};
        PageData pageData = new PageData(pageRule, new PageParent(android.R.id.content, PageAnnotationConstant.DEFAULT_INDEX));
        list.add(pageData);


        pageRule = new PageRule();
        pageRule.key = "key2";
        pageRule.pageClass = TestPage2.class.getName();
        pageRule.type = new int[]{1};
        pageRule.dataFormatClass = TestBean.class.getName();
        pageRule.presenterClass = TestPresenter.class.getName();
        pageRule.interceptors = new String[]{TestInterceptor.class.getName()};
        pageData = new PageData(pageRule, new PageParent(android.R.id.content, PageAnnotationConstant.DEFAULT_INDEX));
        list.add(pageData);
        return list;
    }

    @Override
    public List<ActionData> createActionData() {
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public PageDataTransform createTransform(String clazzName) {
        if (clazzName.equals(DataTransform.class.getName())) {
            return new DataTransform();
        }
        return null;
    }

    @Nullable
    @Override
    public BasePresenter createPresenter(String clazzName) {
        if (clazzName.equals(TestPresenter.class.getName())) {
            return new TestPresenter();
        }
        return null;
    }

    @Nullable
    @Override
    public PageInterceptor createInterceptor(String clazzName) {
        if (clazzName.equals(TestInterceptor.class.getName())) {
            return new TestInterceptor();
        }
        return null;
    }

    @Nullable
    @Override
    public BasePage createPage(String clazzName) {
        if (clazzName.equals(TestPage.class.getName())) {
            return new TestPage();
        } else if (clazzName.equals(TestPage2.class.getName())) {
            return new TestPage2();
        }
        return null;
    }

    @Nullable
    @Override
    public PageAction createAction(String clazzName) {
        return null;
    }
}
