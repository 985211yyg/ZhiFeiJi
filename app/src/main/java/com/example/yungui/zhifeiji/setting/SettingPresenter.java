package com.example.yungui.zhifeiji.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.yungui.zhifeiji.R;

/**
 * Created by yungui on 2017/3/17.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private Context mContext;
    private SettingContract.View view;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    public static final int CLEAN_IMAGE_DONE = 0X123;

    /*
    当清理图片的好事操作完成时，通知view发出清理成功的提示
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == CLEAN_IMAGE_DONE) {

                view.showCleanImageCacheDone();
            }
        }
    };


    public SettingPresenter(Context context, SettingContract.View view) {
        mContext = context;
        this.view = view;
        //为view设置presenter
        view.setPresenter(this);
        sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void start() {

    }

    /*
    存储设置
     */
    @Override
    public void setNoImageMode(Preference preference) {
        //获取设置页面的设置，然后存储到新的设置sharePreference中
        editor.putBoolean("image_mode", preference.getSharedPreferences().getBoolean("image_mode", false));
        //提交
        editor.apply();

    }

    @Override
    public void setBrowserInInnerBrowser(Preference preference) {
        //获取设置页面的设置，然后存储到新的设置sharePreference中
        editor.putBoolean("inner_browser", preference.getSharedPreferences().getBoolean("inner_browser", false));
        //提交
        editor.apply();

    }

    @Override
    public void setArticleSaveDays(Preference preference, Object value) {
        editor.putString("store_article", (String) value);
        editor.apply();
    }

    /*

      需要开启新的线程来执行次耗时操作
     */
    @Override
    public void cleanImageCache(Preference preference) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(mContext).clearDiskCache();
                Message message = new Message();
                message.what = CLEAN_IMAGE_DONE;
                handler.sendMessage(message);
            }
        }).start();
        Glide.get(mContext).clearMemory();

    }

    /*
    给设置页面设置summary
     */
    @Override
    public String getTimeSummary() {
        String[] options = mContext.getResources().getStringArray(R.array.default_store_day);
        //从sharepreference中获取设置
        String days = sharedPreferences.getString("store_article", "7");
        switch (days) {
            case "3":
                return options[0];
            case "10":
                return options[2];
            case "15":
                return options[3];
            case "7":
                return options[1];
        }

        return null;

    }
}
