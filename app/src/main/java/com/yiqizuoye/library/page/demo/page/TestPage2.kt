package com.yiqizuoye.library.page.demo.page

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.yiqizuoye.library.page.R
import com.yiqizuoye.library.page.annotation.PageParent
import com.yiqizuoye.library.page.annotation.PagePipe
import com.yiqizuoye.library.page.annotation.PageRule
import com.yiqizuoye.library.page.api.BasePage
import com.yiqizuoye.library.page.api.PageManager
import com.yiqizuoye.library.page.api.PagePipeManager
import com.yiqizuoye.library.page.demo.other.DataTransform
import com.yiqizuoye.library.page.demo.other.TestBean
import com.yiqizuoye.library.page.demo.other.TestInterceptor
import com.yiqizuoye.library.page.demo.view.TestView2
import com.yiqizuoye.library.page.demo.presenter.TestPresenter2

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
@PageParent(android.R.id.content)
@PageRule(
    "key2",
    type = [1],
    interceptors = [TestInterceptor::class],
    transforms = [DataTransform::class]
)
class TestPage2 : BasePage<TestBean, TestPresenter2>(),
    TestView2 {
    private val TAG = "TestPage2"

    override fun onCreate(data: TestBean?, otherData: Bundle?) {
        Log.d(TAG, "onCreate  data:$data  otherData:$otherData")
        setContentView(R.layout.page_test2)
        //获取其他Page的通道接口
        val testNamePipe = PagePipeManager.getTestNamePipe()
        if (testNamePipe != null) {
            Log.d(TAG, "testNamePipe:${testNamePipe.name} ")

        }
        val txt = findViewById<TextView>(R.id.txt)
        txt.setOnClickListener {
            PageManager.onTypeChange()
        }
//        Log.d(TAG, "获取到TestPage:${page.mPageData}")
    }

    override fun onNewOpen(data: TestBean?, otherData: Bundle?) {
        super.onNewOpen(data, otherData)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")

    }
    @PagePipe("getA")
    fun getA(){

    }
}