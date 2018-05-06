package com.example.yungui.zhifeiji.bookmarks;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;
import com.example.yungui.zhifeiji.bean.douban.DouBanMomentNews;
import com.example.yungui.zhifeiji.bean.guokr.GuoKrStory;
import com.example.yungui.zhifeiji.bean.zhihu.ZhiHuDailyNews;

import java.util.ArrayList;

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
        void showResult(ArrayList<ZhiHuDailyNews.Question> zhihuList
                ,ArrayList<GuoKrStory.ResultBean> guokrList
                ,ArrayList<DouBanMomentNews.PostsBean> doubanList
                ,ArrayList<Integer> itemType);

        void showSearchResult(ArrayList<ZhiHuDailyNews.Question> zhihuList
                ,ArrayList<GuoKrStory.ResultBean> guokrList
                ,ArrayList<DouBanMomentNews.PostsBean> doubanList
                ,ArrayList<Integer> itemType);


    }

    //逻辑相关
    interface Presenter extends BasePresenter {

        //请求数据
        void loadPost(boolean refresh);

        //刷新数据
        void refresh();

        //随便看看
        void lookLook();

        //展开详情
        void search(String search,boolean clearing);

        //展开详情
        void readDetail(int id);
    }
}
