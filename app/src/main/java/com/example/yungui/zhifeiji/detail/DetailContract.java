package com.example.yungui.zhifeiji.detail;

import android.view.View;
import android.webkit.WebView;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;

/**
 * Created by yungui on 2017/3/13.
 */

public interface DetailContract {
    interface View extends BaseView<Presenter> {

        //处理view界面
        void showLoading();

        void stopLoading();

        void showLoadingError();

        void showShareError();
        //数据请求成功
        void showResult(String result);

        void showResultWithoutBody(String url);
        //显示封面
        void showCover(String url);

        //设置标题
        void setTitle(String title);

        void setImageMode(boolean showImage);

        //流浪器加载出错
        void showBrowserNotFindError();

        void showTextCopied();

        //复制文字出错
        void showTextCopyError();

        //添加书签成功的提示
        void showAddedToBookmarks();
        //从书签中删除的提示
        void showDeletedFromBookmarks();



    }

    interface Presenter extends BasePresenter {
        /*
        负责逻辑处理
         */
        //在浏览器中打开
        void openInBrowser();

        //一文本的形式分享
        void shareAsText();

        void openUrl(WebView webView, String url);

        void copyText();

        void copyLink();
        //书签的删除添加
        void addToOrDeleteFromBookmarks();
        //检查是否添加书签
        boolean queryIfIsBookmarked();
        //请求数据
        void requestData();

    }
}
