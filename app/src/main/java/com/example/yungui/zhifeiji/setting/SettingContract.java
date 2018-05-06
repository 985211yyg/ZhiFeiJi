package com.example.yungui.zhifeiji.setting;

import android.preference.Preference;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;

/**
 * Created by yungui on 2017/3/17.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter> {
        void showCleanImageCacheDone();
    }
    interface Presenter extends BasePresenter {
        //无图模式
        void setNoImageMode(Preference preference);
        //在内置浏览器中加载
        void setBrowserInInnerBrowser(Preference preference);
        //设置收藏文章的保存天数
        void setArticleSaveDays(Preference preference,Object value);
        //清除图片缓存
        void cleanImageCache(Preference preference);
        //设置显是的保存天数
        String getTimeSummary();

    }
}
