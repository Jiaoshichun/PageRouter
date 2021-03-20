package com.yiqizuoye.library.page.api;

import android.text.TextUtils;

import com.yiqizuoye.library.page.annotation.ActionThread;
import com.yiqizuoye.library.page.annotation.PageDataTransform;

import java.util.Map;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
public class ActionLauncher implements Launcher {
    @Override
    public int start( final RouterData routerData) {
        //根据key 获取以type为key的PageData map集合
        Map<Integer, ActionData> dataMap = PageConfig.INSTANCE.getActionRules().get(routerData.getKey());
        if (dataMap == null) {
            return PageCode.ERROR_NO_FOUND_KEY;
        }
        //获取pageData数据
        final ActionData actionData = dataMap.get(routerData.getType());
        if (actionData == null) {
            return PageCode.ERROR_NO_FOUND_TYPE;
        }
        //交于队列管理器处理， 如果返回true 表示相同队列的事件正在处理，暂时不能处理当前事件
        if (EventQueueManager.processPageData(actionData.getQueue(), routerData)) {
            return PageCode.QUEUE_WAITING;
        }

        PageCreator pageCreator = PageConfig.INSTANCE.getPageCreator();
        Object realData;
        if (actionData.getDataFormat().equals(Void.class.getName())) {//不需要参数 使用Void泛型
            realData = null;
        } else {
            //真实的数据格式
            realData = routerData.getData();

            String routerDataFormat = routerData.getData().getClass().getName();

            //如果数据格式不正确，并且设置了转换器 ，先进行转换
            if (!TextUtils.equals(routerDataFormat, actionData.getDataFormat())) {
                if (routerData.getTransform() != null) {
                    realData = routerData.getTransform().transform(routerData.getData());
                }
                //如果数据格式不正确，注解配置的有转换器 则进行转换
                if (!TextUtils.equals(realData.getClass().getName(), actionData.getDataFormat()) && actionData.getTransformsClass().get(routerDataFormat) != null) {
                    PageDataTransform transform = pageCreator.createTransform(actionData.getTransformsClass().get(routerDataFormat));
                    realData = transform.transform(routerData.getData());
                }
                if (!TextUtils.equals(realData.getClass().getName(), actionData.getDataFormat())) {
                    return PageCode.ERROR_DATA_FORMAT;
                }

            }

        }


        //创建Page
        final BaseAction baseAction = pageCreator.createAction(actionData.getActionClass());
        final Object finalRealData = realData;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                baseAction.handleEvent(finalRealData);
                EventQueueManager.pageDataFinish(actionData);
            }
        };
        if (actionData.getThread() == ActionThread.Thread.mainThread) {
            PageThreadManager.runMain(runnable);
        } else if (actionData.getThread() == ActionThread.Thread.workThread) {
            PageThreadManager.runWork(runnable);
        } else {
            runnable.run();
        }

        return PageCode.SUCCESS;
    }
}
