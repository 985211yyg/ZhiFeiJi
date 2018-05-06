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
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.db.DataBaseHelper;
import com.example.yungui.zhifeiji.detail.DetailActivity;
import com.example.yungui.zhifeiji.interfaze.onStringListener;
import com.example.yungui.zhifeiji.service.CacheService;
import com.example.yungui.zhifeiji.util.Api;
import com.example.yungui.zhifeiji.util.NetWorkState;
import com.example.yungui.zhifeiji.util.StringModelImp;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yungui on 2017/2/14.
 */

public class GuoKrPresenter implements GuoKrContract.Presenter {

    private Context context;
    private GuoKrContract.View view;
    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase database;
    private StringModelImp modelImp;
    private Gson gson;

    private int loadMoreCounts = 15;

    public static final String GuoKr_Base_Url = "http://apis.guokr.com/handpick/article.json?retrieve_type=by_since&category=all&limit=";
    public static final String GuoKr_Base_Url_link = "&ad=1";

    //是否清除数据
    private boolean isLoadMore = false;

    private GuoKrStory guoKrStory;

    private ArrayList<GuoKrStory.ResultBean> resultBeans = new ArrayList<>();

    public static final String TAG = GuoKrPresenter.class.getSimpleName();


    public GuoKrPresenter(Context context, GuoKrContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        dataBaseHelper = DataBaseHelper.getInstance(context, "History.db", null, 5);
        database = dataBaseHelper.getWritableDatabase();
        modelImp = new StringModelImp(context);
        gson = new Gson();

    }

    /*
    果壳的刷新与日期无关，所以直接刷新就好
     */
    @Override
    public void loadPost(int counts) {
        if (!isLoadMore) {
            view.showLoading();
        }
        isLoadMore = false;
        //如果网路连接正常，从网络去获取数据，并存储到数据库中
        if (NetWorkState.NetWorkConnected(context)) {

            Log.i(TAG, ">>>>>>>>>loadPost: " + GuoKr_Base_Url + counts + GuoKr_Base_Url_link);


            modelImp.load(GuoKr_Base_Url + counts + GuoKr_Base_Url_link, new onStringListener() {
                @Override
                public void onSuccess(String result) {
                    Log.i(TAG, ">>>>>>>>>loadPost: " +result);
                    try {
                        guoKrStory = gson.fromJson(result, GuoKrStory.class);
                        ContentValues values = new ContentValues();

                        resultBeans.clear();

                        for (GuoKrStory.ResultBean bean : guoKrStory.getResult()) {
                            resultBeans.add(bean);

                            //存储数据之前查询是否已经存在于数据库中 !queryIfIdExists(bean.getId())
                            if (!queryifIDExists(bean.getId())) {
                                //如果不存在，存储
                                database.beginTransaction();
                                values.put("guokr_id", bean.getId());
                                //直接以josn的形式存储bean
                                values.put("guokr_news", gson.toJson(bean));
                                values.put("guokr_time", bean.getDate_created());
                                //传入空值在service中缓存
                                values.put("guokr_content", "");
                                database.insert("Guokr", null, values);
                                values.clear();
                                database.setTransactionSuccessful();
                                database.endTransaction();
                            }

                            //以广播的形式开启服务缓存
                            Intent intent = new Intent(ZhiHuDailyPresenter.ZHIFEIJI_BRODCAST);
                            intent.putExtra("type", CacheService.GUOKR);
                            intent.putExtra("id", bean.getId());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                        }

                        view.showResult(resultBeans);
                        view.stopLoading();
                    } catch (JsonParseException j) {

                    }


                }

                @Override
                public void onError(VolleyError error) {
                    view.stopLoading();
                    view.showError();

                }
            });

        } else {
            //数据库加载数据
            //从数据库中加载数据

            //若果没有网络在从数据库中获数据
            Cursor cursor = database.query("Guokr", null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    GuoKrStory.ResultBean bean = gson.fromJson(cursor.getString(cursor.getColumnIndex("guokr_news")), GuoKrStory.ResultBean.class);
                    resultBeans.add(bean);
                } while (cursor.moveToNext());

                cursor.close();
                //停止刷新
                view.stopLoading();
                //回调，传递数据
                view.showResult(resultBeans);
            } else {
                //查不到数据
                view.showError();
            }
        }


    }

    //查看数据库表中已经含有该条数据
    private boolean queryifIDExists(int id) {
        Cursor cursor = database.query("Guokr", null, null, null, null, null, null);
        //如果数据则查，是否有记录
        if (cursor.moveToFirst()) {
            do {
                if (id == cursor.getInt(cursor.getColumnIndex("guokr_id"))) {
                    return true;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return false;
    }


    @Override
    public void refresh() {
        loadPost(loadMoreCounts);

    }

    @Override
    public void loadMore(int counts) {
        isLoadMore = true;
        loadMoreCounts += 5;

        loadPost(loadMoreCounts);

    }


    @Override
    public void readDetail(int position) {

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("id", resultBeans.get(position).getId());
        intent.putExtra("title", resultBeans.get(position).getTitle());
        intent.putExtra("beanType", BeanType.TYPE_GOUKR);
        if (resultBeans.get(position).getImages().size() != 0) {
            intent.putExtra("coverUrl", resultBeans.get(position).getImages().get(0));
        } else {
            intent.putExtra("coverUrl", "");
        }
        context.startActivity(intent);


    }

    @Override
    public void lookLook() {

        if (resultBeans.isEmpty()) {
            view.showError();
            return;
        }
        readDetail(new Random().nextInt(resultBeans.size()));
    }

    @Override
    public void start() {
        loadPost(loadMoreCounts);

    }
}
