package com.example.yungui.zhifeiji.homepage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.about.AboutPreferenceActivity;
import com.example.yungui.zhifeiji.bookmarks.BookMarkFragment;
import com.example.yungui.zhifeiji.bookmarks.BookMarkPresenter;
import com.example.yungui.zhifeiji.service.CacheService;
import com.example.yungui.zhifeiji.setting.SettingActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , BookMarkFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;
    private MainFragment mainFragment;
    private BookMarkFragment bookMarkFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //恢复fragment
        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mainFragment");
            bookMarkFragment = (BookMarkFragment) getSupportFragmentManager().getFragment(savedInstanceState, "bookMarkFragment");
        } else {
            //如果没有存储片段则重新创建
            mainFragment = MainFragment.newInstance();
            bookMarkFragment = BookMarkFragment.newInstance(null, null);
        }


        //如果mainFragment没有添加到activity中，则添加
        if (!mainFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, mainFragment, "mainFragment")
                    .commit();
        }
        //如果bookFragment没有被加载到activity中，则添加
        if (!bookMarkFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, bookMarkFragment, "bookMarkFragment")
                    .commit();
        }
        //开启缓存服务
        startService(new Intent(this, CacheService.class));

        //实例化BookMarkPresenter
        new BookMarkPresenter(MainActivity.this, bookMarkFragment);

        //默认显示主页
        showMainFragment();


    }

    /*
    控制主页显示内容
     */
    private void showMainFragment() {
        getSupportFragmentManager().beginTransaction().show(mainFragment).hide(bookMarkFragment).commit();
        toolbar.setTitle("主页");

    }

    private void showBookFragment() {
        getSupportFragmentManager().beginTransaction().show(bookMarkFragment).hide(mainFragment).commit();
        toolbar.setTitle("收藏");

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            showMainFragment();

        } else if (id == R.id.nav_collection) {
            showBookFragment();

        } else if (id == R.id.nav_theme) {

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutPreferenceActivity.class));

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //存储fragment
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "mainFragment", mainFragment);
        }
        if (bookMarkFragment.isDetached()) {
            getSupportFragmentManager().putFragment(outState, "bookMarkFragment", bookMarkFragment);
        }
    }
}
