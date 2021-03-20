package com.yiqizuoye.library.page.compile;

import com.squareup.javapoet.ClassName;

public class Constants {
    public static final String CLASSNAME_PAGE_PIP_MANAGER = "com.yiqizuoye.library.page.api.PagePipeManager";
    public static final String CLASSNAME_PAGE_CREATOR = "com.yiqizuoye.library.page.api.PageCreator";
    public static final String CLASSNAME_PAGE_PARENT = "com.yiqizuoye.library.page.api.PageParent";
    public static final String CLASSNAME_PAGE_RULE = "com.yiqizuoye.library.page.api.PageRule";
    public static final String CLASSNAME_PAGE_DATA = "com.yiqizuoye.library.page.api.PageData";
    public static final String CLASSNAME_ACTION_DATA = "com.yiqizuoye.library.page.api.ActionData";
    public static final String CLASSNAME_ACTION_RULE = "com.yiqizuoye.library.page.api.ActionRule";
    public static final String CLASSNAME_PAGE_PRESENTER = "com.yiqizuoye.library.page.api.BasePresenter";
    public static final String CLASSNAME_PAGE_INTERCEPTOR = "com.yiqizuoye.library.page.api.PageInterceptor";
    public static final String CLASSNAME_BASE_PAGE = "com.yiqizuoye.library.page.api.BasePage";
    public static final String CLASSNAME_DATA_TRANSFORM = "com.yiqizuoye.library.page.api.PageDataTransform";
    public static final String CLASSNAME_DATA_TRANSFORM_BEAN = "com.yiqizuoye.library.page.api.TransformBean";
    public static final String CLASSNAME_PAGE_QUEUE = "com.yiqizuoye.library.page.api.PageQueue";
    public static final String CLASSNAME_PAGE_MANAGER = "com.yiqizuoye.library.page.api.PageManager";
    public static final String METHOD_NAME_CREATE_PAGE_DATA = "createPageData";
    public static final String METHOD_NAME_CREATE_TRANSFORM = "createTransform";
    public static final String METHOD_NAME_CREATE_PRESENTER = "createPresenter";
    public static final String METHOD_NAME_CREATE_INTERCEPTOR = "createInterceptor";
    public static final String METHOD_NAME_CREATE_PAGE = "createPage";
    public static final String METHOD_NAME_CREATE_ACTION = "createAction";
    public static final String METHOD_NAME_CREATE_ACTION_DATA = "createActionData";

    public static final String PACKAGE_NAME = "com.yiqizuoye.library.page.api";


    public static ClassName basePageClassName = ClassName.bestGuess(Constants.CLASSNAME_BASE_PAGE);
    public static ClassName transformClassName = ClassName.bestGuess(Constants.CLASSNAME_DATA_TRANSFORM);
    public static ClassName transformBeanClassName = ClassName.bestGuess(Constants.CLASSNAME_DATA_TRANSFORM_BEAN);
    public static ClassName pageCreatorClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_CREATOR);
    public static ClassName pageDataClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_DATA);
    public static ClassName actionDataClassName = ClassName.bestGuess(Constants.CLASSNAME_ACTION_DATA);
    public static ClassName pageQueueClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_QUEUE);
    public static ClassName interceptorClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_INTERCEPTOR);
    public static ClassName parentClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_PARENT);
    public static ClassName ruleClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_RULE);
    public static ClassName actionRuleClassName = ClassName.bestGuess(Constants.CLASSNAME_ACTION_RULE);
    public static ClassName presenterClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_PRESENTER);
    public static ClassName pagePipManagerClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_PIP_MANAGER);
    public static ClassName pageManagerClassName = ClassName.bestGuess(Constants.CLASSNAME_PAGE_MANAGER);
}
