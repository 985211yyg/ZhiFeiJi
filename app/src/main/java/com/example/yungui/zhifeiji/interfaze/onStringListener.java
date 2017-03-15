package com.example.yungui.zhifeiji.interfaze;

import com.android.volley.VolleyError;

/**
 * Created by yungui on 2017/2/10.
 */


/*
请求回调的接口，传递请求结果
 */
public interface onStringListener {
    //获取消息字符创失败
    void onError(VolleyError error);


    //请求成功 ，
    void onSuccess(String result);
}
