package com.example.yungui.zhifeiji.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;
import com.example.yungui.zhifeiji.db.DataBaseHelper;
import com.example.yungui.zhifeiji.detail.DetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Random;

import static com.example.yungui.zhifeiji.adapter.BookMarkAdapter.TYPE_DouBanHeader;
import static com.example.yungui.zhifeiji.adapter.BookMarkAdapter.TYPE_DouBanItem;
import static com.example.yungui.zhifeiji.adapter.BookMarkAdapter.TYPE_GuoKrHeader;
import static com.example.yungui.zhifeiji.adapter.BookMarkAdapter.TYPE_GuoKrItem;
import static com.example.yungui.zhifeiji.adapter.BookMarkAdapter.TYPE_ZhiHuiHeader;
import static com.example.yungui.zhifeiji.adapter.BookMarkAdapter.TYPE_ZhiHuiItem;

/**
 * Created by yungui on 2017/2/13.
 */

public class BookMarkPresenter implements BookFragmentContract.Presenter {

    private Context mContext;
    private BookMarkFragment view;

    private DataBaseHelper dataBaseHelper;
    private SQLiteDatabase database;

    private ArrayList<ZhiHuDailyNews.Question> zhihuList = new ArrayList<>();
    private ArrayList<GuoKrStory.ResultBean> guokrList = new ArrayList<>();
    private ArrayList<DouBanMomentNews.PostsBean> doubanList = new ArrayList<>();
    private ArrayList<Integer> itemType = new ArrayList<>();

    private Gson gson;
    public static final String TAG = BookMarkPresenter.class.getSimpleName();


    //构造方法
    public BookMarkPresenter(Context context, BookFragmentContract.View view) {
        mContext = context;
        this.view = (BookMarkFragment) view;
        view.setPresenter(this);
        dataBaseHelper = DataBaseHelper.getInstance(context, "History.db", null, 5);
        database = dataBaseHelper.getWritableDatabase();
        gson = new Gson();

    }

    @Override
    public void loadPost(boolean refresh) {
        Log.e(TAG, ">>>>>>>loadPost!!");
        if (!refresh) {
            view.showLoading();
        } else {
            zhihuList.clear();
            guokrList.clear();
            doubanList.clear();
            itemType.clear();
        }

        //----------------------------查询知乎的数据表--------------------------
//        itemType.size()=1
        itemType.add(TYPE_ZhiHuiHeader);
        //  itemType.size()+zhihuList.size()
        Cursor cursor = database.query("Zhihu", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getInt(cursor.getColumnIndex("bookmark")) == 1) {
                    String s = cursor.getString(cursor.getColumnIndex("zhihu_news"));
                    ZhiHuDailyNews.Question q = gson.fromJson(s, ZhiHuDailyNews.Question.class);
                    if (q != null) {
                        Log.i(TAG, "------>>>>loadPost: " + q);
                        zhihuList.add(q);
                        itemType.add(TYPE_ZhiHuiItem);
                    }

                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        //----------------------------查询果壳的数据表----------------------------
        itemType.add(TYPE_GuoKrHeader);
        Cursor cursor1 = database.query("Guokr", null, null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            do {
                if (cursor1.getInt(cursor1.getColumnIndex("bookmark")) == 1) {
                    String s = cursor1.getString(cursor1.getColumnIndex("guokr_news"));
                    GuoKrStory.ResultBean r = gson.fromJson(s, GuoKrStory.ResultBean.class);

                    Log.i(TAG, "------>>>>loadPost: "+r);
                    guokrList.add(r);
                    itemType.add(TYPE_GuoKrItem);

                }
            } while (cursor1.moveToNext());
        }
        cursor1.close();


        //----------------------------c查询豆瓣的数据表-----------------------------
        itemType.add(TYPE_DouBanHeader);
        Cursor cursor2 = database.query("Douban", null, null, null, null, null, null);
        if (cursor2.moveToFirst()) {
            do {
                if (cursor2.getInt(cursor2.getColumnIndex("bookmark")) == 1) {
                    String s = cursor2.getString(cursor2.getColumnIndex("douban_news"));
                    DouBanMomentNews.PostsBean p = gson.fromJson(s, DouBanMomentNews.PostsBean.class);
                        Log.i(TAG, "------>>>>loadPost: "+p);

                        doubanList.add(p);
                        itemType.add(TYPE_DouBanItem);
                }
            } while (cursor2.moveToNext());
        }
        cursor2.close();


        //显示结果
        view.showResult(zhihuList, guokrList, doubanList, itemType);
        view.stopLoading();


    }

    @Override
    public void refresh() {
        //刷新
        loadPost(true);

    }


    @Override
    public void readDetail(int position) {
        int itemClickType = itemType.get(position);
        int id = -1;
        String title = "";
        String coverUrl = "";
        Intent intent;
        switch (itemClickType) {
            case TYPE_DouBanHeader:
                break;
            case TYPE_GuoKrHeader:
                break;
            case TYPE_ZhiHuiHeader:
                break;

            case TYPE_ZhiHuiItem:
                if (!zhihuList.isEmpty()) {

                    id = zhihuList.get(position - 1).getId();
                    title = zhihuList.get(position - 1).getTitle();
                    if (zhihuList.get(position - 1).getImages().get(0) != null) {
                        coverUrl = zhihuList.get(position - 1).getImages().get(0);
                    } else {
                        coverUrl = "";
                    }
                    intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("title", title);
                    intent.putExtra("coverUrl", coverUrl);
                    intent.putExtra("beanType", BeanType.TYPE_ZHIHU);
                    mContext.startActivity(intent);

                }
                break;
            case TYPE_GuoKrItem:
                if (!guokrList.isEmpty()) {

                    id = guokrList.get(position - zhihuList.size() - 2).getId();
                    title = guokrList.get(position - zhihuList.size() - 2).getTitle();
                    if (guokrList.get(position - zhihuList.size() - 2).getImages().size() != 0) {

                        coverUrl = guokrList.get(position - zhihuList.size() - 2).getImages().get(0);
                    } else {
                        coverUrl = "";
                    }
                    intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("title", title);
                    intent.putExtra("coverUrl", coverUrl);
                    intent.putExtra("beanType", BeanType.TYPE_GOUKR);
                    mContext.startActivity(intent);
                    break;
                }

            case TYPE_DouBanItem:
                if (!doubanList.isEmpty()) {

                    id = doubanList.get(position - zhihuList.size() - guokrList.size() - 3).getId();
                    title = doubanList.get(position - zhihuList.size() - guokrList.size() - 3).getTitle();
                    if (doubanList.get(position - zhihuList.size() - guokrList.size() - 3).getThumbs().size() != 0) {
                        coverUrl = doubanList.get(position - zhihuList.size() - guokrList.size() - 3).getThumbs().get(0).getMedium().getUrl();
                    } else {
                        coverUrl = "";
                    }
                    intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("title", title);
                    intent.putExtra("coverUrl", coverUrl);
                    intent.putExtra("beanType", BeanType.TYPE_DOUBAN);
                    mContext.startActivity(intent);
                }

                break;
        }

    }

    @Override
    public void lookLook() {
        int random = new Random().nextInt(3);
        Intent intent = new Intent(mContext, DetailActivity.class);
        int item;

        switch (random) {
            case 0:
                if (!zhihuList.isEmpty()) {
                    item = new Random().nextInt(zhihuList.size());
                    intent.putExtra("id", zhihuList.get(item).getId());
                    intent.putExtra("title", zhihuList.get(item).getTitle());
                    intent.putExtra("beanType", BeanType.TYPE_ZHIHU);

                    if (zhihuList.get(item).getImages().size() != 0) {
                        intent.putExtra("coverUrl", zhihuList.get(item).getImages().get(0));
                    } else {
                        intent.putExtra("coverUrl", "");
                    }
                    mContext.startActivity(intent);

                }
                break;
            case 1:
                if (!guokrList.isEmpty()) {
                    item = new Random().nextInt(guokrList.size());
                    intent.putExtra("id", zhihuList.get(item).getId());
                    intent.putExtra("title", zhihuList.get(item).getTitle());
                    intent.putExtra("beanType", BeanType.TYPE_GOUKR);

                    if (zhihuList.get(item).getImages().size() != 0) {
                        intent.putExtra("coverUrl", zhihuList.get(item).getImages().get(0));
                    } else {
                        intent.putExtra("coverUrl", "");
                    }
                    mContext.startActivity(intent);


                }
                break;
            case 2:
                if (!doubanList.isEmpty()) {
                    item = new Random().nextInt(doubanList.size());
                    intent.putExtra("id", doubanList.get(item).getId());
                    intent.putExtra("title", doubanList.get(item).getTitle());
                    intent.putExtra("beanType", BeanType.TYPE_DOUBAN);
                    if (doubanList.get(item).getThumbs().size() != 0) {
                        intent.putExtra("coverUrl", doubanList.get(item).getThumbs().get(0).getMedium().getUrl());
                    } else {
                        intent.putExtra("coverUrl", "");
                    }
                    mContext.startActivity(intent);

                }
                break;
        }


    }

    @Override
    public void search(String searchWorks, boolean clearing) {
        Log.i(TAG, "------->>>>>>>search: " + searchWorks);
        //清除原来的数据,来装载新的查询数据
        zhihuList.clear();
        guokrList.clear();
        doubanList.clear();
        itemType.clear();

        //----------------------------查询知乎的数据表--------------------------
//        itemType.size()=1
        itemType.add(TYPE_ZhiHuiHeader);
        Cursor cursor = database.rawQuery(" select * from Zhihu where bookmark = ? and zhihu_news like ? ", new String[]{"1", "%" + searchWorks + "%"});
        Log.i(TAG, "-------search: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(cursor.getColumnIndex("zhihu_news"));
                ZhiHuDailyNews.Question q = gson.fromJson(s, ZhiHuDailyNews.Question.class);
                Log.i(TAG, "-------search: " + q);
                zhihuList.add(q);
                itemType.add(TYPE_ZhiHuiItem);
            } while (cursor.moveToNext());
        }
        cursor.close();

        //----------------------------查询果壳的数据表----------------------------
        itemType.add(TYPE_GuoKrHeader);
        Cursor cursor1 = database.rawQuery(" select * from Guokr where bookmark = ? and guokr_news like ? ", new String[]{"1", "%" + searchWorks + "%"});
        if (cursor1.moveToFirst()) {
            do {
                String s = cursor1.getString(cursor1.getColumnIndex("guokr_news"));
                GuoKrStory.ResultBean r = gson.fromJson(s, GuoKrStory.ResultBean.class);
                if (r != null) {
                    guokrList.add(r);
                    itemType.add(TYPE_GuoKrItem);
                }

            } while (cursor1.moveToNext());
        }
        cursor1.close();


        //----------------------------c查询豆瓣的数据表-----------------------------
        itemType.add(TYPE_DouBanHeader);
        Cursor cursor2 = database.rawQuery(" select * from Douban where bookmark = ? and douban_news like ? ", new String[]{"1", "%" + searchWorks + "%"});

        if (cursor2.moveToFirst()) {
            do {
                String s = cursor2.getString(cursor2.getColumnIndex("douban_news"));
                DouBanMomentNews.PostsBean p = gson.fromJson(s, DouBanMomentNews.PostsBean.class);
                if (p != null) {

                    doubanList.add(p);
                    itemType.add(TYPE_DouBanItem);
                }
            } while (cursor2.moveToNext());
        }
        cursor2.close();


        //显示结果
        view.showResult(zhihuList, guokrList, doubanList, itemType);
        view.stopLoading();


    }

    @Override
    public void start() {
        Log.e(TAG, ">>>>>>>>start: ");
        loadPost(false);

    }
}
