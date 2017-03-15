package com.example.yungui.zhifeiji.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yungui.zhifeiji.R;

public class AboutPreferenceActivity extends AppCompatActivity {
    private AboutPreferencePresenter presenter;
    private AboutPreferenceFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置主题
//        setTheme(R.style.AppTheme_NoActionBar);
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
