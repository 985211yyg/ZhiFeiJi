package com.example.yungui.zhifeiji.detail;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.about.AboutPreferenceContract;
import com.example.yungui.zhifeiji.imagehandle.ShowActivity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements DetailContract.View ,GestureDetector.OnGestureListener{
    private DetailContract.Presenter presenter;
    private Context context;

    private WebView webView;
    private ImageView imageView, bookMarkImageView;
    private CollapsingToolbarLayout toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean loadImageMode;
    public static final String TAG = DetailFragment.class.getSimpleName();
    private GestureDetector detector;

    public DetailFragment() {

    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        }

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
        inflater.inflate(R.menu.detail_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (id == R.id.more_dialog) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View view = View.inflate(context, R.layout.detail_more_dialog, null);
            bookMarkImageView = (ImageView) view.findViewById(R.id.imageView_bookmark);
            bottomSheetDialog.setContentView(view);
            bottomSheetDialog.show();

            //接下来是各个条目的单击事件
            //收藏
            view.findViewById(R.id.more_bookmark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    presenter.addToOrDeleteFromBookmarks();

                }
            });
            //复制链接
            view.findViewById(R.id.more_link).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
                    presenter.copyLink();
                }
            });

            //在浏览器中打开
            view.findViewById(R.id.more_browser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.openInBrowser();
                    bottomSheetDialog.dismiss();

                }
            });
            //复制文本
            view.findViewById(R.id.more_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.copyText();
                    bottomSheetDialog.dismiss();

                }
            });
            //分享文本
            view.findViewById(R.id.more_share).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetDialog.dismiss();
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
        detector = new GestureDetector(context, this);
        toolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolBarLayout);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.detail_swipeRefreshLayout);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        webView = (WebView) view.findViewById(R.id.webView);
        //允许数值滚动条
        webView.setVerticalScrollBarEnabled(true);
        //支持JavaScript交互
        webView.getSettings().setJavaScriptEnabled(true);
        //设置编码格式防止乱码
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        //禁止使用缓存
        webView.getSettings().setAppCacheEnabled(false);
        //不支持缩放
        webView.getSettings().setSupportZoom(false);
        //设置缓存方式  不加载缓存，只从网络加载数据
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //启用DOM缓存
        webView.getSettings().setDomStorageEnabled(true);

        //载入js,是得JS能够调用原声控件
        webView.addJavascriptInterface(new mJavascriptInterface(getContext()),"imageListener");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //在html中注册监听器，遍历所有的型标签，并为其添加点击事件
                webView.loadUrl("javascript:(function()" +
                        "{" +
                        "var urlObjs =new Array();"+
                        "var objs = document.getElementsByTagName(\"img\");" +
                        "for(var i=0;i<objs.length;i++)" +
                        "{" +
                            "urlObjs[i]=this.src;"+
                            "objs[i].onclick=function(){" +
                                 "window.imageListener.openImage(this.src,urlObjs);" +

                                  "}" +
                            "}" +
                        "})()");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用内置的浏览器作为代理
                presenter.openUrl(view, url);
                return true;
            }
        });

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return detector.onTouchEvent(motionEvent);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.requestData();
            }
        });

        DetailActivity detailActivity = (DetailActivity) getActivity();

        detailActivity.setSupportActionBar((Toolbar) view.findViewById(R.id.detail_toolbar));
        detailActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });

    }

    @Override
    public void stopLoading() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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
        webView.loadDataWithBaseURL("x-data://base", result, "text/html", "utf-8", null);
        this.stopLoading();

    }

    @Override
    public void showResultWithoutBody(String url) {

        webView.loadUrl(url);

    }

    @Override
    public void showCover(String url) {
        Glide.with(context)
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1490269943840&di=41056f06e5eb39b7a27ffdbce1eed62d&imgtype=0&src=http%3A%2F%2Fq.115.com%2Fimgload%3Fr%3D508D254E822C08FA09CA68E63D0F9B1D9FDC2E2A%26u%3DepYa%26s%3DGSQov.LEhLLbHQRH0F1O2Q%26e%3D5")
                .asGif()
                .placeholder(R.drawable.loading)
                .fitCenter()
                .error(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
        Log.i("TAG", "------->>>>>>showCover: " + url);

    }

    @Override
    public void setTitle(String title) {
        setCollapsingToolbarLayoutTitle(title);

    }

    @Override
    public void setImageMode(boolean showImage) {
        webView.getSettings().setBlockNetworkImage(showImage);

    }

    @Override
    public void showBrowserNotFindError() {
        Snackbar.make(toolbar, "没有找到浏览器", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void showTextCopied() {
        Snackbar.make(toolbar, "复制完成", Snackbar.LENGTH_SHORT).show();


    }

    @Override
    public void showTextCopyError() {
        Snackbar.make(toolbar, "复制出错！", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void showAddedToBookmarks() {
        Snackbar.make(toolbar, "收藏成功", Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void showDeletedFromBookmarks() {
        Snackbar.make(toolbar, "取消收藏", Snackbar.LENGTH_SHORT).show();


    }

    private void setCollapsingToolbarLayoutTitle(String title) {
        toolbar.setTitle(title);
        toolbar.setExpandedTitleTextAppearance(R.style.Day_CollapsingToolbar_Expanded);
        toolbar.setCollapsedTitleTextAppearance(R.style.Day_CollapsingToolbar_Collapsed);
        toolbar.setExpandedTitleColor(Color.WHITE);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if (motionEvent1.getX() - motionEvent.getX() > 50) {
            getActivity().finish();
        }
        return false;
    }


    private class mJavascriptInterface{
        private Context context;
        private Intent intent;
        public mJavascriptInterface(Context context) {
            this.context = context;
            intent = new Intent(context, ShowActivity.class);
        }
        @android.webkit.JavascriptInterface
        public void openImage(String imgUrl,ArrayList<String> urls) {

            intent.putExtra("img", imgUrl);
            intent.putStringArrayListExtra("urls", urls);
            Log.i(TAG, "openImage: ");

            context.startActivity(intent);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }
}
