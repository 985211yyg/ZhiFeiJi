package com.example.yungui.zhifeiji.homepage;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;

import java.util.ArrayList;

/**
 * Created by yungui on 2017/2/9.
 */
    /*

    创建一个契约类用来同意管理presenter和view
     */
public interface ZhiHuDailyContract {
    //与view显示相关的的类
    interface View extends BaseView<Presenter> {
        //显示错误
        void showError();

        //正在加载
        void showLoading();

        //停止加载
        void stopLoading();

        //成功获取数据之后再界面中显示
        void showResult(ArrayList<ZhiHuDailyNews.Question> item);

        //挑选日期时加载日期选择对话框
        void pickDialog();


    }

    //逻辑处理相关的类
    interface Presenter extends BasePresenter {
        //请求数据
        void loadPost(long date, boolean clearing);

        //刷新数据
        void refresh();

        //加载更多
        void loadMore(long date);

        //进入详情
        void readDetail(int position);
        //随便看看
        void lookLook();

    }
}
