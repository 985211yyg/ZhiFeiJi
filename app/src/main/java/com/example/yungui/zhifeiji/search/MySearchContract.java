package com.example.yungui.zhifeiji.search;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;

/**
 * Created by yungui on 2017/3/26.
 */

public interface MySearchContract {

    interface View extends BaseView<Presenter> {
        void showResult();
        void showLoadingError();
    }

    interface Presenter extends BasePresenter {
        void loadPost();

        void readDetail(int position);

    }

}
