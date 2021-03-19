package com.yiqizuoye.library.page.api


/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 父类布局
 */
class PageParent(
    val parentId: Int,
    val index: Int
)

/**
 * 数据转换器类
 */
data class TransformBean(
    val className: String,
    val fromClassName: String,
    val toClassName: String
)

/**
 * 队列数据
 */
class PageQueue {
    constructor()
    constructor(id: Int, priority: Int) {
        isQueue = true
        this.id = id
        this.priority = priority
    }

    /**
     * 是否为队列消息
     */
    var isQueue = false

    /**
     * 队列id
     */
    var id: Int = 1

    /**
     * 队列优先级 默认100
     * 越高优先级越高
     */
    var priority: Int = 100
}

/**
 * 页面数据
 */
class PageData(rule: PageRule, parent: PageParent) {
    //路由key
    val key: String = rule.key

    //当前的类型，通过typeFactory key和type确定一个page


    //数据转换器，将原始数据转换为页面需要的数据
    private val transformBeans = rule.transformBeans

    val types: IntArray = rule.type

    //拦截器
    val interceptors: Array<String> = rule.interceptors

    //页面需要的数据格式  的className
    val dataFormat: String = rule.dataFormatClass

    //presenter的类型 的className
    val presenterClass = rule.presenterClass

    //page页面类型 的className
    val pageClass = rule.pageClass

    //被添加的父View id
    val parentId = parent.parentId

    //被添加到父View 的index
    val index = parent.index

    //队列信息
    val queue = rule.queue

    //格式格式类的class名字，key为from value为to
    val transformsClass = mutableMapOf<String, String>()

    init {
        transformBeans.forEach {

            if (it.toClassName != dataFormat) {
                throw RuntimeException("$it  转换出来的格式不是page需要的格式 needFormat:$dataFormat  $it")
            }
            transformsClass[it.fromClassName] = it.className
        }
    }

    override fun toString(): String {
        return "PageData(key='$key', transformBeans=${transformBeans?.contentToString()}, interceptors=${interceptors.contentToString()}, dataFormat='$dataFormat', presenterClass='$presenterClass', pageClass='$pageClass', parentId=$parentId, index=$index, transformsClass=$transformsClass)"
    }


}