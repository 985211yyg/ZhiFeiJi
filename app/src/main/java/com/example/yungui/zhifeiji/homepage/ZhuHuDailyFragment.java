package com.example.yungui.zhifeiji.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.ZhuHuDailyAdapter;
import com.example.yungui.zhifeiji.bean.ZhuHuDailyNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewOnClickListener;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/2/9.
 */

public class ZhuHuDailyFragment extends Fragment implements ZhiHuDailyContract.View {
    private ZhuHuDailyAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ZhiHuDailyContract.Presenter mPresenter;

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
        return root;
    }

    @Override
    public void showError() {

    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(false);

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showResult(ArrayList<ZhuHuDailyNews.Question> item) {
        if (adapter == null) {
            System.out.println(">>>>>>>>>>" + item.toString());
            adapter = new ZhuHuDailyAdapter(getContext(), item);
            adapter.setItemClickListener(new onRecycleViewOnClickListener() {
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
        recyclerView = (RecyclerView) view.findViewById(R.id.mRecycleView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        recyclerView.setHasFixedSize(true);
        //为recycleview设置layoutmanager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
