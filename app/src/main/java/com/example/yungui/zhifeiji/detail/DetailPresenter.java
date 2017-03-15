package com.example.yungui.zhifeiji.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.bean.ZhiHuDailyStory;
import com.example.yungui.zhifeiji.interfaze.onStringListener;
import com.example.yungui.zhifeiji.util.Api;
import com.example.yungui.zhifeiji.util.NetWorkState;
import com.example.yungui.zhifeiji.util.StringModelImp;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by yungui on 2017/3/13.
 */

public class DetailPresenter implements DetailContract.Presenter {
    private DetailContract.View mView;
    private Context mContext;
    private StringModelImp modelImp;
    private SharedPreferences sharedPreferences;
    public static final String TAG = DetailPresenter.class.getSimpleName();

    // 由intent提供数据
    private BeanType beanType;
    private String title;
    private int id;

    @Override
    public String toString() {
        return "DetailPresenter{" +
                "beanType=" + beanType +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }

    private String coverUrl;


    public void setBeanType(BeanType beanType) {
        this.beanType = beanType;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    /*
    ---------------------------分割线------------------------------------
     */
    public DetailPresenter(Context context, DetailContract.View view) {
        mView= view;
        mContext = context;
        /*
        为传递过来的view设置presenter
         */
        view.setPresenter(this);
        modelImp = new StringModelImp(mContext);
//        sharedPreferences = context.getSharedPreferences("user_setting", Context.MODE_PRIVATE);

    }

    @Override
    public void start() {

    }

    @Override
    public void openInBrowser() {

    }

    @Override
    public void shareAsText() {
        /*
        需要获取标题和链接
         */

    }

    @Override
    public void openUrl(WebView webView, String url) {

    }

    @Override
    public void copyText() {

    }

    @Override
    public void copyLink() {

    }

    @Override
    public void addToOrDeleteFromBookmarks() {

    }

    @Override
    public boolean queryIfIsBookmarked() {
        return false;
    }

    @Override
    public void requestData() {
//        首先判断id和type是否为空
        if (this.id == 0 || this.beanType == null) {
            mView.showLoadingError();
            return;
        }
        //开始加载数据，显示UI
        mView.showLoading();
        //设置title
        mView.setTitle(this.title);
        //设置封面
        mView.showCover(this.coverUrl);
        Log.i(TAG, "————————————》》》》》requestData: "+toString());
        //从设置中查看是否设置是否为显示图片模式
//        mView.setImageMode(sharedPreferences.getBoolean("no_pic_mode",false));
//        更具beantype确定加载请求数据的类型
        switch (beanType) {
            case TYPE_ZHIHU:
                if (NetWorkState.NetWorkConnected(mContext)) {
                    //开始请求数据
                    modelImp.load(Api.ZHIHU_NEWS + id, new onStringListener() {
                        @Override
                        public void onError(VolleyError error) {

                        }

                        @Override
                        public void onSuccess(String result) {
                            Log.i(TAG, "------>>>>>>>>>onSuccess: "+result);
                            //解析数据
                            Gson gson = new Gson();
                            try {
                                ZhiHuDailyStory story = gson.fromJson(result, ZhiHuDailyStory.class);
                                if (story.getBody() == null) {
                                    mView.showResultWithoutBody(story.getShare_url());
                                } else {
                                    Log.i(TAG, "-----------Css: "+story.getCss()+"---------JS:"+story.getJs());
                                    mView.showResult(convertZhihuContent(story.getBody()));
                                }

                            } catch (JsonSyntaxException e) {
                                mView.showLoadingError();

                            }
                            mView.stopLoading();

                        }
                    });

                } else {
                    /*
                    如果没有网络
                     */

                }
                break;
            case TYPE_DOUBAN:
                break;
            case TYPE_GOUKR:
                break;
        }
    }

    //处理转换返回的数据
    private String convertZhihuContent(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // api中还有js的部分，这里不再解析js
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

        String theme = "<body className=\"\" onload=\"onLoaded()\">";
         //阻止一个网页
        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString();
    }
}
