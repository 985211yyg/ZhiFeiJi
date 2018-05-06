package com.example.yungui.zhifeiji.homepage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.mViewPagerAdapter;

import java.util.Calendar;
import java.util.Random;


public class MainFragment extends Fragment {

    private FragmentManager fragmentManager;
    private Context context;

    private TabLayout mTabLayOut;
    private ViewPager mViewPager;
    private FloatingActionButton floatingActionButton;
    private mViewPagerAdapter fragmentPagerAdapter;
    private View root;

    //定义事务
    private ZhiHuDailyPresenter zhiHuDailyPresenter;
    private GuoKrPresenter guoKrPresenter;
    private DouBanMomentPresenter douBanMomentPresenter;

    //定义片段
    private ZhuHuDailyFragment zhuHuDailyFragment;
    private GuoKrFragment guoKrFragment;
    private DouBanMomentFragment douBanMomentFragment;

    public static final String TAG = MainFragment.class.getSimpleName();

    //记录当前是那个fragment在显示
    private int fragmentID;

    //固定选择日期后的calendar的显示时间
    private Calendar zhiHuCalendarNow = Calendar.getInstance();
    private Calendar douBanCalendarNow = Calendar.getInstance();


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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (fragmentID) {
                    //知乎
                    case 0:
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                zhiHuCalendarNow.set(i, i1, i2);
                                //通知更新数据
                                zhiHuDailyPresenter.loadPost(zhiHuCalendarNow.getTimeInMillis(), true);
//                                Log.i(TAG, "zhiHuCalendarNow.getTimeInMillis(): " + zhiHuCalendarNow.getTimeInMillis());

                            }
                        }, zhiHuCalendarNow.get(Calendar.YEAR), zhiHuCalendarNow.get(Calendar.MONTH)
                                , zhiHuCalendarNow.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();

                        break;
                    //豆瓣
                    case 2:
                        DatePickerDialog dateDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                douBanCalendarNow.set(i, i1, i2);
                                //通知更新数据
                                douBanMomentPresenter.loadPost(douBanCalendarNow.getTimeInMillis(), true);

                            }
                        }, douBanCalendarNow.get(Calendar.YEAR), douBanCalendarNow.get(Calendar.MONTH)
                                , douBanCalendarNow.get(Calendar.DAY_OF_MONTH));
                        dateDialog.show();
//                        fragmentPagerAdapter.getDouBanMomentFragment().showDialog();
                        break;

                }

            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mian_lookllok) {
            looklook();
        }
        return true;
    }

    private void looklook() {
        /*
        3以内的数字随机挑选一个整数  及1，2
         */
        int type = new Random().nextInt(3);

        switch (type) {

            case 0:
                zhiHuDailyPresenter.lookLook();
                Log.i(TAG, "zhiHuDailyPresenter>>>>>>>>>>>>looklook: ");
                break;
            case 1:
                guoKrPresenter.lookLook();
                Log.i(TAG, "guoKrPresenter>>>>>>>>>>>>looklook: ");

                break;
            default:
                douBanMomentPresenter.lookLook();
                Log.i(TAG, "douBanMomentPresenter>>>>>>>>>>>>looklook: ");

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

                /*
                0 对应知乎  2 对应豆瓣
                 */
                fragmentID = tab.getPosition();

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
        getChildFragmentManager().putFragment(outState,"zhuHuDailyFragment",zhuHuDailyFragment);
        getChildFragmentManager().putFragment(outState,"douBanMomentFragment",douBanMomentFragment);
        getChildFragmentManager().putFragment(outState,"guoKrFragment",guoKrFragment);

    }


}
