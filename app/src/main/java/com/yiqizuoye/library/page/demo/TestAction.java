package com.yiqizuoye.library.page.demo;

import android.util.Log;

import com.yiqizuoye.library.page.annotation.ActionRule;
import com.yiqizuoye.library.page.annotation.ActionThread;
import com.yiqizuoye.library.page.annotation.PageQueue;
import com.yiqizuoye.library.page.api.BaseAction;
import com.yiqizuoye.library.page.demo.other.DataTransform;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
@PageQueue(id = 11,priority=101)
@ActionRule(value = "key", type = 1,transforms = DataTransform.class)
public class TestAction implements BaseAction<String> {
    private final static String TAG = "TestAction";

    @ActionThread(ActionThread.Thread.workThread)
    @Override
    public void handleEvent(String data) {
        Log.d(TAG, "handleEvent   -->" + data + "---" + Thread.currentThread().getName());
    }
}
