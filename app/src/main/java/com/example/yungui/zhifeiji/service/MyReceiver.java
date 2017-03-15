package com.example.yungui.zhifeiji.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    private CacheService cacheService = new CacheService();

    @Override
    public void onReceive(Context context, Intent intent) {
//        cacheService.startCache(intent.getIntExtra("type", -1),intent.getIntExtra("id", 0));
//        Log.i("TAG", "------->>>>>>>>>>>>onReceive: "+intent.getIntExtra("type", -1)+intent.getIntExtra("id", 0));

    }
}
