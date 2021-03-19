package com.yiqizuoye.library.pagec.comiler;

import com.squareup.javapoet.MethodSpec;
import com.yiqizuoye.library.page.annotation.PageRule;

import java.util.HashSet;

import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:  createInterceptor method
 */
public class MethodCreateInterceptor {
    private static boolean isStart = false;
    private static final HashSet<String> hasAdd = new HashSet<>();

    public static MethodSpec.Builder createMethodBuild() {
        MethodSpec.Builder methodCreateHRouterRule = MethodSpec.overriding(Utils.getOverrideMethod(Constants.pageCreatorClassName, Constants.METHOD_NAME_CREATE_INTERCEPTOR));
//        methodCreateHRouterRule.addParameter(String.class,"clazzName");

        return methodCreateHRouterRule;
    }

    public static void addCode(MethodSpec.Builder method, PageRule rule) {
        TypeMirror[] typeMirrors = getInterceptors(rule);
        if (typeMirrors != null && typeMirrors.length > 0) {
            for (TypeMirror type : typeMirrors) {
                if (hasAdd.contains(type.toString())) continue;
                hasAdd.add(type.toString());
                if (!isStart) {
                    isStart = true;
                    method.beginControlFlow("if (arg0.equals($S))", type);
                } else {
                    method.nextControlFlow(" else if (arg0.equals($S))", type);
                }
                method.addStatement("return new $T()", type);
            }
        }


    }

    private static TypeMirror[] getInterceptors(PageRule rule) {
        if (rule == null) return null;
        try {
            rule.interceptors();// this should throw
        } catch (MirroredTypesException mte) {
            return mte.getTypeMirrors().toArray(new TypeMirror[0]);
        }
        return null;

    }

    public static MethodSpec over(MethodSpec.Builder method) {
        hasAdd.clear();
        if (isStart) {
            method.endControlFlow();
        }
        isStart = false;
        return method.addStatement("return null").build();
    }
}
