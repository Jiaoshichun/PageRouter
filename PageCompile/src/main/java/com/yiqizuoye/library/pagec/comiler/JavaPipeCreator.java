package com.yiqizuoye.library.pagec.comiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiqizuoye.library.page.annotation.PagePipe;
import com.yiqizuoye.library.page.annotation.PageRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
public class JavaPipeCreator {
//    private static class Pipe {
//        public Pipe(PagePipe pagePipe, PageRule pageRule, TypeElement typeElement) {
//            this.pagePipe = pagePipe;
//            this.pageRule = pageRule;
//            this.typeElement = typeElement;
//        }
//
//        public PagePipe pagePipe;
//        public TypeElement typeElement;
//        public PageRule pageRule;
//    }

    private static class PipeData {
        public String methodKey;
        public String returnType;
        public String routerKey;

        public PipeData(String routerKey, String methodKey, String returnType) {
            this.methodKey = methodKey;
            this.routerKey = routerKey;
            this.returnType = returnType;
        }

        public List<TypeElement> pages = new ArrayList<>();
    }

    private static Map<String, PipeData> allPipeData = new HashMap<>();

    public static void process(TypeSpec.Builder pagePipeType, TypeElement typeElement, PageRule rule) {
        PagePipe pagePipe = null;
        ExecutableElement methodElement = null;
        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
        for (Element e : enclosedElements
        ) {
            PagePipe p = e.getAnnotation(PagePipe.class);
            if (p != null) {
                methodElement = (ExecutableElement) e;
                if ("getPipe".equals(methodElement.getSimpleName().toString())) {
                    pagePipe = p;
                    break;
                }

            }

        }
        if (pagePipe == null) return;
        TypeMirror returnType = methodElement.getReturnType();

        PipeData pipeData = allPipeData.get(pagePipe.value());
        if (pipeData == null) {
            pipeData = new PipeData(rule.value(), pagePipe.value(), returnType.toString());
            allPipeData.put(pagePipe.value(), pipeData);
        }
        if (!pipeData.methodKey.equals(pagePipe.value())) {
            pagePipeType.addMethod(MethodSpec.methodBuilder("方法名重复啦 " + typeElement.toString() + " methodName:" + pagePipe.value()).build());
            return;
        }
        if (!pipeData.returnType.equals(returnType.toString())) {
            pagePipeType.addMethod(MethodSpec.methodBuilder("相同的方法名返回的接口类型不同 " + typeElement.toString() + " methodName:" + pagePipe.value()).build());
            return;
        }
        pipeData.pages.add(typeElement);
    }

    //    public static TestPipe getTestNamePipe() {
//        BasePage page = PageManager.getPage("key");
//        if (page == null) return null;
//        if (page instanceof TestPage) {
//            return ((TestPage) page).getPipe();
//        } else if (page instanceof TestPageType2) {
//            return ((TestPageType2) page).getPipe();
//        }
//        return null;
//    }
    public static void over(TypeSpec.Builder pagePipeType) {
//        pagePipeType.addMethod(MethodSpec.methodBuilder(  "nihao").addStatement("$S",allPipeData.size()).build());

        for (Map.Entry<String, PipeData> entry : allPipeData.entrySet()) {
            String key = entry.getKey();
            PipeData pipeData = entry.getValue();
            MethodSpec.Builder builder = MethodSpec.methodBuilder(key)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .addAnnotation(ClassName.bestGuess("androidx.annotation.Nullable"))
                    .returns(ClassName.bestGuess(pipeData.returnType));

            builder.addStatement("$T page = $T.getPage($S)", Constants.basePageClassName, Constants.pageManagerClassName, pipeData.routerKey);
            builder.beginControlFlow("if (page == null)");
            builder.addStatement("return null");
            builder.endControlFlow();

            boolean hasBegin = false;
            for (TypeElement p : pipeData.pages) {
                if (!hasBegin) {
                    hasBegin = true;
                    builder.beginControlFlow("if (page instanceof $T)", p);
                } else {
                    builder.nextControlFlow("else if (page instanceof $T)", p);
                }
                builder.addStatement(" return (($T) page).getPipe()", p);
            }
            builder.endControlFlow();
            builder.addStatement("return null");
            pagePipeType.addMethod(builder.build());
        }
//        pagePipeType.addMethod(methodSpe.build());
    }
}
