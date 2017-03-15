package com.example.yungui.zhifeiji.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.bean.BeanType;

public class DetailActivity extends AppCompatActivity {
    private DetailFragment fragment;
    private DetailPresenter detailPresenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        添加哦片段
         */
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        //获取intent，及其传递过来的数据
        Intent intent = getIntent();
        detailPresenter = new DetailPresenter(DetailActivity.this, fragment);
        detailPresenter.setId(intent.getIntExtra("id", 0));
        detailPresenter.setTitle(intent.getStringExtra("title"));
        detailPresenter.setCoverUrl(intent.getStringExtra("coverUrl"));
        detailPresenter.setBeanType((BeanType) intent.getSerializableExtra("beanType"));


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
