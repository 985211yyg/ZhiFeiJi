package com.example.yungui.zhifeiji.bookmarks;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.adapter.BookMarkAdapter;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;
import com.example.yungui.zhifeiji.interfaze.onRecycleViewItemClickListener;

import java.util.ArrayList;


public class BookMarkFragment extends Fragment implements BookFragmentContract.View {
    private Context mContext;
    private BookMarkPresenter bookMarkPresenter;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private BookMarkAdapter bookMarkAdapter;

    public static final String TAG = BookMarkFragment.class.getSimpleName();

    private SearchView searchView;
    private String searchWords;

    public BookMarkFragment() {

    }

    public static BookMarkFragment newInstance() {
        BookMarkFragment fragment = new BookMarkFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_book_mark, container, false);
        initViews(root);
        bookMarkPresenter.start();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bookMarkPresenter.refresh();
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.bookmark_menu, menu);
        setSearchView(menu);

//        //关联检索配置与searchView
//        /*
//        调用getSearchableInfo()返回一个SearchableInfo由检索配置XML文件创建的对象。
//        检索配置与SearchView正确关联后，当用户提交一个搜索请求时，
//        SearchView会以ACTION_SEARCH intent启动一个activity。
//         */
//        //获取searchView
//        SearchView sv = (SearchView) menu.findItem(R.id.bookmark_search).getActionView();
//        SearchManager searchManager = (SearchManager) mContext.getSystemService(mContext.SEARCH_SERVICE);
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(new ComponentName(mContext, SearchActivity.class));
//        sv.setSearchableInfo(searchableInfo);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "--------onQueryTextSubmit: " + query);
                searchWords = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "--------onQueryTextSubmit: " + newText);
                searchWords = newText;
                return true;
            }
        });

        /*
        当点击开始按钮是，开始搜索
         */
        searchView.findViewById(R.id.search_go_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookMarkPresenter.search(searchWords,true);
            }
        });


    }

    //初始化searchView
    private void setSearchView(Menu menu) {
        //获取searchview所在的位置
        MenuItem item = menu.getItem(0);
        //实例化searchView
        searchView = new SearchView(mContext);
        //设置展开后图标样式，为true是搜索图标在搜索框内，FALSE是在外
        searchView.setIconifiedByDefault(false);
        //设置提示内容
        searchView.setQueryHint("输入搜索");
        //设置最右侧为提交按钮
        searchView.setSubmitButtonEnabled(true);
        //添加searchView
        item.setActionView(searchView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.bookmark_search:
                //交给searchView来处理事件
                break;
            case R.id.looklook:
                bookMarkPresenter.lookLook();
                break;
            //有搜索界面点击返回界面是
            case android.R.id.home:
                bookMarkPresenter.refresh();
                break;
        }
        return true;
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
                refreshLayout.setRefreshing(true);
                bookMarkPresenter.loadPost(true);

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
    public void showResult(ArrayList<ZhiHuDailyNews.Question> zhihuList
            , ArrayList<GuoKrStory.ResultBean> guokrList
            , ArrayList<DouBanMomentNews.PostsBean> doubanList
            , ArrayList<Integer> itemType) {
        if (bookMarkAdapter == null) {
            bookMarkAdapter = new BookMarkAdapter(getContext(), doubanList, guokrList, zhihuList, itemType);
            recyclerView.setAdapter(bookMarkAdapter);
        } else {
            bookMarkAdapter.notifyDataSetChanged();
        }

        bookMarkAdapter.setOnItemClickListener(new onRecycleViewItemClickListener() {
            @Override
            public void OnItemClickListener(int position, View view) {
                //position点击的位置
                bookMarkPresenter.readDetail(position);
            }
        });
    }

    @Override
    public void showSearchResult(ArrayList<ZhiHuDailyNews.Question> zhihuList
            , ArrayList<GuoKrStory.ResultBean> guokrList
            , ArrayList<DouBanMomentNews.PostsBean> doubanList
            , ArrayList<Integer> itemType) {

    }


    @Override
    public void setPresenter(BookFragmentContract.Presenter presenter) {
        this.bookMarkPresenter = (BookMarkPresenter) presenter;

    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.bookmark_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bookmark_refresh);
    }


}
