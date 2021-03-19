package com.yiqizuoye.library.pagec.comiler;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.yiqizuoye.library.page.annotation.PageParent;
import com.yiqizuoye.library.page.annotation.PageQueue;
import com.yiqizuoye.library.page.annotation.PageRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:  createPage method
 */
public class MethodCreatePageData {

    public static MethodSpec.Builder createMethodBuild() {
        MethodSpec.Builder methodCreateHRouterRule = MethodSpec.overriding(Utils.getOverrideMethod(Constants.pageCreatorClassName, Constants.METHOD_NAME_CREATE_PAGE_DATA));
        methodCreateHRouterRule.addStatement("$T<$T> list = new $T<>()", List.class, Constants.pageDataClassName, ArrayList.class);
        methodCreateHRouterRule.addStatement("$T pageRule", Constants.ruleClassName);
        methodCreateHRouterRule.addStatement("$T transformBeans", Constants.transformBeanClassName);

        return methodCreateHRouterRule;
    }

    private static Map<String, Set<Integer>> allRouter = new HashMap<>();

    public static void addCode(TypeElement type, MethodSpec.Builder method, PageRule rule, PageParent pageParent, String dataClassName, String presenterClassName) {
        Set<Integer> integerList = allRouter.get(rule.value());
        if (integerList == null) {
            integerList = new HashSet<>();
            allRouter.put(rule.value(), integerList);

        }
        for (int i : rule.type()) {
            if (!integerList.add(i)) {//包含重复的值
                method.addStatement("出错啦");
                method.addStatement("包含key和type重复的Page,Page的Value: " + rule.value() + "  重复的type:" + i+"  类名:"+type);
            }

        }

        method.addStatement("pageRule = new $T()", Constants.ruleClassName);

//        pageRule.key = "key2";
        method.addStatement("pageRule.key = $S", rule.value());

        //        pageRule.pageClass = TestPage2.class.getName();
        method.addStatement("pageRule.pageClass = $S", type);

        //        pageRule.dataFormatClass = TestBean.class.getName();
        method.addStatement("pageRule.dataFormatClass = $S", dataClassName);

        //        pageRule.presenterClass = TestPresenter.class.getName();
        method.addStatement("pageRule.presenterClass = $S", presenterClassName);
//        TypeMirror typeMirror = (TypeMirror) qualifiedName;
        addInterceptorsCode(method, rule);

        addType(method, rule.type());

        addDataTransform(method, rule);

        PageQueue pageQueue = type.getAnnotation(PageQueue.class);
        if (pageQueue != null) {
            method.addStatement("pageRule.queue = new $T(" + pageQueue.id() + "," + pageQueue.priority() + ")", Constants.pageQueueClassName);

        }

//        pageData = new PageData(pageRule, new PageParent(android.R.id.content, PageAnnotationConstant.DEFAULT_INDEX));
        method.addStatement("list.add( new $T(pageRule, new $T(" + pageParent.value() + ", " + pageParent.index() + ")))", Constants.pageDataClassName, Constants.parentClassName);


    }

    public static MethodSpec over(MethodSpec.Builder method) {
        allRouter.clear();
        return method.addStatement("return list").build();
    }


    private static void addInterceptorsCode(MethodSpec.Builder method, PageRule rule) {
        //        pageRule.interceptors = new String[]{TestInterceptor.class.getName()};
        if (rule == null) return;
        TypeMirror[] interceptors = null;
        try {
            rule.interceptors();// this should throw
        } catch (MirroredTypesException mte) {
            interceptors = mte.getTypeMirrors().toArray(new TypeMirror[0]);
        }
        if (interceptors == null) return;
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("pageRule.interceptors = new String[]{");
        for (int i = 0; i < interceptors.length; i++) {
            if (i > 0) {
                builder.add(",");
            }
            builder.add("$S", interceptors[i]);
        }
        builder.add("};\r\n");
        method.addCode(builder.build());
    }

    private static void addDataTransform(MethodSpec.Builder method, PageRule rule) {
//        pageRule.transforms = new Class[]{DataTransform.class};
        if (rule == null) return;
        TypeMirror[] transform = null;
        try {
            rule.transforms();// this should throw
        } catch (MirroredTypesException mte) {
            transform = mte.getTypeMirrors().toArray(new TypeMirror[0]);
        }
        if (transform == null || transform.length == 0) return;

        TransformBean[] transformBeans = new TransformBean[transform.length];
        for (int i = 0; i < transform.length; i++) {

            String superClassName = (((TypeElement) ((DeclaredType) transform[i]).asElement()).getInterfaces()).get(0).toString();

            String allName = superClassName.substring(superClassName.indexOf("<") + 1, superClassName.length() - 1);

            String[] split = allName.split(",");
            String fromStr = split[0];
            String toStr = split[1];
            transformBeans[i] = new TransformBean(transform[i], fromStr, toStr);
        }

        CodeBlock.Builder builder = CodeBlock.builder();
        builder.add("pageRule.transformBeans = new $T[]{", Constants.transformBeanClassName);
        for (int i = 0; i < transformBeans.length; i++) {
            TransformBean transformBean = transformBeans[i];
            if (i > 0) {
                builder.add(",");
            }
            builder.add("new $T($S,$S,$S)", Constants.transformBeanClassName, transformBean.className, transformBean.fromClassName, transformBean.toClassName);
        }
        builder.add("};\r\n");
        method.addCode(builder.build());
    }

    private static void addType(MethodSpec.Builder method, int[] types) {
        //        pageRule.type = new int[]{1};
        StringBuilder builder = new StringBuilder();
        builder.append("pageRule.type = new int[]{");
        for (int i = 0; i < types.length; i++) {
            builder.append(types[i]);
            if (i != types.length - 1) {
                builder.append(",");
            }
        }
        builder.append("}");
        method.addStatement(builder.toString());
    }
}
