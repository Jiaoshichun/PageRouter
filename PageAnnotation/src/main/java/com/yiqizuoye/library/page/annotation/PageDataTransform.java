package com.yiqizuoye.library.page.annotation;

/**
 * Author: jiao
 * Date: 2021/3/15
 * Description:
 */
public interface PageDataTransform<FROM, TO> {
    TO transform(FROM from);
}
