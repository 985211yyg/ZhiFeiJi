package com.example.yungui.zhifeiji.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.db.DataBaseHelper;
import com.example.yungui.zhifeiji.detail.DetailActivity;
import com.example.yungui.zhifeiji.interfaze.onStringListener;
import com.example.yungui.zhifeiji.service.CacheService;
import com.example.yungui.zhifeiji.util.Api;
import com.example.yungui.zhifeiji.util.DateFormatter;
import com.example.yungui.zhifeiji.util.NetWorkState;
import com.example.yungui.zhifeiji.util.StringModelImp;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by yungui on 2017/2/14.
 */

public class DouBanMomentPresenter implements DouBanMomentContract.Presenter {
    private Context context;
    private DouBanMomentContract.View view;
    //是否清除缓存数据的标识
    //获取数据的实现类
    private StringModelImp modelImp;
    private DataBaseHelper dbHelper;
    private SQLiteDatabase database;
    /*
    ------s数据集合-----------------
     */
    private ArrayList<DouBanMomentNews.PostsBean> postsBean;

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public static final String DOUBANMOMENT_BROCAST = "com.example.yungui.doubanmoment.LOACL_BRODCAST";
    public static final String TAG = DouBanMomentPresenter.class.getSimpleName();


    public DouBanMomentPresenter(Context context, DouBanMomentContract.View view) {
        this.context = context;
        this.view = view;
        //开启事务
        view.setPresenter(this);
        modelImp = new StringModelImp(context);
        dbHelper = DataBaseHelper.getInstance(context, "History.db", null, 5);
        database = dbHelper.getWritableDatabase();
        sharedPreferences = context.getSharedPreferences("user_setting", Context.MODE_PRIVATE);
        gson = new Gson();
        postsBean = new ArrayList<>();


    }

    @Override
    public void loadPost(final long date, final boolean clearing) {

        final DateFormatter dateFormatter = new DateFormatter();
        //首先是否加载新的数据
        if (clearing) {
            view.showLoading();
        }
        //首先判断网络是否正常
        if (NetWorkState.NetWorkConnected(context)) {
            //请求URL=基础url加上日期,请求成功的带着数据回调的接口
            modelImp.load(Api.DOUBAN_MOMENT + dateFormatter.DouBanFormat(date), new onStringListener() {
                @Override
                public void onSuccess(String result) {
                    try {
                        //解析数据
                        DouBanMomentNews douBanMomentNews = gson.fromJson(result, DouBanMomentNews.class);
                        //存储的键值对
                        ContentValues contentValues = new ContentValues();
                        if (clearing) {

                            postsBean.clear();

                        }

                        for (DouBanMomentNews.PostsBean bean : douBanMomentNews.getPosts()) {
                            //存储数据的键值对
                            postsBean.add(bean);
                            //获取数据并存储
                            //c存储之齐纳判断是否已经存储过
                            if (!queryifIDExists(bean.getId())) {

                                //开始事务
                                database.beginTransaction();
                                SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
                                Date date = null;
                                date = format.parse(bean.getDate());
                                contentValues.put("douban_id", bean.getId());
                                contentValues.put("douban_news", gson.toJson(bean));
                                contentValues.put("douban_time", date.getTime() / 1000);
                                contentValues.put("douban_content", "");

                                database.insert("Douban", null, contentValues);
                                //事物成功
                                contentValues.clear();
                                database.setTransactionSuccessful();
                                database.endTransaction();

                                //开启线程缓存content
                                Intent intent = new Intent(ZhiHuDailyPresenter.ZHIFEIJI_BRODCAST);
                                intent.putExtra("type", CacheService.DOUBAN);
                                intent.putExtra("id", bean.getId());
                                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                            }
                        }
                        //显示数据
                        view.showResult(postsBean);

                    } catch (ParseException e) {
                        e.printStackTrace();

                        view.showError();
                    }


                }

                @Override
                public void onError(VolleyError error) {
                    view.stopLoading();
                    view.showError();

                }
            });


        } else {
            //从数据库中加载数据
            if (clearing) {
                //若果没有网络在从数据库中获数据
                Cursor cursor = database.query("Douban", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        DouBanMomentNews.PostsBean posts = gson.fromJson(cursor.getString(cursor.getColumnIndex("douban_news")), DouBanMomentNews.PostsBean.class);
                        postsBean.add(posts);
                    } while (cursor.moveToNext());

                    cursor.close();
                    //停止刷新
                    view.stopLoading();
                    //回调，传递数据
                    view.showResult(postsBean);
                } else {
                    //查不到数据
                    view.showError();
                }
            }
        }

    }

    //查看数据库表中已经含有该条数据
    private boolean queryifIDExists(int id) {
        Log.i(TAG, "queryifIDExists: ");

        Cursor cursor = database.query("Douban", null, null, null, null, null, null);
        //如果数据则查，是否有记录

        if (cursor.moveToFirst()) {
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("douban_id"))) {
                    return true;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    @Override
    public void refresh() {
        loadPost(Calendar.getInstance().getTimeInMillis(), true);

    }

    @Override
    public void loadMore(long date) {
        loadPost(date,false);

    }

    @Override
    public void readDetail(int position) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", postsBean.get(position).getId());
        intent.putExtra("title", postsBean.get(position).getTitle());
        if (postsBean.get(position).getThumbs().size() == 0) {
            intent.putExtra("coverUrl", "");
        } else {
            intent.putExtra("coverUrl", postsBean.get(position).getThumbs().get(0).getMedium().getUrl());
        }
        intent.putExtra("beanType", BeanType.TYPE_DOUBAN);
        context.startActivity(intent);

    }

    @Override
    public void lookLook() {

        if (postsBean.isEmpty()) {
            view.showError();
            return;
        }
        readDetail(new Random().nextInt(postsBean.size()));


    }

    @Override
    public void start() {
        //呼获取一个当前的时间
        loadPost(Calendar.getInstance().getTimeInMillis(), true);

    }


}
