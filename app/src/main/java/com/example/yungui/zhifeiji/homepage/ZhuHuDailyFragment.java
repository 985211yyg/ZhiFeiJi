package com.example.yungui.zhifeiji.homepage;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.ZhuHuDailyAdapter;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yungui on 2017/2/9.
 */

public class ZhuHuDailyFragment extends Fragment implements ZhiHuDailyContract.View {
    private ZhuHuDailyAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ZhiHuDailyContract.Presenter mPresenter;
    private FloatingActionButton floatingActionButton;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private DatePickerDialog datePickerDialog;


    //构造方法
    public ZhuHuDailyFragment() {

    }

    //单例模式
    public static ZhuHuDailyFragment getInstance() {
        return new ZhuHuDailyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(root);
        mPresenter.start();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
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
                                mPresenter.loadMore(calendar.getTimeInMillis());
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
                    floatingActionButton.hide();

                } else {
                    floatingActionButton.show();

                }

            }
        });

        return root;
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
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
    public void showResult(ArrayList<ZhiHuDailyNews.Question> item) {
        if (adapter == null) {
            adapter = new ZhuHuDailyAdapter(getContext(), item);
            adapter.setItemClickListener(new onRecycleViewItemClickListener() {
                @Override
                public void OnItemClickListener(int position, View view) {
                    //将点击的项目返回给presenter进行处理
                    mPresenter.readDetail(position);

                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void pickDialog() {

    }


    @Override
    public void setPresenter(ZhiHuDailyContract.Presenter presenter) {
        if (presenter != null) {
            this.mPresenter = presenter;
        }

    }

    @Override
    public void initViews(View view) {
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        recyclerView = (RecyclerView) view.findViewById(R.id.mRecycleView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        recyclerView.setHasFixedSize(true);
        //为recycleview设置layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //添加分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

    }
}
