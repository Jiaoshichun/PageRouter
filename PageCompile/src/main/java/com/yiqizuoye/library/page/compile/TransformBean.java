package com.yiqizuoye.library.page.compile;

import javax.lang.model.type.TypeMirror;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 * 数据转换器类
 */
public class TransformBean {
    public TypeMirror className;
    public String fromClassName;
    public String toClassName;

    public TransformBean(TypeMirror className, String fromClassName, String toClassName) {
        this.className = className;
        this.fromClassName = fromClassName;
        this.toClassName = toClassName;
    }
}
