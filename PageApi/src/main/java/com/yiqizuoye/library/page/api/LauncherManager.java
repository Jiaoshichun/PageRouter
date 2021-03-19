package com.yiqizuoye.library.page.api;

import android.app.Activity;

/**
 * Author: jiao
 * Date: 2021/3/19
 * Description:
 */
public class LauncherManager {
    public static int start(Launcher launcher, Activity activity, RouterData routerData) {
        return launcher.start(activity, routerData);
    }
}
