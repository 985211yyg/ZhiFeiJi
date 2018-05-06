package com.example.yungui.zhifeiji.homepage;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/2/14.
 */

public interface DouBanMomentContract {

    interface View extends BaseView<Presenter> {
        //显示错误
        void showError();

        //正在加载
        void showLoading();

        //停止加载
        void stopLoading();

        //成功获取数据之后再界面中显示
        void showResult(ArrayList<DouBanMomentNews.PostsBean> list);
       //x显示时间选择界面
        void showDialog();
    }

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
