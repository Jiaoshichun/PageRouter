package com.yiqizuoye.library.page.demo.other;

import com.yiqizuoye.library.page.annotation.PageDataTransform;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
public class DataTransform2 implements PageDataTransform<Integer, TestBean> {
    @Override
    public TestBean transform(Integer s) {
        TestBean testBean = new TestBean();
        testBean.name = String.valueOf(s);
        return testBean;
    }
}
