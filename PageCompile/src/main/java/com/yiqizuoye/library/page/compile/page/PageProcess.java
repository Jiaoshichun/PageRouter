package com.yiqizuoye.library.page.compile.page;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiqizuoye.library.page.annotation.PageParent;
import com.yiqizuoye.library.page.annotation.PageRule;
import com.yiqizuoye.library.page.annotation.PageRules;
import com.yiqizuoye.library.page.compile.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Author: jiao
 * Date: 2021/3/20
 * Description:
 */
public class PageProcess {
    public static boolean process(RoundEnvironment roundEnv, TypeSpec.Builder creatorImplType, TypeSpec.Builder pagePipeType) {

        MethodSpec.Builder createPage = MethodCreatePage.createMethodBuild();
        MethodSpec.Builder createPageData = MethodCreatePageData.createMethodBuild();
        MethodSpec.Builder createTransform = MethodCreateTransform.createMethodBuild();
        MethodSpec.Builder createInterceptor = MethodCreateInterceptor.createMethodBuild();
        MethodSpec.Builder createPresenter = MethodCreatePresenter.createMethodBuild();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(PageRules.class);
        Set<Element> elementsAll = new HashSet<>(elements);
        elements = roundEnv.getElementsAnnotatedWith(PageRule.class);
        elementsAll.addAll(elements);
        if (elementsAll.isEmpty()) {
            MethodSpec page = MethodCreatePage.over(createPage);
            MethodSpec pageData = MethodCreatePageData.over(createPageData);
            MethodSpec presenter = MethodCreatePresenter.over(createPresenter);
            MethodSpec transform = MethodCreateTransform.over(createTransform);
            MethodSpec interceptor = MethodCreateInterceptor.over(createInterceptor);

            JavaPipeCreator.over(pagePipeType);

            creatorImplType.addMethod(page)
                    .addMethod(pageData)
                    .addMethod(presenter)
                    .addMethod(transform)
                    .addMethod(interceptor);
            return false;
        }
        TypeElement type;


        for (Element ele : elementsAll) {
            type = (TypeElement) ele;
            if (!Utils.checkTypeValid(type)) continue;
            ArrayList<PageRule> pageRules = new ArrayList<>();
            PageRule annotation = type.getAnnotation(PageRule.class);
            if (annotation != null) {
                pageRules.add(annotation);
            }
            PageRules rules = type.getAnnotation(PageRules.class);
            if (rules != null) {
                pageRules.addAll(Arrays.asList(rules.value()));
            }
            for (PageRule rule : pageRules) {
                PageParent parent = type.getAnnotation(PageParent.class);
                if (parent == null) continue;
                String typeMirror = type.getSuperclass().toString();
                String allName = typeMirror.substring(typeMirror.indexOf("<") + 1, typeMirror.length() - 1);

                String[] split = allName.split(",");
                String dataClassName = split[0];
                String presenterClassName = split[1];

                MethodCreatePageData.addCode(type, createPageData, rule, parent, dataClassName, presenterClassName);

                MethodCreatePage.addCode(type, createPage, rule);
                MethodCreatePresenter.addCode(presenterClassName, createPresenter, rule);
                MethodCreateTransform.addCode(createTransform, rule);
                MethodCreateInterceptor.addCode(createInterceptor, rule);

                JavaPipeCreator.process(pagePipeType, type, rule);
            }
        }

        MethodSpec page = MethodCreatePage.over(createPage);
        MethodSpec pageData = MethodCreatePageData.over(createPageData);
        MethodSpec presenter = MethodCreatePresenter.over(createPresenter);
        MethodSpec transform = MethodCreateTransform.over(createTransform);
        MethodSpec interceptor = MethodCreateInterceptor.over(createInterceptor);

        JavaPipeCreator.over(pagePipeType);

        creatorImplType.addMethod(page)
                .addMethod(pageData)
                .addMethod(presenter)
                .addMethod(transform)
                .addMethod(interceptor);

        return true;
    }


}
