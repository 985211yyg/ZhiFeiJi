package com.example.yungui.zhifeiji.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yungui.zhifeiji.bean.ZhiHuDailyStory;
import com.example.yungui.zhifeiji.db.DataBaseHelper;
import com.example.yungui.zhifeiji.util.Api;
import com.example.yungui.zhifeiji.util.VolleySingleton;
import com.google.gson.Gson;

public class CacheService extends Service {
    public final static int ZHIHU = 0x01;
    public final static int DOUBAN = 0x02;
    public final static int GUOKR = 0x03;
    public static final String TAG = "CacheService";
    //广播接受
    private MyReceiver myReceiver;
    //数据库操作对象
    private DataBaseHelper dbHelper;
    //可写入的数据库
    private SQLiteDatabase database;


    public CacheService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "----------》》》》》》》》》》》onCreate: "+CacheService.class.getName());
        dbHelper = new DataBaseHelper(this, "History.db", null, 5);
        database = dbHelper.getWritableDatabase();


        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.yungui.zhihudaily.LOACL_BRODCAST");
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(new MyReceiver(),filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭网络请求
        VolleySingleton.getVolleySingleton(CacheService.this).getRequestQueue().cancelAll(TAG);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    /**
     * 网络请求id对应的知乎日报的内容主体
     *
     * @param id   所要获取的知乎日报消息内容对应的id
     * @param type 当type为0时，存储body中的数据
     *             当type为1时，再次请求share url中的内容并储存
     */
    private void startCache(String type, final int id) {
        Cursor cursor = database.query(type, null, null, null, null, null, null);
        Log.i(TAG, "---------->>>>>>>>>cursor: "+cursor.getColumnCount()+type+id);
        //如果有数据
        if (cursor.moveToFirst()) {
            Log.i(TAG, "------->>>>cursor.moveToFirst(): ");
            do {
                //如果数据库表存在story的ID并且主要内容为空的话
                if (cursor.getInt(cursor.getColumnIndex("zhihu_id")) == id
                        && cursor.getString(cursor.getColumnIndex("zhihu_content")).equals("")) {

                    Log.i(TAG, "--------->>>>>>start+StringRequest: ");
                    //创建新的请求
                    StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, Api.ZHIHU_NEWS + id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
//                                    Log.i(TAG, "---------->>>>>response" + response);
                                    //解析获的数据
                                    Gson gson = new Gson();
                                    final ZhiHuDailyStory story=gson.fromJson(response, ZhiHuDailyStory.class);
                                    Log.i(TAG, "------》》》》》》》》ZhiHuDailyStory："+story.toString());

                                    /*
                                    获取story的类型，如果是1则需再次请求内容
                                     */
                                    if (story.getType() == 1) {
                                        StringRequest requset = new StringRequest(Request.Method.GET, story.getShare_url()
                                                , new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                //存储内容
                                                ContentValues value = new ContentValues();
                                                value.put("zhihu_content", response);
                                                //更新数据表,将ID的值转换为string型，然后替换“zhihu_id=?"中的位置，即要更新的数据目标
                                                database.update("Zhihu", value, "zhihu_id = ?", new String[]{String.valueOf(id)});
                                                value.clear();
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        });
                                        //用于标识网络请求
                                        requset.setTag(TAG);
                                        //将request加入请求列队中
                                        VolleySingleton.getVolleySingleton(CacheService.this).addToRequeStQueue(requset);

                                    } else {
                                        /*
                                        如果不是分享类的数据，则直接更新数据
                                         */
                                        ContentValues value = new ContentValues();
                                        value.put("zhihu_content", response);
                                        //更新数据表
                                        database.update("zhihu", value, "zhihu_id = ?", new String[]{String.valueOf(id)});
                                        //清空values
                                        value.clear();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i(TAG, "------->>>>>onErrorResponse: " + error);
                        }
                    });
                    //用于标识网络请求
                    request.setTag(TAG);
                    //将请求加入列队中去
                    VolleySingleton.getVolleySingleton(CacheService.this).addToRequeStQueue(request);


                }
            } while (cursor.moveToNext());
        }
        cursor.close();



    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "------>>>>>>onReceive: ");
            int type = intent.getIntExtra("type", -1);
            int id = intent.getIntExtra("id", 0);
            switch (type) {
                case CacheService.ZHIHU:
                    Log.i(TAG, "------->>>>>>startCache: "+"知乎知乎知乎");
                    startCache("Zhihu", id);
                    break;
                case CacheService.DOUBAN:
                    startCache("Douban", id);
                    break;
                case CacheService.GUOKR:
                    startCache("Guokr", id);
                    break;
                case -1:
                    break;
            }
        }
    }


}
