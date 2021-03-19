package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 页面跳转规则，由注解处理器创建
 */
public class PageRule {
    public String key;
    public int[] type;
    public TransformBean[] transformBeans = new TransformBean[0];
    public String presenterClass;
    public String pageClass;
    public String[] interceptors = new String[0];
    public String dataFormatClass;
    public PageQueue queue = new PageQueue();
}
