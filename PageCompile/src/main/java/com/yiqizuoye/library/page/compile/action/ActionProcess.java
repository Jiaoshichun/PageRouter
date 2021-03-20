package com.yiqizuoye.library.page.compile.action;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.yiqizuoye.library.page.annotation.ActionRule;
import com.yiqizuoye.library.page.annotation.ActionRules;
import com.yiqizuoye.library.page.annotation.ActionThread;
import com.yiqizuoye.library.page.annotation.PageQueue;
import com.yiqizuoye.library.page.compile.Constants;
import com.yiqizuoye.library.page.compile.TransformBean;
import com.yiqizuoye.library.page.compile.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

/**
 * Author: jiao
 * Date: 2021/3/20
 * Description:
 */
public class ActionProcess {
    public static boolean process(RoundEnvironment roundEnv, TypeSpec.Builder creatorImplType) {
        MethodSpec.Builder method = MethodSpec.overriding(Utils.getOverrideMethod(Constants.pageCreatorClassName, Constants.METHOD_NAME_CREATE_ACTION_DATA));
        method.addStatement("$T<$T> list = new $T<>()", List.class, Constants.actionDataClassName, ArrayList.class);

        MethodSpec.Builder createAction = MethodCreateAction.createMethodBuild();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ActionRules.class);
        Set<Element> elementsAll = new HashSet<>(elements);
        elements = roundEnv.getElementsAnnotatedWith(ActionRule.class);
        elementsAll.addAll(elements);
        if (elementsAll.isEmpty()) {
            creatorImplType.addMethod(method.addStatement("return list").build());
            creatorImplType.addMethod(createAction.build());

            return false;
        }
        TypeElement type;

        method.addStatement("$T actionRule", Constants.actionRuleClassName);
        for (Element ele : elementsAll) {
            type = (TypeElement) ele;
            if (!Utils.checkTypeValid(type)) continue;
            ArrayList<ActionRule> pageRules = new ArrayList<>();
            ActionRule annotation = type.getAnnotation(ActionRule.class);
            if (annotation != null) {
                pageRules.add(annotation);
            }
            ActionRules rules = type.getAnnotation(ActionRules.class);
            if (rules != null) {
                pageRules.addAll(Arrays.asList(rules.value()));
            }
            for (ActionRule rule : pageRules) {

                String typeMirror = type.getInterfaces().get(0).toString();
                String allName = typeMirror.substring(typeMirror.indexOf("<") + 1, typeMirror.length() - 1);

                String[] split = allName.split(",");
                String dataClassName = split[0];

                addCode(type, method, rule, dataClassName);
                MethodCreateAction.addCode(type, createAction);

            }
        }
        allRouter.clear();
        MethodCreateAction.over(createAction);
        creatorImplType.addMethod(method.addStatement("return list").build());
        creatorImplType.addMethod(createAction.build());
        return true;
    }

    private static Map<String, Set<Integer>> allRouter = new HashMap<>();

    private static void addCode(TypeElement type, MethodSpec.Builder method, ActionRule rule, String dataClassName) {
        Set<Integer> integerList = allRouter.get(rule.value());
        if (integerList == null) {
            integerList = new HashSet<>();
            allRouter.put(rule.value(), integerList);

        }
        for (int i : rule.type()) {
            if (!integerList.add(i)) {//包含重复的值
                method.addStatement("出错啦");
                method.addStatement("包含key和type重复的Action,Action的Value: " + rule.value() + "  重复的type:" + i + "  类名:" + type);
            }

        }
        List<? extends Element> elements = type.getEnclosedElements();
        ActionThread.Thread thread = ActionThread.Thread.defaultThread;
        for (Element e : elements) {
            if (e.toString().contains("handleEvent")) {
                ActionThread annotation = e.getAnnotation(ActionThread.class);
                if (annotation != null) {
                    thread = annotation.value();
                    break;
                }

            }
        }

        method.addStatement("actionRule = new $T()", Constants.actionRuleClassName);

//        actionRule.key = "key2";
        method.addStatement("actionRule.key = $S", rule.value());

        //        actionRule.pageClass = TestPage2.class.getName();
        method.addStatement("actionRule.actionClass = $S", type);

        //        actionRule.dataFormatClass = TestBean.class.getName();
        method.addStatement("actionRule.dataFormatClass = $S", dataClassName);

        //        actionRule.presenterClass = TestPresenter.class.getName();
//        TypeMirror typeMirror = (TypeMirror) qualifiedName;

        addType(method, rule.type());

        addDataTransform(method, rule);

        PageQueue pageQueue = type.getAnnotation(PageQueue.class);
        if (pageQueue != null) {
            method.addStatement("actionRule.queue = new $T(" + pageQueue.id() + "," + pageQueue.priority() + ")", Constants.pageQueueClassName);

        }

        method.addStatement("list.add( new $T(actionRule , $T.$N))", Constants.actionDataClassName, ClassName.get(thread.getClass()), thread.name());


    }

    private static void addDataTransform(MethodSpec.Builder method, ActionRule rule) {
//        actionRule.transforms = new Class[]{DataTransform.class};
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
        builder.add("actionRule.transformBeans = new $T[]{", Constants.transformBeanClassName);
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
        //        actionRule.type = new int[]{1};
        StringBuilder builder = new StringBuilder();
        builder.append("actionRule.type = new int[]{");
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
