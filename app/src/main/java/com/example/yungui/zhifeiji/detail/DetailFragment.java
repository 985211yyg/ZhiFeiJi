package com.example.yungui.zhifeiji.detail;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.about.AboutPreferenceContract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements DetailContract.View {
    private DetailContract.Presenter presenter;
    private Context context;

    private WebView webView;
    private ImageView imageView;
    private CollapsingToolbarLayout toolbar;
    private boolean loadImageMode;

    public DetailFragment() {
        /*
        因为在fragment实例添加到activity中的时候，已经将fragment（View）与presenter连接，所以在fragment创建是实例化是不对的
         */
        //实例化presenter将view传递到presenter中，回调view的方法
        presenter = new DetailPresenter(getContext(), this);

    }


    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews(root);
        presenter.requestData();
        //是fragment支持menu菜单
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (id == R.id.more_dialog) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View view = View.inflate(context, R.layout.detail_more_dialog, null);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();

            //接下来是各个条目的单击事件
            //收藏
            view.findViewById(R.id.more_bookmark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.addToOrDeleteFromBookmarks();
                }
            });
            //复制链接
            view.findViewById(R.id.more_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.copyLink();
                }
            });
            //在浏览器中打开
            view.findViewById(R.id.more_browser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.openInBrowser();
                }
            });
            //复制文本
            view.findViewById(R.id.more_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.copyText();
                }
            });
            //分享文本
            view.findViewById(R.id.more_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.shareAsText();
                }
            });

        }

        return true;
    }

    /*


             */
    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }

    }

    @Override
    public void initViews(View view) {
        toolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolBarLayout);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        webView = (WebView) view.findViewById(R.id.webView);
        //取消滚动条
        webView.setVerticalScrollBarEnabled(false);
        //支持JavaScript交互
        webView.getSettings().setJavaScriptEnabled(true);
        //禁止使用缓存
        webView.getSettings().setAppCacheEnabled(false);
        //不支持缩放
        webView.getSettings().setSupportZoom(false);
        //设置缓存方式  不加载缓存，只从网络加载数据
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //启用DOM缓存
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                presenter.openUrl(view,url);
                return true;
            }
        });

        DetailActivity detailActivity = (DetailActivity) getActivity();

        detailActivity.setSupportActionBar((Toolbar) view.findViewById(R.id.detail_toolbar));
        detailActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showLoadingError() {
        Snackbar.make(imageView, "加载失败", Snackbar.LENGTH_INDEFINITE).setAction("点击重试", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.requestData();
            }
        }).show();

    }

    @Override
    public void showShareError() {
        Snackbar.make(imageView, "分享失败", Snackbar.LENGTH_INDEFINITE).show();

    }

    @Override
    public void showResult(String result) {
        webView.loadDataWithBaseURL("x-data://base",result,"text/html","utf-8",null);

    }

    @Override
    public void showResultWithoutBody(String url) {
        webView.loadUrl(url);

    }

    @Override
    public void showCover(String url) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(R.mipmap.timg)
                .centerCrop()
                .error(R.mipmap.timg)
                .into(imageView);
        Log.i("TAG", "------->>>>>>showCover: " + url);

    }

    @Override
    public void setTitle(String title) {
        setCollapsingToolbarLayoutTitle(title);

    }

    @Override
    public void setImageMode(boolean showImage) {
       webView.getSettings().setBlockNetworkImage(loadImageMode);

    }

    @Override
    public void showBrowserNotFindError() {

    }

    @Override
    public void showTextCopied() {

    }

    @Override
    public void showTextCopyError() {

    }

    @Override
    public void showAddedToBookmarks() {

    }

    @Override
    public void showDeletedFromBookmarks() {

    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        toolbar.setTitle(title);
        toolbar.setExpandedTitleTextAppearance(R.style.CollapsingToolbar_Expanded);
        toolbar.setCollapsedTitleTextAppearance(R.style.CollapsingToolbar_Collapsed);
        toolbar.setExpandedTitleColor(Color.WHITE);
    }




}
