package com.yiqizuoye.library.page.demo.view

import com.yiqizuoye.library.page.api.IView
import com.yiqizuoye.library.page.demo.other.TestBean

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
interface TestView : IView<TestBean> {
    fun loginSuccess()
}