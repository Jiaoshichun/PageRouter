package com.yiqizuoye.library.page.api;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Author: jiao
 * Date: 2021/3/16
 * Description:
 * 页面回调，跳转界面时通过{@link PageRouter#setResultCallBack(PageResultCallBack)}  设置回调
 * 当跳转的界面调用 {@link BasePage#setResult(int, Bundle)} 时，会回调该方法
 */
public interface PageResultCallBack {
    void onResult(int result, @Nullable Bundle data);

}
