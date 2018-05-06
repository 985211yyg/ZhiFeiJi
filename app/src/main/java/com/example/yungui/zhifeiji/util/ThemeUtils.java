package com.example.yungui.zhifeiji.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yungui on 2017/4/1.  设置主题，根据保存在sharedpreferenced的数据来且换主题
 */

public class ThemeUtils {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private Context context;

    public ThemeUtils(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }
}
