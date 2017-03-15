package com.example.yungui.zhifeiji.about;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yungui.zhifeiji.R;

/**
 * Created by yungui on 2017/3/14.
 */

public class AboutPreferenceFragment extends PreferenceFragment implements AboutPreferenceContract.View {
    private AboutPreferenceContract.Presenter presenter;
    private Toolbar toolbar;

    public AboutPreferenceFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载XML布局
        addPreferencesFromResource(R.xml.about_perference_fragment);
        //获取layout布局
        initViews(getView());

        findPreference("version").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.openAppStore();
                Log.i("TAG", "--------->>>>>>>>>onPreferenceChange: " + "点击了版本");
                return false;
            }
        });
        findPreference("suggest").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.openEmail();
                return false;
            }
        });
        findPreference("gitHub").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.followGitHub();
                return false;
            }
        });
        findPreference("support").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.copyAliPay();
                return false;
            }
        });
        findPreference("source").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.openLicense();
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setPresenter(AboutPreferenceContract.Presenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public void initViews(View view) {
        toolbar = (Toolbar) getActivity().findViewById(R.id.about_toolbar);

    }

    @Override
    public void showVersionError() {
        Snackbar.make(toolbar, "没有应用市场", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showFeedbackError() {
        Snackbar.make(toolbar, "没有安装邮件", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showBrowserNotInstall() {
        Snackbar.make(toolbar, "没有安装浏览器", Snackbar.LENGTH_SHORT).show();
    }
}
