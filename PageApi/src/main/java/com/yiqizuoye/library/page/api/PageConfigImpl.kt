package com.yiqizuoye.library.page.api

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 全局配置类
 */
internal object PageConfigImpl {

    //所有的页面数据
    private val allPageData = mutableListOf<PageData>()
    val pageRules = mutableMapOf<String, MutableMap<Int, PageData>>()
    private val allActionData = mutableListOf<ActionData>()
    val actionRules = mutableMapOf<String, MutableMap<Int, ActionData>>()

    //页面创建器
    lateinit var pageCreator: PageCreator

    private var activity: AppCompatActivity? = null

    fun init(activity: AppCompatActivity, creator: PageCreator) {
        PageManager.clearPage()
        EventQueueManager.clear()

        this.activity?.lifecycle?.removeObserver(lifecycleObserver)
        this.pageCreator = creator
        this.activity = activity
        //获取页面路由map
        val create = creator.createPageData()
        allPageData.clear()
        allPageData.addAll(create)
        allPageData.forEach {
            it.types.forEach { type ->
                pageRules.getOrPut(it.key, { mutableMapOf() })[type] = it
            }

        }
        //获取事件路由map
        val actionData = creator.createActionData()
        allActionData.clear()
        allActionData.addAll(actionData)
        allActionData.forEach {
            it.types.forEach { type ->
                actionRules.getOrPut(it.key, { mutableMapOf() })[type] = it
            }

        }
        activity.lifecycle.addObserver(lifecycleObserver)
    }

    //页面销毁时，清除缓存
    private val lifecycleObserver: LifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            PageManager.clearPage()
            EventQueueManager.clear()
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
    fun removeGlobalInterceptor(pageInterceptor: PageInterceptor) {
        addGlobalInterceptors.remove(pageInterceptor)
    }

//    /**
//     * 全局回调函数
//     */
//    var globalCallBack: GlobalCallBack = object : GlobalCallBack {
//        override fun onError(
//            code: Int,
//            msg: String,
//            pageData: PageData,
//            data: Any,
//            needDataClass: Class<Any>
//        ) {
//        }
//        override fun onSuccess(pageData: PageData, data: Any, needDataClass: Class<Any>) {
//        }
//
//    }

//    interface GlobalCallBack {
//        fun onError(
//            code: Int,
//            msg: String,
//            pageData: PageData,
//            data: Any,
//            needDataClass: Class<Any>
//        )
//
//        fun onSuccess(pageData: PageData, data: Any, needDataClass: Class<Any>)
//    }

    /**
     * 类型工厂类  用于获取当前的类型
     */
    var typeFactory: TypeFactory = object : TypeFactory {
        override fun getType() = 1

    }

}

interface TypeFactory {
    fun getType(): Int
}