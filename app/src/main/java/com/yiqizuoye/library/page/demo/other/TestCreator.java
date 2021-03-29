package com.yiqizuoye.library.page.demo.other;

import androidx.annotation.Nullable;

import com.yiqizuoye.library.page.annotation.PageDataTransform;
import com.yiqizuoye.library.page.api.ActionData;
import com.yiqizuoye.library.page.api.BasePage;
import com.yiqizuoye.library.page.api.BasePresenter;
import com.yiqizuoye.library.page.api.BaseAction;
import com.yiqizuoye.library.page.api.PageCreator;
import com.yiqizuoye.library.page.api.PageInterceptor;
import com.yiqizuoye.library.page.api.PageRuleData;
import com.yiqizuoye.library.page.api.PageData;
import com.yiqizuoye.library.page.api.PageParent;
import com.yiqizuoye.library.page.demo.page.DemoPage;
import com.yiqizuoye.library.page.demo.page.DemoPage2;
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
        PageRuleData pageRuleData = new PageRuleData();
        pageRuleData.key = "key";
        pageRuleData.pageClass = DemoPage.class.getName();
        pageRuleData.type = new int[]{1};
        pageRuleData.dataFormatClass = String.class.getName();
        pageRuleData.presenterClass = TestPresenter.class.getName();
        pageRuleData.interceptors = new String[]{TestInterceptor.class.getName()};
        PageData pageData = new PageData(pageRuleData, new PageParent(android.R.id.content, PageAnnotationConstant.DEFAULT_INDEX));
        list.add(pageData);


        pageRuleData = new PageRuleData();
        pageRuleData.key = "key2";
        pageRuleData.pageClass = DemoPage2.class.getName();
        pageRuleData.type = new int[]{1};
        pageRuleData.dataFormatClass = TestBean.class.getName();
        pageRuleData.presenterClass = TestPresenter.class.getName();
        pageRuleData.interceptors = new String[]{TestInterceptor.class.getName()};
        pageData = new PageData(pageRuleData, new PageParent(android.R.id.content, PageAnnotationConstant.DEFAULT_INDEX));
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
        if (clazzName.equals(DemoPage.class.getName())) {
            return new DemoPage();
        } else if (clazzName.equals(DemoPage2.class.getName())) {
            return new DemoPage2();
        }
        return null;
    }

    @Nullable
    @Override
    public BaseAction createAction(String clazzName) {
        return null;
    }
}
