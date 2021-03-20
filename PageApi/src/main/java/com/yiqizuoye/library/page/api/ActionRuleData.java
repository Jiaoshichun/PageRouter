package com.yiqizuoye.library.page.api;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 * 事件路由跳转规则，由注解处理器创建
 */
public class ActionRuleData {
    //唯一key
    public String key;
    //类型 key+type保持唯一
    public int[] type;
    //数据格式转换类的类名
    public TransformBean[] transformBeans = new TransformBean[0];
    //action的类名
    public String actionClass;
    //数据的类名
    public String dataFormatClass;
    //事件队列
    public PageQueueData queue = new PageQueueData();

}
