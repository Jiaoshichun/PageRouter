package com.yiqizuoye.library.page.api;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Author: jiao
 * Date: 2021/3/17
 * Description:
 * PageActivity基类
 */
public abstract class PageActivity extends AppCompatActivity {

    @CallSuper
    @Override
    public void onBackPressed() {
        if (PageManager.INSTANCE.onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PageManager.INSTANCE.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
