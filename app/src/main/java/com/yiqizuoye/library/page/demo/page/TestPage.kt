package com.yiqizuoye.library.page.demo.page

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.yiqizuoye.library.page.R
import com.yiqizuoye.library.page.annotation.*
import com.yiqizuoye.library.page.api.BasePage
import com.yiqizuoye.library.page.api.IPagePipe
import com.yiqizuoye.library.page.api.PageRouter
import com.yiqizuoye.library.page.demo.TestPipe
import com.yiqizuoye.library.page.demo.other.DataTransform
import com.yiqizuoye.library.page.demo.other.DataTransform2
import com.yiqizuoye.library.page.demo.other.TestBean
import com.yiqizuoye.library.page.demo.other.TestInterceptor
import com.yiqizuoye.library.page.demo.presenter.TestPresenter
import com.yiqizuoye.library.page.demo.view.TestView

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
@PageParent(android.R.id.content)
@PageRule(
    "key",
    type = [1],
    interceptors = [TestInterceptor::class],
    transforms = [DataTransform::class, DataTransform2::class]
)
class TestPage : BasePage<TestBean, TestPresenter>(), TestView {
    private val TAG = "TestPage"

    override fun onCreate(data: TestBean?, otherData: Bundle?) {
        Log.d(TAG, "onCreate  data:$data  otherData:$otherData")
        setContentView(R.layout.page_test)
        val txt = findViewById<TextView>(R.id.txt)
        txt.setOnClickListener {
            val open = PageRouter.create(mContext,"key2", "TEST2新来的").setResultCallBack { result, data ->
                Log.d(TAG, "setResultCallBack  result:$result  data:$data")
            }.open()
            Log.d(TAG, "open Result:$open")
//            关闭当前界面
//            finish()
            mPresenter.toLogin()
            val open1 = PageRouter.createAction("key", "事件咯").open()
            Log.d(TAG, "openAction Result:$open1")

        }

    }

    override fun onTypeChanged(otherData: Bundle): Boolean {
        otherData.putString("typeChanged", "咯");
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionsResult")

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onBackPressed(): Boolean {
        Log.d(TAG, "onBackPressed")
        return super.onBackPressed()
    }

    override fun onNewOpen(data: TestBean?, otherData: Bundle?) {
        super.onNewOpen(data, otherData)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")

    }


    override fun loginSuccess() {
        Log.d(TAG, "loginSuccess")
    }

    @PagePipe("getTestNamePipe")
    override fun getPipe(): TestPipe {
        return object : TestPipe {
            override fun getName(): Int {
                return 1111111
            }

        }
    }

}