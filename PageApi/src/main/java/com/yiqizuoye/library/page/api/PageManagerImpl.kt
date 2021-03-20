package com.yiqizuoye.library.page.api

import android.app.Activity
import android.os.Bundle
import java.lang.Exception

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 * 页面管理器  同时分发回调消息
 */
internal object PageManagerImpl {
    private val pageStack = mutableListOf<BasePage<*, *>>()
    private val pageResultCallBacks = mutableMapOf<Int, PageResultCallBack?>()

    fun <T : BasePage<*, *>> getPage(key: String): T? {
        val type = PageConfig.typeFactory.getType()
        val find = pageStack.find { page -> page.mPageData.key == key } ?: return null
        if (find.mPageData.types.find { it == type } != null) {
            return try {
                find as T
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    /**
     * 清除全部page
     */
    @Synchronized
    fun clearPage() {
        for (i in pageStack.size - 1 downTo 0) {
            pageStack[i].finish()
        }
    }

    @Synchronized
    fun addPage(page: BasePage<*, *>, resultCallBack: PageResultCallBack?) {
        pageStack.add(page)
        pageResultCallBacks[page.hashCode()] = resultCallBack
    }

    @Synchronized
    fun removePage(page: BasePage<*, *>) {
        page.finish()
        pageStack.remove(page)
        pageResultCallBacks.remove(page.hashCode())
    }

    /**
     * 结果回调
     */
    fun setResult(page: BasePage<*, *>, result: Int, data: Bundle?) {
        pageResultCallBacks[page.hashCode()]?.onResult(result, data)
    }

    /**
     * 是否拦截返回键
     */
    fun onBackPressed(): Boolean {
        for (i in pageStack.size - 1 downTo 0) {
            if (pageStack[i].onBackPressed()) {
                return true
            }
        }
        return false
    }

    /**
     * type类型改变时  调用该方法
     * 清除全部page，如果需要保存页面，关闭后重新开启
     */
    fun onTypeChange() {
        val needOpenPage = mutableListOf<SavePageData>()
        var activity: Activity? = null;
        pageStack.forEach {
            val bundle = Bundle()
            if (it.onTypeChanged(bundle)) {
                if (activity == null) {
                    activity = it.mContext
                }
                needOpenPage.add(SavePageData(it.mPageData, it.mData, bundle))
            }
        }
        clearPage()
        needOpenPage.forEach {
            PageRouter.create(activity, it.pageData.key, it.data).setOtherData(it.otherData).open()
        }
    }

    /**
     * 权限回调，由Activity调用
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        pageStack.forEach { it.onRequestPermissionsResult(requestCode, permissions, grantResults) }
    }

    private data class SavePageData(val pageData: PageData, val data: Any?, val otherData: Bundle?)
}