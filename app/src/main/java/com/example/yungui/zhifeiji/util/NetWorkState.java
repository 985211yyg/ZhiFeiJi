package com.example.yungui.zhifeiji.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yungui on 2017/2/10.
 * 判断网络连接状态
 * WiFi，移动网络，定位
 */

public class NetWorkState {
    private static NetworkInfo getNetWorkInfo(Context context) {
        if (context != null) {
            //获取连接管理器
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取连接信息
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info;
        }
        return null;
    }

    //检查网络是否可用
    public static boolean NetWorkConnected(Context context) {
        NetworkInfo info = getNetWorkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    //检查WiFi是否连接
    public static boolean WifiConnected(Context context) {
        NetworkInfo info = getNetWorkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }

        return false;
    }

    //检查移动网络是否连接
    public static boolean MobileDataConnected(Context context) {
        NetworkInfo info = getNetWorkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

}
