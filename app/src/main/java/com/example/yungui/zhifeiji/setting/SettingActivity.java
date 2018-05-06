package com.example.yungui.zhifeiji.setting;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.homepage.MainActivity;
import com.example.yungui.zhifeiji.util.StatusBarUtil;

public class SettingActivity extends AppCompatActivity{
    private SettingFragment fragment;
    private SettingPresenter settingPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//---------------------设置亮色主题 -------------------------------------
        StatusBarUtil.StatusBarLightMode(SettingActivity.this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            SettingActivity.this.getWindow()
//                    .getDecorView()
//                    .setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //安位或
//        }
        setContentView(R.layout.activity_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.setting_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            fragment = new SettingFragment();

        } else {
            fragment= (SettingFragment) getFragmentManager().getFragment(savedInstanceState, "setting_fragment");
        }
        settingPresenter = new SettingPresenter(this, fragment);

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_container, fragment,"setting_fragment")
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.onBackPressed();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
