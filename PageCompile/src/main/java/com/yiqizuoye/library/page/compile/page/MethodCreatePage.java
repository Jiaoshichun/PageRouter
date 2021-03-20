package com.yiqizuoye.library.page.compile.page;

import com.squareup.javapoet.MethodSpec;
import com.yiqizuoye.library.page.annotation.PageRule;
import com.yiqizuoye.library.page.compile.Constants;
import com.yiqizuoye.library.page.compile.Utils;

import javax.lang.model.element.TypeElement;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:  createPage method
 */
public class MethodCreatePage {
    private static boolean isStart = false;

    public static MethodSpec.Builder createMethodBuild() {
        MethodSpec.Builder method = MethodSpec.overriding(Utils.getOverrideMethod(Constants.pageCreatorClassName, Constants.METHOD_NAME_CREATE_PAGE));
//        methodCreateHRouterRule.addParameter(String.class, "clazzName");

        return method;
    }

    public static void addCode(TypeElement type, MethodSpec.Builder method, PageRule rule) {
        if (!isStart) {
            isStart = true;
            method.beginControlFlow("if (arg0.equals($S))", type);
        } else {
            method.nextControlFlow(" else if (arg0.equals($S))", type);
        }
        method.addStatement("return new $T()", type);

    }

    public static MethodSpec over(MethodSpec.Builder method) {
        if(isStart){
            method.endControlFlow();
        }
        isStart = false;
        return method.addStatement("return null").build();
    }
}

