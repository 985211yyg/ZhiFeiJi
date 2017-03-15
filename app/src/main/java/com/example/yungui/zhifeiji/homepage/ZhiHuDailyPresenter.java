package com.example.yungui.zhifeiji.homepage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.bean.ZhuHuDailyNews;
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

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

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
    private ArrayList<ZhuHuDailyNews.Question> list = new ArrayList<>();

    private final static String ZHIHUIDAILY_BRODCAST = "com.example.yungui.zhihudaily.LOACL_BRODCAST";

    private DataBaseHelper db;
    //克读写的数据库
    private SQLiteDatabase sqLiteDatabase;

    public ZhiHuDailyPresenter(Context context, ZhiHuDailyContract.View view) {
        this.mContext = context;
        this.view = view;
        model = new StringModelImp(mContext);
        fromatter = new DateFormatter();
        gson = new Gson();
        //创建数据库和数据表
        db = new DataBaseHelper(context, "History.db", null, 5);
        sqLiteDatabase = db.getWritableDatabase();
        view.setPresenter(this);

    }

    //    ZhuHuDailyNews{data
//                     ;Stories}
    @Override
    public void loadPost(long date, final boolean clearing) {
        //首先是否加载新的数据，显示加载
        if (clearing) {
            view.showLoading();
        }
        //判断网络是否连接,连通证常则获取数据
        if (NetWorkState.NetWorkConnected(mContext)) {
            Log.i(TAG, "--------->>>>>>>>>>>NetWorkConnected:"+true);

            //获取数据
            model.load(Api.ZHIHU_HISTORY + fromatter.ZhiHuDailyFormat(date), new onStringListener() {
                @Override
                public void onError(VolleyError error) {
                    Log.i(TAG, ">>>>>>onError: " + error);
                    //回调
                    view.showError();
                    view.stopLoading();
                }

                @Override
                public void onSuccess(String result) {
                    //返回的result数据是json格式的
                    try {
                        //News理封装有 data和Question
                        Log.i(TAG, ">>>>>>>>>onSuccess: " + result);

                        ZhuHuDailyNews news = gson.fromJson(result, ZhuHuDailyNews.class);
                        //要存储的键值对数据
                        ContentValues values = new ContentValues();
                        //是否应景清除数据
                        if (clearing) {
                            list.clear();
                        }
                        for (ZhuHuDailyNews.Question item : news.getStories()) {

                            Log.i(TAG, ">>>>>>>>>newsItem: " + item.toString());
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
                            Intent intent = new Intent(ZHIHUIDAILY_BRODCAST);
                            intent.putExtra("type", CacheService.ZHIHU);
                            intent.putExtra("id", item.getId());
                            Log.i(TAG, "------>>>>>>itemID:"+item.getId());
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
                Log.i(TAG, "--------->>>>>>>>>>>NetWorkConnected:"+false);
                Cursor cursor = sqLiteDatabase.query("ZhiHu", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        ZhuHuDailyNews.Question question = gson.fromJson(cursor.getString(cursor.getColumnIndex("zhihu_news")), ZhuHuDailyNews.Question.class);
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

    }

    @Override
    public void loadMore(long date) {

    }

    @Override
    public void readDetail(int position) {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra("id",list.get(position).getId() );
        intent.putExtra("title", list.get(position).getTitle());
        intent.putExtra("coverUrl", list.get(position).getImages().get(0));
        intent.putExtra("beanType", BeanType.TYPE_ZHIHU);
        mContext.startActivity(intent);

    }

    @Override
    public void lookLook() {

    }

    @Override
    public void start() {
        loadPost(Calendar.getInstance().getTimeInMillis(), true);


    }

}
