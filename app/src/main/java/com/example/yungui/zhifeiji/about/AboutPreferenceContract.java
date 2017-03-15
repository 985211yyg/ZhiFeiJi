package com.example.yungui.zhifeiji.about;

import com.example.yungui.zhifeiji.BasePresenter;
import com.example.yungui.zhifeiji.BaseView;

/**
 * Created by yungui on 2017/3/14.
 */

public interface AboutPreferenceContract {

    interface View extends BaseView<Presenter> {

        //点击版本时如果没有安装应用商店则提示错误
        void showVersionError();

        //点击建议意见是，如果没有安装邮件应用则提示错误
        void showFeedbackError();

        //点击GitHub是，如果没有安装browser是提示
        void showBrowserNotInstall();

    }

    interface Presenter extends BasePresenter {
        //进入应用商店
        void openAppStore();

        //打开邮件反馈
        void openEmail();

        //关注GitHub
        void followGitHub();

        //复制支付宝账号
        void copyAliPay();

        //打开开源许可
        void openLicense();

    }
}
