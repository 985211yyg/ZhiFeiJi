package com.example.yungui.zhifeiji.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yungui on 2017/3/10.
 */




/*
字段	          类型	     含义	            备注
id	          integer	主键	                自增长
zhihu_id	  integer	知乎日报消息id	     由知乎提供
zhihu_news	   text	   知乎日报消息内容	    与Java实体类对应
zhihu_time	   real	    知乎日报消息发布的时间	  由知乎提供
zhihu_content	text	 知乎日报消息详细内容	  与Java实体类对应
bookmark	   integer	    是否被收藏	     由于SQLite并没有boolean类型，使用integer的不同值代替
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private String SQLString = "create table if not exists Zhihu(id integer primary key autoincrement," +
            "zhihu_id integer not null," +
            "zhihu_news text," +
            "zhihu_time real, " +
            "zhihu_content text)";

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //执行创建表的操作
        sqLiteDatabase.execSQL(SQLString);
        //插入书签标记行
        sqLiteDatabase.execSQL("alter table ZhiHu add column bookmark integer default 0");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
