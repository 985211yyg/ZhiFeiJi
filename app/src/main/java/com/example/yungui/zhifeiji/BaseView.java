package com.example.yungui.zhifeiji;

import android.view.View;

/**
 * Created by yungui on 2017/2/9.
 */
/*
  所有view的基础类
 */
public interface BaseView<T> {
    //为view设置presenter
    void setPresenter(T presenter);
    //初始化view
    void initViews(View view);


}
