package com.yiqizuoye.library.page.compile;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiqizuoye.library.page.annotation.ActionRule;
import com.yiqizuoye.library.page.annotation.ActionRules;
import com.yiqizuoye.library.page.annotation.PageParent;
import com.yiqizuoye.library.page.annotation.PageRule;
import com.yiqizuoye.library.page.annotation.PageRules;
import com.yiqizuoye.library.page.compile.action.ActionProcess;
import com.yiqizuoye.library.page.compile.page.JavaPipeCreator;
import com.yiqizuoye.library.page.compile.page.MethodCreateInterceptor;
import com.yiqizuoye.library.page.compile.page.MethodCreatePage;
import com.yiqizuoye.library.page.compile.page.MethodCreatePageData;
import com.yiqizuoye.library.page.compile.page.MethodCreatePresenter;
import com.yiqizuoye.library.page.compile.page.MethodCreateTransform;
import com.yiqizuoye.library.page.compile.page.PageProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        UtilMgr.getMgr().init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processRouteRules(roundEnv);
        return true;

    }

    private static boolean hasCreate = false;

    private void processRouteRules(RoundEnvironment roundEnv) {
        if (hasCreate) return;

        try {
            TypeSpec.Builder creatorImplType = TypeSpec.classBuilder(ClassName.get(Constants.PACKAGE_NAME, "PageCreatorImpl"))
                    .addSuperinterface(Constants.pageCreatorClassName)
                    .addModifiers(Modifier.PUBLIC);

            TypeSpec.Builder pagePipeType = TypeSpec.classBuilder(Constants.pagePipManagerClassName)
                    .addModifiers(Modifier.PUBLIC);

            boolean isOverPage = PageProcess.process(roundEnv, creatorImplType, pagePipeType);
            if (!isOverPage) {
                return;
            }
            hasCreate = true;
            ActionProcess.process(roundEnv, creatorImplType);

            JavaFile.Builder javaBuilder = JavaFile.builder(Constants.PACKAGE_NAME, creatorImplType.build());
            javaBuilder.build().writeTo(UtilMgr.getMgr().getFiler());

            JavaFile.Builder pagePipeJava = JavaFile.builder(Constants.PACKAGE_NAME, pagePipeType.build());
            pagePipeJava.build().writeTo(UtilMgr.getMgr().getFiler());
        } catch (Throwable e) {
            error(e.getMessage(), e);
        }


    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(PageRules.class.getCanonicalName());
        types.add(PageRule.class.getCanonicalName());
        types.add(ActionRule.class.getCanonicalName());
        types.add(ActionRules.class.getCanonicalName());
        return types;
    }

    private void error(String msg, Object... args) {
        UtilMgr.getMgr().getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        UtilMgr.getMgr().getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
