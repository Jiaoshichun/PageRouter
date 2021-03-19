package com.yiqizuoye.library.page.api;

import android.app.Activity;
import android.text.TextUtils;

import com.yiqizuoye.library.page.annotation.PageDataTransform;

import java.util.List;
import java.util.Map;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
public class PageLauncher implements Launcher {
    @Override
    public int start(final Activity context, final RouterData routerData) {
        //根据key 获取以type为key的PageData map集合
        Map<Integer, PageData> dataMap = PageConfig.INSTANCE.getPageRules().get(routerData.getKey());
        if (dataMap == null) {
            return PageCode.ERROR_NO_FOUND_KEY;
        }
        //获取pageData数据
        final PageData pageData = dataMap.get(routerData.getType());
        if (pageData == null) {
            return PageCode.ERROR_NO_FOUND_TYPE;
        }
        //交于队列管理器处理， 如果返回true 表示相同队列的view正在展示，暂时不能启动view
        if (PageQueueManager.processPageData(context,pageData.getQueue(),routerData)) {
            return PageCode.QUEUE_WAITING;
        }

        PageCreator pageCreator = PageConfig.INSTANCE.getPageCreator();
        Object realData;
        if (pageData.getDataFormat().equals(Void.class.getName())) {//不需要参数 使用Void泛型
            realData = null;
        } else {
            //真实的数据格式
            realData = routerData.getData();
            //如果数据格式不正确，并且设置了转换器 ，先进行转换
            if (!TextUtils.equals(routerData.getData().getClass().getName(), pageData.getDataFormat()) && routerData.getTransform() != null) {
                realData = routerData.getTransform().transform(routerData.getData());
            }
            //如果数据格式不正确，注解配置的有转换器 则进行转换
            if (!TextUtils.equals(realData.getClass().getName(), pageData.getDataFormat()) && pageData.getTransformsClass().get(routerData.getData().getClass().getName()) != null) {
                PageDataTransform transform = pageCreator.createTransform(pageData.getTransformsClass().get(routerData.getData().getClass().getName()));
                realData = transform.transform(routerData.getData());
            }
            if (!TextUtils.equals(realData.getClass().getName(), pageData.getDataFormat())) {
                return PageCode.ERROR_DATA_FORMAT;
            }
            //先走全局拦截器
            List<PageInterceptor> globalInterceptor = PageConfig.INSTANCE.getAddGlobalInterceptors();
            if (globalInterceptor != null && globalInterceptor.size() > 0) {
                for (PageInterceptor interceptor : globalInterceptor) {
                    if (interceptor.intercept(pageData, realData, routerData.getOtherData())) {
                        return PageCode.INTERRUPTED;
                    }
                }
            }
        }

        //再走注解的拦截器逻辑
        String[] dataInterceptors = pageData.getInterceptors();
        if (dataInterceptors != null && dataInterceptors.length > 0) {
            for (String interceptor : dataInterceptors) {
                PageInterceptor pageInterceptor = pageCreator.createInterceptor(interceptor);
                if (pageInterceptor != null) {
                    if (pageInterceptor.intercept(pageData, realData, routerData.getOtherData())) {
                        return PageCode.INTERRUPTED;
                    }

                }
            }
        }
        //如果当前界面已经打开，不再重新创建，直接调用onNewOpen方法
        BasePage page = PageManager.INSTANCE.getPage(pageData.getKey());
        if (page != null) {
            page.onNewOpen(realData, routerData.getOtherData());
            return PageCode.SUCCESS;
        }

        //创建Page
        final BasePage basePage = pageCreator.createPage(pageData.getPageClass());
        //创建Presenter
        final BasePresenter presenter = pageCreator.createPresenter(pageData.getPresenterClass());

        final Object finalRealData = realData;
        PageConfig.INSTANCE.getMAIN_HANDLER().post(new Runnable() {
            @Override
            public void run() {
                //保存当前页面
                PageManagerImpl.INSTANCE.addPage(basePage, routerData.getPageResultCallBack());
                //走设置page数据走onCreate方法
                basePage.setPageData(pageData, finalRealData, context, presenter, routerData.getOtherData());
            }
        });

        return PageCode.SUCCESS;
    }
}
