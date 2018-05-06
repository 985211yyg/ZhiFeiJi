package com.example.yungui.zhifeiji.detail;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.bean.BeanType;
import com.example.yungui.zhifeiji.setting.SettingActivity;
import com.example.yungui.zhifeiji.util.StatusBarUtil;

public class DetailActivity extends AppCompatActivity {
    private DetailFragment fragment;
    private DetailPresenter detailPresenter;
    private Toolbar toolbar;
    public static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// ---------------------------设置亮色主题 ----------------------------------
//        StatusBarUtil.StatusBarLightMode(DetailActivity.this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            DetailActivity.this.getWindow()
//                    .getDecorView()
//                    .setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //安位或
//        }
        setContentView(R.layout.activity_detail);
        initView();
        setSupportActionBar(toolbar);
        /*
        恢复fragment的状态
         */
        if (savedInstanceState != null) {
            fragment = (DetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "DetailFragment");
        } else {
            fragment = DetailFragment.newInstance();
        }
        /*
        添加片段
         */
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        detailPresenter = new DetailPresenter(DetailActivity.this, fragment);

        //获取intent，及其传递过来的数据
        Intent intent = getIntent();
        detailPresenter.setId(intent.getIntExtra("id", 0));
        detailPresenter.setTitle(intent.getStringExtra("title"));
        detailPresenter.setCoverUrl(intent.getStringExtra("coverUrl"));
        detailPresenter.setBeanType((BeanType) intent.getSerializableExtra("beanType"));

        Log.e(TAG, ">>>>>>>>>>DetailActivity: " +detailPresenter.toString() );


    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

    }

    /*
    保存fragment的状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "DetailFragment", fragment);
        }
    }
}
