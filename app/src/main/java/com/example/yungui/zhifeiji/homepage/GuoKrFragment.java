package com.example.yungui.zhifeiji.homepage;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.GuoKrAdapter;
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by yungui on 2017/2/14.
 */

public class GuoKrFragment extends Fragment implements GuoKrContract.View {

    private GuoKrPresenter presenter;
    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private GuoKrAdapter guoKrAdapter;
    public static final String TAG = GuoKrFragment.class.getSimpleName();
    private ArrayList<GuoKrStory.ResultBean> resultBeanArrayList=new ArrayList<>();

    private boolean isScrollToLast = false;


    public GuoKrFragment() {

    }

    public static GuoKrFragment getInstance() {

        return new GuoKrFragment();

    }

    /**
     * Called when a fragment is first attached to its context.
     * onCreate(Bundle) will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * 可以获取activity以bundle形式传递过来的参数
     * Called to do initial creation of a fragment. This is called after onAttach(Activity)
     * and before onCreateView(LayoutInflater, ViewGroup, Bundle).
     * Note that this can be called while the fragment's activity is still in the process of being created.
     * As such, you can not rely on things like the activity's content view hierarchy being initialized at this point.
     * If you want to do work once the activity itself is created, see onActivityCreated(Bundle).
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保存fragment的实例，fragment在恢复是会跳过onCreate（）和onDestroy(),加速加载速度
        setRetainInstance(true);
        Bundle arg = getArguments();
        if (arg != null) {
            // do something
        }
    }

    /**
     * 创建视图
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(root);
        presenter.start();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                presenter.loadMore(15);
                            }
                        }.start();

                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrollToLast = dy > 0;

            }
        });
        return root;
    }

    /**
     * 当fragment的activity已创建并且此fragment的视图层次结构实例化时调用。
     * 一旦这些部件到位，它可以用于最终初始化，例如检索视图或恢复状态。
     * 对于使用setRetainInstance（boolean）的片段来保留其实例也是有用的，
     * 因为这个回调告诉片段与新的活动实例完全关联时。
     *
     * @param savedInstanceState
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Called when the Fragment is visible to the user
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Called when the fragment is visible to the user and actively running
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("listresultBeanArrayList);
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
    public void showResult(final ArrayList<GuoKrStory.ResultBean> resultBeanArrayList) {

        if (guoKrAdapter == null) {
            guoKrAdapter = new GuoKrAdapter(context, resultBeanArrayList);
            recyclerView.setAdapter(guoKrAdapter);
            guoKrAdapter.setOnRecycleViewItemClickListener(new onRecycleViewItemClickListener() {
                @Override
                public void OnItemClickListener(int position, View view) {
                    presenter.readDetail(position);
                }
            });
        } else {
            guoKrAdapter.notifyDataSetChanged();
        }


    }


    @Override
    public void setPresenter(GuoKrContract.Presenter presenter) {
        this.presenter = (GuoKrPresenter) presenter;

    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.mRecycleView);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }
}
