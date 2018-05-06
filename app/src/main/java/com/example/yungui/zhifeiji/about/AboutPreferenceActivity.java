package com.example.yungui.zhifeiji.about;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.setting.SettingActivity;
import com.example.yungui.zhifeiji.util.StatusBarUtil;

public class AboutPreferenceActivity extends AppCompatActivity {
    private AboutPreferencePresenter presenter;
    private AboutPreferenceFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//---------------------设置亮色主题 -------------------------------------
        StatusBarUtil.StatusBarLightMode(AboutPreferenceActivity.this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            AboutPreferenceActivity.this.getWindow()
//                    .getDecorView()
//                    .setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //安位或
//        }
        setContentView(R.layout.activity_about);
        setSupportActionBar((Toolbar) findViewById(R.id.about_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {

            fragment = new AboutPreferenceFragment();
        } else {
            fragment = (AboutPreferenceFragment) getFragmentManager().getFragment(savedInstanceState, "about_fragment");
        }
        /*
        为presenter设置view
         */
        presenter = new AboutPreferencePresenter(AboutPreferenceActivity.this, fragment);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.about_Container, fragment, "about_fragment")
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
