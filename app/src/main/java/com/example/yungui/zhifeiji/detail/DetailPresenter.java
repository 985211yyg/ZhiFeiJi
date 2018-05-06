package com.example.yungui.zhifeiji.detail;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;

import com.android.volley.VolleyError;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentStory;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyStory;
import com.example.yungui.zhifeiji.customtabs.CustomTabActivityHelper;
import com.example.yungui.zhifeiji.customtabs.CustomTabFallback;
import com.example.yungui.zhifeiji.db.DataBaseHelper;
import com.example.yungui.zhifeiji.innerbrowser.InnerBrowserActivity;
import com.example.yungui.zhifeiji.interfaze.onStringListener;
import com.example.yungui.zhifeiji.util.Api;
import com.example.yungui.zhifeiji.util.NetWorkState;
import com.example.yungui.zhifeiji.util.StringModelImp;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yungui on 2017/3/13.
 */

public class DetailPresenter implements DetailContract.Presenter, CustomTabFallback {
    private DetailContract.View mView;
    private Context mContext;
    private StringModelImp modelImp;
    private SharedPreferences sharedPreferences;
    public static final String TAG = DetailPresenter.class.getSimpleName();

    private ZhiHuDailyStory zhiHuDailyStory;
    private DouBanMomentStory douBanMomentStory;
    private String guokrStory;

    // 由intent提供数据
    private BeanType beanType;
    private String title;
    private String coverUrl;
    private int id;
    //数据库操作
    private DataBaseHelper dataBaseHelper;

    private SQLiteDatabase sqLiteDatabase;
    private Gson gson;

    @Override
    public String toString() {
        return "DetailPresenter{" +
                "beanType=" + beanType +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }


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
        mView = view;
        mContext = context;
        /*
        为传递过来的view设置presenter PRAGMA journal_mode
         */
        view.setPresenter(this);
        modelImp = new StringModelImp(context);
        sharedPreferences = context.getSharedPreferences("user_settings", Context.MODE_PRIVATE);
        //创建名字是history的数据库
        dataBaseHelper = DataBaseHelper.getInstance(context, "History.db", null, 5);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        gson = new Gson();

    }

    @Override
    public void start() {

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

        //设置封面
        mView.showCover(this.coverUrl);

        //从设置中查看是否设置是否为显示图片模式
        mView.setImageMode(sharedPreferences.getBoolean("image_mode", false));

        Log.i(TAG, ">>>>>>>>>>setImageMode: "+sharedPreferences.getBoolean("image_mode", false));

       /*
          --------------------根据beantype确定加载请求数据的类型-----------------
       */
        switch (beanType) {
            case TYPE_ZHIHU:
                //设置title
                mView.setTitle(this.title);
                if (NetWorkState.NetWorkConnected(mContext)) {
                    //开始请求数据
                    modelImp.load(Api.ZHIHU_NEWS + id, new onStringListener() {
                        @Override
                        public void onError(VolleyError error) {

                        }

                        @Override
                        public void onSuccess(String result) {
                            try {
                                zhiHuDailyStory = gson.fromJson(result, ZhiHuDailyStory.class);
                                if (zhiHuDailyStory.getBody() == null) {
                                    mView.showResultWithoutBody(zhiHuDailyStory.getShare_url());
                                } else {
                                    mView.showResult(convertZhihuContent(zhiHuDailyStory.getBody()));
                                }

                            } catch (JsonSyntaxException e) {
                                mView.showLoadingError();

                            }
                            mView.stopLoading();

                        }
                    });

                } else {
                    /*
                    如果没有网络,从数据库读取数据
                     */
                    Cursor cursor = sqLiteDatabase.query("Zhihu", null, null, null, null, null, null);
                    Gson gson = new Gson();
                    String content = "";
                    //找到数据表，一条一条轮训数据
                    if (cursor.moveToFirst()) {
                        do {
                            //直到找到文章ID对应的项目
                            if (cursor.getInt(cursor.getColumnIndex("zhihu_id")) == id) {
                                //在对应ID的条目中获得指定字段索引的内容，得到的数据是jos格式的
                                content = cursor.getString(cursor.getColumnIndex("zhihu_content"));
                                //解析josn格式的数据，得到数据bean
                                try {
                                    zhiHuDailyStory = gson.fromJson(content, ZhiHuDailyStory.class);
                                } catch (JsonParseException e) {
                                    mView.showLoadingError();
                                }
                                //装换成移动端格式的html文档
                                mView.showResult(convertZhihuContent(zhiHuDailyStory.getBody()));
                            }

                        } while (cursor.moveToNext());

                    }
                    //关闭指针
                    cursor.close();

                }
                break;
            case TYPE_DOUBAN:
                //设置title
                mView.setTitle(this.title);
                if (NetWorkState.NetWorkConnected(mContext)) {
                    //开始请求数据
                    modelImp.load(Api.DOUBAN_ARTICLE_DETAIL + id, new onStringListener() {

                        @Override
                        public void onSuccess(String result) {
                            try {
                                //解析豆瓣文章详情
                                douBanMomentStory = gson.fromJson(result, DouBanMomentStory.class);
                                if (douBanMomentStory.getContent() != null) {

                                    Log.i(TAG, "showResult: "+convertDouBanContent());
                                    mView.showResult(convertDouBanContent());
                                }

                            } catch (JsonParseException e) {
                                mView.showLoadingError();

                            }
                            mView.stopLoading();

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });

                } else {
                    /*
                    数据库读取
                     */
                    Cursor cursor = sqLiteDatabase
                            .rawQuery("select douban_content from Douban where douban_id = " + id, null);
                    Gson gson = new Gson();

                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getCount() == 1) {
                                douBanMomentStory = gson.fromJson(cursor.getString(0), DouBanMomentStory.class);
                                mView.showResult(convertDouBanContent());
                            }

                        } while (cursor.moveToNext());

                    }
                    //关闭指针
                    cursor.close();


                }

                break;
            case TYPE_GOUKR:
                //设置title
                mView.setTitle(this.title);
                if (NetWorkState.NetWorkConnected(mContext)) {

                    modelImp.load(Api.GUOKR_ARTICLE_LINK_V1 + id, new onStringListener() {

                        @Override
                        public void onSuccess(String result) {
                            convertGuokrContent(result);

                            mView.showResult(guokrStory);
                        }

                        @Override
                        public void onError(VolleyError error) {
                            mView.stopLoading();
                            mView.showLoadingError();

                        }
                    });
                } else {
                    /*
                    数据库读取数
                     */
                    Cursor cursor = sqLiteDatabase.query("Guokr", null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getInt(cursor.getColumnIndex("guokr_id")) == id) {
                                String s = cursor.getString(cursor.getColumnIndex("guokr_content"));
                                convertGuokrContent(s);
                                mView.showResult(guokrStory);
                            }


                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                }
                break;
        }
    }

    @Override
    public void openUri(Activity activity, Uri uri) {

        activity.startActivity(
                new Intent(activity, InnerBrowserActivity.class)
                        .putExtra("url", uri.toString()));

    }


    @Override
    public void openUrl(WebView webView, String url) {

        //如果设置需要在内置浏览器中代开link的话
        if (sharedPreferences.getBoolean("inner_browser", false)) {

            CustomTabsIntent tabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(mContext.getResources().getColor(R.color.colorAccent))
                    .setShowTitle(true)
                    .build();
            CustomTabActivityHelper.openCustomTab(
                    (Activity) mContext,
                    tabsIntent,
                    Uri.parse(url), this);

        } else {
            /*
            没有设置的话，用设备安装的浏览器打开
             */
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            } catch (ActivityNotFoundException e) {
                mView.showBrowserNotFindError();
            }

        }

    }

    @Override
    public void openInBrowser() {
        if (CheckNull()) {
            mView.showLoadingError();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (beanType) {
            case TYPE_ZHIHU:
                intent.setData(Uri.parse(zhiHuDailyStory.getShare_url()));
                break;
            case TYPE_GOUKR:
                intent.setData(Uri.parse(Api.GUOKR_ARTICLE_LINK_V1 + id));
                break;
            case TYPE_DOUBAN:
                intent.setData(Uri.parse(douBanMomentStory.getUrl()));

        }
        mContext.startActivity(intent);


    }

    @Override
    public void shareAsText() {
        /*
        title+link+ 分享自 纸飞机
         */
        //如果数据为空,显示分享错误，直接退出
        if (CheckNull()) {
            mView.showShareError();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND).setType("text/html");
        String shareText = "" + title + "";

        switch (beanType) {
            case TYPE_ZHIHU:
                shareText += Api.ZHIHU_DAILY_BASE_URL + id + "\t\t\t" + "分享自  纸飞机";
                break;
            case TYPE_GOUKR:
                shareText += Api.GUOKR_ARTICLE_LINK_V1 + id + "\t\t\t" + "分享自  纸飞机";
                break;
            case TYPE_DOUBAN:
                shareText += Api.DOUBAN_ARTICLE_DETAIL + id + "\t\t\t" + "分享自  纸飞机";
                break;
        }

        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        mContext.startActivity(Intent.createChooser(intent, "分享至"));


    }

    /*
    Returns displayable styled text from the provided HTML string with the legacy flags
     */
    @Override
    public void copyText() {

        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = null;
        switch (beanType) {
            case TYPE_ZHIHU:
                clipData = ClipData.newPlainText("copy",
                        Html.fromHtml(title + "\n" + zhiHuDailyStory.getBody()).toString());
                break;
            case TYPE_GOUKR:
                clipData = ClipData.newPlainText("copy",
                        Html.fromHtml(title + "\n" + guokrStory));

                break;
            case TYPE_DOUBAN:
                clipData = ClipData.newPlainText("copy",
                        Html.fromHtml(title + "\n" + douBanMomentStory.getContent()));
                break;
        }
        clipboardManager.setPrimaryClip(clipData);
        //界面显示复制文成
        mView.showTextCopied();
    }

    @Override
    public void copyLink() {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = null;
        switch (beanType) {
            case TYPE_ZHIHU:
                clipData = ClipData.newPlainText("copy", Api.ZHIHU_DAILY_BASE_URL + id);
                break;
            case TYPE_GOUKR:
                clipData = ClipData.newPlainText("copy", Api.GUOKR_ARTICLE_LINK_V1 + id);
                break;
            case TYPE_DOUBAN:
                clipData = ClipData.newPlainText("copy", Api.DOUBAN_ARTICLE_DETAIL + id);
                break;
        }
        clipboardManager.setPrimaryClip(clipData);
        //显示复制链接成功
        mView.showTextCopied();
    }

    @Override
    public void addToOrDeleteFromBookmarks() {
        String temId = "";
        String temTable = "";
        switch (beanType) {
            case TYPE_ZHIHU:
                temId = "zhihu_id";
                temTable = "Zhihu";
                break;
            case TYPE_DOUBAN:
                temId = "douban_id";
                temTable = "Douban";
                break;
            case TYPE_GOUKR:
                temId = "guokr_id";
                temTable = "Guokr";
                break;

        }
        /*
        需要根据数据的类型来进行数据库的操作
         */
        //首先判断是否已经收藏，需要查询数据库,如果已经收藏则删除，bookmark的值改为0，即更新数据
        if (queryIfIsBookmarked()) {
            sqLiteDatabase.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("bookmark", 0);
            sqLiteDatabase.update(temTable, values, temId + "= ?", new String[]{String.valueOf(id)});
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            values.clear();
            mView.showDeletedFromBookmarks();

        } else {
            //添加到数据库
            sqLiteDatabase.beginTransaction();
            ContentValues values = new ContentValues();
            values.put("bookmark", 1);
            sqLiteDatabase.update(temTable, values, temId + "= ?", new String[]{String.valueOf(id)});
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
            values.clear();
            mView.showAddedToBookmarks();


        }

    }

    @Override
    public boolean queryIfIsBookmarked() {
        if (id == 0 || beanType == null) {
            mView.showLoadingError();
            return false;
        }
        String temId = "";
        String temTable = "";
        switch (beanType) {
            case TYPE_ZHIHU:
                temId = "zhihu_id";
                temTable = "Zhihu";
                break;
            case TYPE_DOUBAN:
                temId = "douban_id";
                temTable = "Douban";
                break;
            case TYPE_GOUKR:
                temId = "guokr_id";
                temTable = "Guokr";
        }
//        String sql = "select * from " + tempTable + " where " + tempId + " = ?";

        String SQL = " select * from " + temTable + "  where " + temId + " = ?";
        //将指针移动到对应ID的列，并且假装有很多行
        Cursor cursor = sqLiteDatabase.rawQuery(SQL, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            do {
                //获取每一行的对应的bookMark值,cursor对应的行有很多类型为int的数据，所以要获取索引为bookmarks的int
                int bookmark = cursor.getInt(cursor.getColumnIndex("bookmark"));
                if (bookmark == 1) {
                    return true;
                }

            } while (cursor.moveToNext());

        }
        cursor.close();
        return false;
    }

    private boolean CheckNull() {
        return ((beanType == BeanType.TYPE_ZHIHU && zhiHuDailyStory == null)
                ||(beanType==BeanType.TYPE_GOUKR&&guokrStory==null)
                ||beanType==BeanType.TYPE_DOUBAN&&douBanMomentStory==null);
    }

    /*
    clause
     */
    /*
    复制链接
     */
    private String convertDouBanContent( ) {

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_light.css\" type=\"text/css\">";

//        if ((mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
//                == Configuration.UI_MODE_NIGHT_YES) {
//            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_dark.css\" type=\"text/css\">";
//        } else {
//            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_light.css\" type=\"text/css\">";
//        }
        String content = douBanMomentStory.getContent();
        ArrayList<DouBanMomentStory.PhotosBean> imageList = douBanMomentStory.getPhotos();
        for (int i = 0; i < imageList.size(); i++) {
            String old = "<img id=\"" + imageList.get(i).getTag_name() + "\" />";
            String newStr = "<img id=\"" + imageList.get(i).getTag_name() + "\" "
                    + "src=\"" + imageList.get(i).getMedium().getUrl() + "\"/>";
            content = content.replace(old, newStr);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        builder.append("<head>\n<meta charset=\"utf-8\" />\n");
        builder.append(css);
        builder.append("\n</head>\n<body>\n");
        builder.append("<div class=\"container bs-docs-container\">\n");
        builder.append("<div class=\"post-container\">\n");
        builder.append(content);
        builder.append("</div>\n</div>\n</body>\n</html>");

        return builder.toString();
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


    private void convertGuokrContent(String content) {
        // 简单粗暴的去掉下载的div部分
        this.guokrStory = content.replace("<div class=\"down\" id=\"down-footer\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"\" class=\"app-down\" id=\"app-down-footer\">下载</a>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"down-pc\" id=\"down-pc\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"http://www.guokr.com/mobile/\" class=\"app-down\">下载</a>\n" +
                "    </div>", "");
//        this.guokrStory = guokrStory.replace("<div class=\"header\">\n" +
//                "        <h1 class=\"header-title\"></h1>\n" +
//                "    </div>", "");

        // 替换css文件为本地文件
        guokrStory = guokrStory.replace("<link rel=\"stylesheet\" href=\"http://static.guokr.com/apps/handpick/styles/d48b771f.article.css\" />",
                "<link rel=\"stylesheet\" href=\"file:///android_asset/guokr.article.css\" />");

        // 替换js文件为本地文件
        guokrStory = guokrStory.replace("<script src=\"http://static.guokr.com/apps/handpick/scripts/9c661fc7.base.js\"></script>",
                "<script src=\"file:///android_asset/guokr.base.js\"></script>");

//        if ((mContext.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
//                == Configuration.UI_MODE_NIGHT_YES) {
//            guokrStory = guokrStory.replace("<div class=\"article\" id=\"contentMain\">",
//                    "<div class=\"article \" id=\"contentMain\" style=\"background-color:#212b30; color:#878787\">");
//        }
    }
}
