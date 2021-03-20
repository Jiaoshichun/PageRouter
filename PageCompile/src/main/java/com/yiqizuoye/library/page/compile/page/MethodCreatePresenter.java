package com.yiqizuoye.library.page.compile.page;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.yiqizuoye.library.page.annotation.PageRule;
import com.yiqizuoye.library.page.compile.Constants;
import com.yiqizuoye.library.page.compile.Utils;

import java.util.HashSet;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:  createPresenter method
 */
public class MethodCreatePresenter {
    private static boolean isStart = false;
    private static final HashSet<String> hasAdd=new HashSet<>();
    public static MethodSpec.Builder createMethodBuild() {
        MethodSpec.Builder methodCreateHRouterRule = MethodSpec.overriding(Utils.getOverrideMethod(com.yiqizuoye.library.page.compile.Constants.pageCreatorClassName, Constants.METHOD_NAME_CREATE_PRESENTER));
//        methodCreateHRouterRule.addParameter(String.class,"clazzName");

        return methodCreateHRouterRule;
    }

    public static void addCode(String presenterClassName, MethodSpec.Builder method, PageRule rule) {
        if(hasAdd.contains(presenterClassName))return;
        hasAdd.add(presenterClassName);
        if (!isStart) {
            isStart = true;
            method.beginControlFlow("if (arg0.equals($S))", presenterClassName);
        } else {
            method.nextControlFlow(" else if (arg0.equals($S))", presenterClassName);
        }
        method.addStatement("return new $T()", ClassName.bestGuess(presenterClassName));

    }


    public static MethodSpec over(MethodSpec.Builder method) {
        if (isStart) {
            method.endControlFlow();
        }
        hasAdd.clear();
        isStart = false;
        return method.addStatement("return null").build();
    }
}
