package com.yiqizuoye.library.page.demo;

import android.app.Activity;
import android.util.Log;

import com.yiqizuoye.library.page.annotation.ActionRule;
import com.yiqizuoye.library.page.annotation.ActionThread;
import com.yiqizuoye.library.page.annotation.PageQueue;
import com.yiqizuoye.library.page.api.PageAction;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
@PageQueue(id = 11)
@ActionRule(value = "key", type = 1)
public class TestAction implements PageAction<String> {
    private final static String TAG = "TestAction";

    @ActionThread(ActionThread.Thread.workThread)
    @Override
    public void handleEvent(String data) {
        Log.d(TAG, "handleEvent   -->" + data + "---" + Thread.currentThread().getName());
    }
}
