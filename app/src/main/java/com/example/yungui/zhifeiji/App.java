package com.example.yungui.zhifeiji;

import android.app.Application;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatDelegate;

import com.example.yungui.zhifeiji.db.DataBaseHelper;

/**
 * Created by yungui on 2017/4/20.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_YES;
//        if (mode == Configuration.UI_MODE_NIGHT_YES) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        } else if (mode == Configuration.UI_MODE_NIGHT_NO) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }
        DataBaseHelper.getInstance(getApplicationContext(),"ZhiFeiJi",null,1);
    }
}
