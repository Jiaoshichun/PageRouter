package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 事件路由跳转规则，由注解处理器创建
 */
public class ActionRule {
    public String key;
    public int[] type;
    public TransformBean[] transformBeans = new TransformBean[0];
    public String actionClass;
    public String dataFormatClass;
    public PageQueue queue = new PageQueue();
}
