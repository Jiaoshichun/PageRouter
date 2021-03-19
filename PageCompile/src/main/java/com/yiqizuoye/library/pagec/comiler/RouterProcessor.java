package com.yiqizuoye.library.pagec.comiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiqizuoye.library.page.annotation.PageParent;
import com.yiqizuoye.library.page.annotation.PageRule;
import com.yiqizuoye.library.page.annotation.PageRules;

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
import javax.lang.model.type.TypeMirror;
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

    private void processRouteRules(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(PageRules.class);
        Set<Element> elementsAll = new HashSet<>(elements);
        elements = roundEnv.getElementsAnnotatedWith(PageRule.class);
        elementsAll.addAll(elements);
        if (elementsAll.isEmpty()) {
            return;
        }
        TypeElement type;
        try {

            TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(ClassName.get(Constants.PACKAGE_NAME, "PageCreatorImpl"))
                    .addSuperinterface(Constants.pageCreatorClassName)
                    .addModifiers(Modifier.PUBLIC);

            TypeSpec.Builder pagePipeType = TypeSpec.classBuilder(Constants.pagePipManagerClassName)
                    .addModifiers(Modifier.PUBLIC);

            MethodSpec.Builder createPage = MethodCreatePage.createMethodBuild();
            MethodSpec.Builder createPageData = MethodCreatePageData.createMethodBuild();
            MethodSpec.Builder createTransform = MethodCreateTransform.createMethodBuild();
            MethodSpec.Builder createInterceptor = MethodCreateInterceptor.createMethodBuild();
            MethodSpec.Builder createPresenter = MethodCreatePresenter.createMethodBuild();


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

            TypeSpec.Builder typeSpec = typeBuilder.addMethod(page)
                    .addMethod(pageData)
                    .addMethod(presenter)
                    .addMethod(transform)
                    .addMethod(interceptor);
            JavaFile.Builder javaBuilder = JavaFile.builder(Constants.PACKAGE_NAME, typeSpec.build());
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
        return types;
    }

    private void error(String msg, Object... args) {
        UtilMgr.getMgr().getMessager().printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        UtilMgr.getMgr().getMessager().printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
