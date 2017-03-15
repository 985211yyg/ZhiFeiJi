package com.example.yungui.zhifeiji;

/**
 * Created by yungui on 2017/2/9.
 */
  /*

     连接View和model，处理逻辑任务
   */
public interface BasePresenter {
    //抽象类 用于获取数据改变view，启动时间是fragment的onResume(),
    void start();
}
