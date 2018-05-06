package com.example.yungui.zhifeiji.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by yungui on 2017/2/9.
 */

public class VolleySingleton {
    //单例模式
    private static VolleySingleton volleySingleton;
    //创建请求列队
    private RequestQueue requestQueue;

    //私有的构造方法
    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    //单例
    public static synchronized VolleySingleton getVolleySingleton(Context context) {

        if (volleySingleton == null) {
            volleySingleton = new VolleySingleton(context);

        }
        return volleySingleton;
    }
    //返回请求列队
    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }
//     /将请求加入请求列队中
    public <T> void addToRequeStQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

}
