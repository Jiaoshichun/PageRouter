package com.yiqizuoye.library.page.demo.presenter

import android.util.Log
import com.yiqizuoye.library.page.api.BasePresenter
import com.yiqizuoye.library.page.demo.other.TestBean
import com.yiqizuoye.library.page.demo.view.DemoView

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
class TestPresenter : BasePresenter<TestBean, DemoView>() {
    fun toLogin() {
        Log.d("TestPresenter", "toLogin")
        mView.loginSuccess()
    }
}