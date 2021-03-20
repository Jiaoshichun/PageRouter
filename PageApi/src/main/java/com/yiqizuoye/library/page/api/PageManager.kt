package com.yiqizuoye.library.page.api

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 * 页面管理器  同时分发回调消息
 */
object PageManager {
    /**
     * 根据key获取当前正在打开的Page，可能为空
     */
    @JvmStatic
    fun <T : BasePage<*, *>> getPage(key: String): T? {
        return PageManagerImpl.getPage(key)
    }


    /**
     * 是否拦截返回键
     */
    @JvmStatic
    fun onBackPressed(): Boolean {
        return PageManagerImpl.onBackPressed()
    }

    /**
     * type类型改变时  调用该方法
     * 清除全部page，如果需要保存页面，关闭后重新开启
     */
    @JvmStatic
    fun onTypeChange() {
        PageManagerImpl.onTypeChange()
    }

    /**
     * 清除page。
     * 同时清除，page队列
     */
    @JvmStatic
    fun clearPage() {
        PageManagerImpl.clearPage()
        PageQueueManager.clear()

    }

    /**
     * 权限回调，由Activity调用
     */
    @JvmStatic
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        PageManagerImpl.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}