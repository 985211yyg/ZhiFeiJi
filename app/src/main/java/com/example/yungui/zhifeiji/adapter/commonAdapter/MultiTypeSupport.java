package com.example.yungui.zhifeiji.adapter.commonAdapter;

/**
 * Created by yungui on 2017/2/14.
 */
   /*
   多布局支持接口
    */
public interface MultiTypeSupport<T> {
    //根据当前的位置或者是条目返回对应的布局
    public int getLayoutId(int position);

}
