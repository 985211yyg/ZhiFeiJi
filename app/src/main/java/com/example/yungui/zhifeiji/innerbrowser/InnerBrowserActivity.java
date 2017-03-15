package com.example.yungui.zhifeiji.innerbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yungui.zhifeiji.R;

public class InnerBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_browser);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.innerBrowser_container, InnerBrowserFragment.newInstance())
                .commit();

    }
}
