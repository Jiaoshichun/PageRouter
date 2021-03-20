package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 页面跳转规则，由注解处理器创建
 */
public class PageRuleData {
    //唯一key
    public String key;
    //类型 key+type保持唯一
    public int[] type;
    //数据格式转换类的类名
    public TransformBean[] transformBeans = new TransformBean[0];
    //page绑定的presenter类名
    public String presenterClass;
    //page的类名
    public String pageClass;
    //拦截器的类名
    public String[] interceptors = new String[0];
    //数据的类名
    public String dataFormatClass;
    //页面队列
    public PageQueueData queue = new PageQueueData();
}
