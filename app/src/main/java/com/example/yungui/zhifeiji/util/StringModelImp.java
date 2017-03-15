package com.example.yungui.zhifeiji.util;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yungui.zhifeiji.interfaze.onStringListener;

/**
 * Created by yungui on 2017/2/10.
 */
/*

  StringModel的实现类

 */
public class StringModelImp {
    //实现类的构造方法
    private Context context;

    public StringModelImp(Context context) {
        this.context = context;
    }


    //加载数据
    public void load(String url, final onStringListener stringListener) {
        //创建一个字符串请求
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            //请求成功
            @Override
            public void onResponse(String response) {
                //回调,传出数据
                stringListener.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            ///请求失败
            @Override
            public void onErrorResponse(VolleyError error) {
                //回调，传出失败的结果
                stringListener.onError(error);
            }
        });
        //将请求加入列队，开始请求
        VolleySingleton.getVolleySingleton(context).addToRequeStQueue(request);

    }
}
