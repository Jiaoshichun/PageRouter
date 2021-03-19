package com.yiqizuoye.library.page.api

import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.yiqizuoye.library.page.annotation.PageDataTransform

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 全局配置类
 */
object PageConfig {
    val MAIN_HANDLER = Handler(Looper.getMainLooper());

    //所有的页面数据
    private val allPageData = mutableListOf<PageData>()
    val pageRules = mutableMapOf<String, MutableMap<Int, PageData>>()

    //页面创建器
    var pageCreator: PageCreator = object : PageCreator {
        override fun createInterceptor(clazzName: String?): PageInterceptor? {
            return null
        }

        override fun createPage(clazzName: String?): BasePage<*, out BasePresenter<*, *>>? {
            return null
        }

        override fun createPageData(): MutableList<PageData>? {
            return null
        }

        override fun createTransform(clazzName: String?): PageDataTransform<*, *>? {
            return null
        }

        override fun createPresenter(clazzName: String?): BasePresenter<*, out IView<*>>? {
            return null
        }


    }
    private var activity: AppCompatActivity? = null

    fun init(activity: AppCompatActivity, creator: PageCreator) {
        PageManager.clearPage()
        this.activity?.lifecycle?.removeObserver(lifecycleObserver)
        this.pageCreator = creator
        this.activity = activity
        val create = creator.createPageData()
        allPageData.clear()
        allPageData.addAll(create)
        allPageData.forEach {
            it.types.forEach { type ->
                pageRules.getOrPut(it.key, { mutableMapOf() })[type] = it
            }

        }
        activity.lifecycle.addObserver(lifecycleObserver)
    }

    private val lifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            PageManager.clearPage()
            activity?.lifecycle?.removeObserver(this)

        }
    }


    val addGlobalInterceptors = mutableListOf<PageInterceptor>()

    /**
     * 添加全局拦截器
     */
    fun addGlobalInterceptor(pageInterceptor: PageInterceptor) {
        addGlobalInterceptors.add(pageInterceptor)
    }

    /**
     * 全局回调函数
     */
    var globalCallBack: GlobalCallBack = object : GlobalCallBack {
        override fun onError(
            code: Int,
            msg: String,
            pageData: PageData,
            data: Any,
            needDataClass: Class<Any>
        ) {

        }

        override fun onSuccess(pageData: PageData, data: Any, needDataClass: Class<Any>) {
        }

    }

    interface GlobalCallBack {
        fun onError(
            code: Int,
            msg: String,
            pageData: PageData,
            data: Any,
            needDataClass: Class<Any>
        )

        fun onSuccess(pageData: PageData, data: Any, needDataClass: Class<Any>)
    }

    /**
     * 类型工厂类  用于获取当前的类型
     */
    var typeFactory: TypeFactory = object : TypeFactory {
        override fun getType() = 1

    }

    interface TypeFactory {
        fun getType(): Int
    }


}