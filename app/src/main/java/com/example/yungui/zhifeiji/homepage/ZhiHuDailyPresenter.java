package com.example.yungui.zhifeiji.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.VolleyError;
import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;
import com.example.yungui.zhifeiji.db.DataBaseHelper;
import com.example.yungui.zhifeiji.detail.DetailActivity;
import com.example.yungui.zhifeiji.interfaze.onStringListener;
import com.example.yungui.zhifeiji.service.CacheService;
import com.example.yungui.zhifeiji.util.Api;
import com.example.yungui.zhifeiji.util.DateFormatter;
import com.example.yungui.zhifeiji.util.NetWorkState;
import com.example.yungui.zhifeiji.util.StringModelImp;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by yungui on 2017/2/9.
 */

public class ZhiHuDailyPresenter implements ZhiHuDailyContract.Presenter {
    //实体类
    private StringModelImp model;
    private Context mContext;
    private ZhiHuDailyContract.View view;
    private DateFormatter fromatter;
    private Gson gson;
    //数据
    private ArrayList<ZhiHuDailyNews.Question> list = new ArrayList<>();

    public final static String ZHIFEIJI_BRODCAST = "com.example.yungui.zhifeiji.LOACL_BRODCAST";

    private DataBaseHelper db;
    //克读写的数据库
    private SQLiteDatabase sqLiteDatabase;


    //读取应用设置
    private SharedPreferences preferences;

    public ZhiHuDailyPresenter(Context context, ZhiHuDailyContract.View view) {
        this.mContext = context;
        this.view = view;
        model = new StringModelImp(mContext);
        fromatter = new DateFormatter();
        gson = new Gson();
        //创建数据库和数据表
        db = DataBaseHelper.getInstance(context, "History.db", null, 5);
        sqLiteDatabase = db.getWritableDatabase();
        view.setPresenter(this);

    }


    @Override
    public void loadPost(long date, final boolean clearing) {
        //首先是否加载新的数据，显示加载
        if (clearing) {
            view.showLoading();
        }
        //判断网络是否连接,连通证常则获取数据
        if (NetWorkState.NetWorkConnected(mContext)) {

            //获取数据
            model.load(Api.ZHIHU_HISTORY + fromatter.ZhiHuDailyFormat(date), new onStringListener() {
                @Override
                public void onError(VolleyError error) {
                    //回调
                    view.showError();
                    view.stopLoading();
                }

                @Override
                public void onSuccess(String result) {
                    //返回的result数据是json格式的
                    try {
                        //News理封装有 data和Question
                        ZhiHuDailyNews news = gson.fromJson(result, ZhiHuDailyNews.class);
                        //要存储的键值对数据
                        ContentValues values = new ContentValues();
                        //是否应景清除数据
                        if (clearing) {
                            list.clear();
                        }
                        for (ZhiHuDailyNews.Question item : news.getStories()) {

                            list.add(item);
                            //,存储数据，首先判断ID是否应已经存在
                            if (!queryifIDExists(item.getId())) {
                                //开始数据库事务
                                sqLiteDatabase.beginTransaction();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
                                Date date = dateFormat.parse(news.getDate());
                                //保存ID，news，发布时间，具体内容
                                values.put("zhihu_id", item.getId());
                                values.put("zhihu_news", gson.toJson(item));

                                //content使用service来缓存，因为内容比较多，即可存储可能造成线程拥堵
                                values.put("zhihu_content", "");
                                values.put("zhihu_time", date.getTime() / 1000);
                                //向数据库表中插入内容
                                sqLiteDatabase.insert("Zhihu", null, values);
                                //清除键值对内容释放内存
                                values.clear();
                                //提交事务
                                sqLiteDatabase.setTransactionSuccessful();
                                sqLiteDatabase.endTransaction();
                            }

                            //使用service缓存zhihu_content.发送广播通知服务开始缓存
                            Intent intent = new Intent(ZHIFEIJI_BRODCAST);
                            intent.putExtra("type", CacheService.ZHIHU);
                            intent.putExtra("id", item.getId());
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                        //回调
                        view.showResult(list);
                    } catch (JsonSyntaxException e) {
                        view.showError();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    view.stopLoading();
                }
            });
        } else {
            if (clearing) {
                //若果没有网络在从数据库中获数据
                Cursor cursor = sqLiteDatabase.query("ZhiHu", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        ZhiHuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), ZhiHuDailyNews.Question.class);
                        list.add(question);
                    } while (cursor.moveToNext());

                    cursor.close();
                    //停止刷新
                    view.stopLoading();
                    //回调，传递数据
                    view.showResult(list);
                } else {
                    //查不到数据
                    view.showError();
                }
            }

        }


    }

    //查看数据库表中已经含有该条数据
    private boolean queryifIDExists(int id) {
        Cursor cursor = sqLiteDatabase.query("Zhihu", null, null, null, null, null, null);
        //如果数据则查，是否有记录
        if (cursor.moveToFirst()) {
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("zhihu_id"))) {
                    return true;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }

    @Override
    public void refresh() {
        //更新数据，将原来的数据清除，显示最新数据
        loadPost(Calendar.getInstance().getTimeInMillis(), true);


    }

    @Override
    public void loadMore(long date) {
        //不清楚数据
        loadPost(date, false);


    }

    @Override
    public void readDetail(int position) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("id", list.get(position).getId());
        intent.putExtra("title", list.get(position).getTitle());
        intent.putExtra("coverUrl", list.get(position).getImages().get(0));
        intent.putExtra("beanType", BeanType.TYPE_ZHIHU);
        mContext.startActivity(intent);

    }

    @Override
    public void lookLook() {
        /*
        从数据集合list中随机选取一个
         */

        if (list.isEmpty()) {

            view.showError();
            return;
        }

        readDetail(new Random().nextInt(list.size()));


    }

    @Override
    public void start() {
        loadPost(Calendar.getInstance().getTimeInMillis(), true);


    }

}
