package com.example.yungui.zhifeiji.homepage;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.cdma.CdmaCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.DouBanMomentAdapter;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;
import com.example.yungui.zhifeiji.util.DateFormatter;

import java.util.ArrayList;
import java.util.Calendar;

public class DouBanMomentFragment extends Fragment implements DouBanMomentContract.View {

    private DouBanMomentPresenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private DouBanMomentAdapter douBanMomentAdapter;
    private Context context;
    private FloatingActionButton floatingActionButton;

    public static final String TAG = DouBanMomentFragment.class.getSimpleName();

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private boolean isScrollToLast = false;
    private Calendar douBanCalendarNow = Calendar.getInstance();

    public DouBanMomentFragment() {

    }


    public static DouBanMomentFragment newInstance() {
        DouBanMomentFragment fragment = new DouBanMomentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(root);
        //一切准备就绪，开始事务
        presenter.start();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                presenter.refresh();
            }
        });
         /*
        判断滑动状态
         */
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isScrollToLast = false;

            /*
            如果处在最后一个item，此时加载数据更新
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //当不滚动时
                if (newState == recyclerView.SCROLL_STATE_IDLE) {
                    //获取最后完全可见的item的position
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //获取总的item数量
                    int totalItemCounts = manager.getItemCount();
                    //判断是否处在底部，并且正在滑动,即将滑动到加载更多布局
                    if (lastVisibleItem == (totalItemCounts - 1) && isScrollToLast) {
                        final Calendar calendar = Calendar.getInstance();
                        //自减一天
                        calendar.set(mYear, mMonth, --mDay);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                presenter.loadMore(calendar.getTimeInMillis());
                            }
                        }.start();

                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            /*
            更具dy来显示隐藏fab
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                //dy The amount of vertical scroll  垂直方向上的滚动距离，滚动了就是true
                isScrollToLast = dy > 0;
                if (dy > 0) {
//                    触摸点y坐标减去雨点坐标
                    floatingActionButton.hide();
                } else {
                    floatingActionButton.show();
                }
            }
        });
        return root;
    }


    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {

                refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
                refreshLayout.setRefreshing(true);
            }
        });

    }

    @Override
    public void stopLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);

            }
        });

    }

    @Override
    public void showResult(final ArrayList<DouBanMomentNews.PostsBean> list) {
        refreshLayout.setRefreshing(false);
        if (douBanMomentAdapter == null) {
            douBanMomentAdapter = new DouBanMomentAdapter(context, list);
            recyclerView.setAdapter(douBanMomentAdapter);
            douBanMomentAdapter.setOnRecycleViewItemClickListener(new onRecycleViewItemClickListener() {
                @Override
                public void OnItemClickListener(int position, View view) {
                    //实现接口，将点击的item的position及view回传出来
                    presenter.readDetail(position);

                }
            });
        } else {
            douBanMomentAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void showDialog() {

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                douBanCalendarNow.set(i, i1, i2);

                Calendar temCalendar = Calendar.getInstance();
                temCalendar.set(i,i1,i2);

//                Calendar minCalendar = Calendar.getInstance();
//                minCalendar.set(2014, 5, 12);
//                datePicker.setMinDate(minCalendar.getTimeInMillis());
                Toast.makeText(context, "douBanCalendarNow : "+new DateFormatter().DouBanFormat(temCalendar.getTimeInMillis()), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "temCalendar.getTimeInMillis()"+ new DateFormatter().DouBanFormat(temCalendar.getTimeInMillis()));

                //通知更新数据
                presenter.loadPost(temCalendar.getTimeInMillis(),true);

            }
        }, douBanCalendarNow.get(Calendar.YEAR), douBanCalendarNow.get(Calendar.MONTH), douBanCalendarNow.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }


    @Override
    public void setPresenter(DouBanMomentContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = (DouBanMomentPresenter) presenter;
        }

    }

    @Override
    public void initViews(View view) {
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.mRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

    }
}


