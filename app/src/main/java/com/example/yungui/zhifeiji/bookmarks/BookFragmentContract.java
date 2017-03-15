package com.example.yungui.zhifeiji.bookmarks;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;

/**
 * Created by yungui on 2017/2/13.
 */

public interface BookFragmentContract {
    //view相关
    interface View extends BaseView<Presenter> {
        //显示错误
        void showError();

        //正在加载
        void showLoading();

        //停止加载
        void stopLoading();

        //成功获取数据之后再界面中显示
        void showResult();

        //挑选日期时加载日期选择对话框
        void pickDialog();

        //跟新数据
        void notifyDataChange();


    }

    //逻辑相关
    interface Presenter extends BasePresenter {

        //请求数据
        void loadPost(long date, boolean clearing);

        //刷新数据
        void refresh();

        //加载更多
        void loadMore(long date);

        //进入详情
        void readDetail(int id);

        //随便看看
        void lookLook();
    }
}
