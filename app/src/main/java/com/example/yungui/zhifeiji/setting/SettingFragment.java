package com.example.yungui.zhifeiji.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.yungui.zhifeiji.R;

/**
 * Created by yungui on 2017/3/14.
 */

public class SettingFragment extends PreferenceFragment implements SettingContract.View {
    private SettingContract.Presenter presenter;
    private Preference timePreference;
    private Context context;

    public SettingFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.seeting_fragment);
        initViews(getView());

        findPreference("image_mode").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.setNoImageMode(preference);
                return false;
            }
        });

        findPreference("inner_browser").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.setBrowserInInnerBrowser(preference);
                return false;
            }
        });

        findPreference("clear").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.cleanImageCache(preference);
                return false;
            }
        });

        timePreference = findPreference("store_article");
        timePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.i("TAG", "---------->>>>>>>>>onPreferenceChange: "+o.toString());
                //设置缓存的天数
                timePreference.setSummary(presenter.getTimeSummary());
                presenter.setArticleSaveDays(preference,o);
                return false;
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public void initViews(View view) {
        context = getActivity();

    }

    @Override
    public void showCleanImageCacheDone() {
        Snackbar.make(getActivity().findViewById(R.id.setting_toolbar),"清除图片缓存成功",Snackbar.LENGTH_SHORT).show();
    }
}
