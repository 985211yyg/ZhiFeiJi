package com.example.yungui.zhifeiji.homepage;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.about.AboutPreferenceActivity;
import com.example.yungui.zhifeiji.bookmarks.BookMarkFragment;
import com.example.yungui.zhifeiji.bookmarks.BookMarkPresenter;
import com.example.yungui.zhifeiji.service.CacheService;
import com.example.yungui.zhifeiji.setting.SettingActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private MainFragment mainFragment;
    private BookMarkFragment bookMarkFragment;
    private int Permission;
    public static final int MY_REQUEST_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置uimode为夜间模式
//        AppCompatDelegate.setDefaultNightMode(Configuration.UI_MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
//----------------------------设置主题------------------------------------------
//        StatusBarUtil.StatusBarLightMode(MainActivity.this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            MainActivity.this.getWindow()
//                    .getDecorView()
//                    .setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //安位或
//        }
//-----------------------------------------------------------------------
        setContentView(R.layout.activity_main);
        initView();
//----------------------------权限请求-----------------------------------------------------------------------------

        Permission = ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //PackageManager.PERMISSION_DENIED   权限拒绝
        //如果没有廷议权限
        if (Permission!=PackageManager.PERMISSION_GRANTED) {
            //是否需要作出请求权限解析，之前拒绝过或者用户选择了dont ask again
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //之前已经拒绝过，需要作出解释

            } else {
                //不用解析，直接请求
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        ,MY_REQUEST_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        }
//--------------------------------------恢复fragment-------------------------------------------------------------

        if (savedInstanceState != null) {
            mainFragment = (MainFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mainFragment");
            bookMarkFragment = (BookMarkFragment) getSupportFragmentManager().getFragment(savedInstanceState, "bookMarkFragment");
        } else {
            //如果没有存储片段则重新创建
            mainFragment = MainFragment.newInstance();
            bookMarkFragment = BookMarkFragment.newInstance();
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

        //开启缓存服务，以及广播接受服务
        startService(new Intent(this, CacheService.class));

        //实例化BookMarkPresenter
        new BookMarkPresenter(MainActivity.this, bookMarkFragment);
        //默认显示主页
        showMainFragment();


    }

    /**
     * 可以再次方法中恢复保存的状态也可以在onCreate中
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
          运行是权限的处理
         */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_PERMISSIONS_WRITE_EXTERNAL_STORAGE:
                //请求权限得到允许
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: 2017/4/1

                } else {
                    //没有权限，直接退出不用运行程序
                    MainActivity.this.finish();
                }
                break;
        }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            showMainFragment();

        } else if (id == R.id.nav_collection) {
            showBookFragment();

        } else if (id == R.id.nav_theme) {
            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                /*
                抽屉滑开
                 */
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }
                /*
                完全打开
                 */
                @Override
                public void onDrawerOpened(View drawerView) {

                }
                /*
                关闭
                 */
                @Override
                public void onDrawerClosed(View drawerView) {
                    //保存主题模式
                    SharedPreferences sharedPreferences = getSharedPreferences("user.setting", MODE_PRIVATE);

                    //获取当前主题的状态，0为夜间，1为白天
                    int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    //1.黑夜
                    if (mode == Configuration.UI_MODE_NIGHT_YES) {
                        sharedPreferences.edit().putInt("theme", 0).apply();
//                        /切换为白天模式
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else  {
                        //2、白天
                        sharedPreferences.edit().putInt("theme", 1).apply();
                        //切换为黑夜模式
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    //设置窗口动画
                    getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                    //重新绘制，但是此时需要在状态改变时保存原有的状态
                    recreate();
                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            });

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutPreferenceActivity.class));

        }

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //存储fragment
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "mainFragment", mainFragment);
        }
        if (bookMarkFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "bookMarkFragment", bookMarkFragment);
        }
    }
}
