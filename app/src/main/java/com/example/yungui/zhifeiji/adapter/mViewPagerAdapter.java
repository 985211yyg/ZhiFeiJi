package com.example.yungui.zhifeiji.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yungui.zhifeiji.homepage.DouBanMomentFragment;
import com.example.yungui.zhifeiji.homepage.GuoKrContract;
import com.example.yungui.zhifeiji.homepage.GuoKrFragment;
import com.example.yungui.zhifeiji.homepage.ZhuHuDailyFragment;

/**
 * Created by yungui on 2017/2/12.
 */

public class mViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"知乎日报", "果壳精选", "豆瓣时刻"};
    private Context context;

    private ZhuHuDailyFragment zhuHuDailyFragment;
    private GuoKrFragment guoKrFragment;
    private DouBanMomentFragment douBanMomentFragment;

    public ZhuHuDailyFragment getZhuHuDailyFragment() {
        return zhuHuDailyFragment;
    }

    public GuoKrFragment getGuoKrFragment() {
        return guoKrFragment;
    }

    public DouBanMomentFragment getDouBanMomentFragment() {
        return douBanMomentFragment;
    }

    public mViewPagerAdapter(FragmentManager fm, Context context,
                             ZhuHuDailyFragment zhuHuDailyFragment,
                             GuoKrFragment guoKrFragment,
                             DouBanMomentFragment douBanMomentFragment) {
        super(fm);
        this.context = context;
        this.zhuHuDailyFragment = zhuHuDailyFragment;
        this.guoKrFragment = guoKrFragment;
        this.douBanMomentFragment = douBanMomentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return zhuHuDailyFragment;
        } else if (position == 1) {
            return guoKrFragment;
        } else if (position == 2) {
            return douBanMomentFragment;
        }
        return ZhuHuDailyFragment.getInstance();
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
