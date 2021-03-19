package com.yiqizuoye.library.page.demo.other;

import com.yiqizuoye.library.page.annotation.PageDataTransform;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 */
public class DataTransform implements PageDataTransform<String, TestBean> {
    @Override
    public TestBean transform(String s) {
        TestBean testBean = new TestBean();
        testBean.name = s;
        return testBean;
    }
}
