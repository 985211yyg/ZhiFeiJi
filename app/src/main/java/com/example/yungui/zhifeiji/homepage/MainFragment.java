package com.example.yungui.zhifeiji.homepage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.mViewPagerAdapter;

import java.util.Random;


public class MainFragment extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;

    private TabLayout mTabLayOut;
    private ViewPager mViewPager;
    private FloatingActionButton floatingActionButton;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private View root;

    //定义事务
    private ZhiHuDailyPresenter zhiHuDailyPresenter;
    private GuoKrPresenter guoKrPresenter;
    private DouBanMomentPresenter douBanMomentPresenter;

    //定义片段
    private ZhuHuDailyFragment zhuHuDailyFragment;
    private GuoKrFragment guoKrFragment;
    private DouBanMomentFragment douBanMomentFragment;


    public MainFragment() {
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    //当fragment被添加时初始化一些操作
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        //从fragment中获取fragmentManager
        fragmentManager = getChildFragmentManager();

        //判断是存储了状态,有,恢复数据，没有  则初始化
        if (savedInstanceState != null) {
            zhuHuDailyFragment = (ZhuHuDailyFragment) fragmentManager.getFragment(savedInstanceState, "zhuHuDailyFragment");
            guoKrFragment = (GuoKrFragment) fragmentManager.getFragment(savedInstanceState, "guoKrFragment");
            douBanMomentFragment = (DouBanMomentFragment) fragmentManager.getFragment(savedInstanceState, "douBanMomentFragment");
        } else {
            //创建view实例
            zhuHuDailyFragment = ZhuHuDailyFragment.getInstance();
            guoKrFragment = GuoKrFragment.getInstance();
            douBanMomentFragment = DouBanMomentFragment.newInstance();
        }

        //实例化事务
        zhiHuDailyPresenter = new ZhiHuDailyPresenter(context, zhuHuDailyFragment);
        douBanMomentPresenter = new DouBanMomentPresenter(context, douBanMomentFragment);
        guoKrPresenter = new GuoKrPresenter(context, guoKrFragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        //初始化控件
        initView(root);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_look) {
            looklook();
        }
        return true;
    }

    private void looklook() {
        int type = new Random().nextInt(3);

        switch (type) {
            case 1:
                zhiHuDailyPresenter.lookLook();
                break;
            case 2:
                guoKrPresenter.lookLook();
                break;
            case 3:
                douBanMomentPresenter.lookLook();
        }
    }

    private void initView(View root) {
        mTabLayOut = (TabLayout) root.findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) root.findViewById(R.id.mViewPager);
        floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab);
        mViewPager.setOffscreenPageLimit(3);
        //当tab选项位于果壳精选时隐藏floatingActionButton
        mTabLayOut.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    floatingActionButton.hide();
                } else {
                    floatingActionButton.show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        fragmentPagerAdapter = new mViewPagerAdapter(fragmentManager, context,
                zhuHuDailyFragment,
                guoKrFragment,
                douBanMomentFragment);

        mViewPager.setAdapter(fragmentPagerAdapter);
        mTabLayOut.setupWithViewPager(mViewPager);
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //保存fragment
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
