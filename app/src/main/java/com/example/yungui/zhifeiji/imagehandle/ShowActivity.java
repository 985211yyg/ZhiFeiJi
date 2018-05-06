package com.example.yungui.zhifeiji.imagehandle;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yungui.zhifeiji.R;
import com.example.yungui.zhifeiji.interfaze.onImageDownLoadListener;
import com.example.yungui.zhifeiji.util.ImageDownload;

import java.io.File;
import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    private String imageUrl;
    private ArrayList<String> urls = new ArrayList<>();
    private ImageView imageView;
    public static final String TAG = ShowActivity.class.getSimpleName();
    private GestureDetector detector;
    private int distance = 60;
    private Toolbar toolbar;

    private ImageDownload imageDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        imageUrl = getIntent().getStringExtra("img");
        imageView = (ImageView) findViewById(R.id.show_imageView);
        detector = new GestureDetector(this, this);

        Glide.with(ShowActivity.this).load(imageUrl)
                .error(R.mipmap.launcher)
                .fitCenter()
                .into(imageView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将触摸事件交给detector
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_handle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //返回
            this.onBackPressed();
        } else if (id == R.id.save_image) {
            imageDownload = new ImageDownload(this, imageUrl, new onImageDownLoadListener() {
                @Override
                public void onImageDownloadSuccess(Bitmap bitmap) {
                    Snackbar.make(toolbar, "保存成功！", Snackbar.LENGTH_SHORT).show();

                }

                @Override
                public void onImageDownloadSuccess(File file) {

                }

                @Override
                public void onImageDownloadFailed() {
                    Snackbar.make(toolbar, "保存失败！", Snackbar.LENGTH_SHORT).show();

                }
            });
            new Thread(imageDownload).start();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if ((motionEvent1.getX() - motionEvent.getX()) > distance) {
            this.finish();
        }
        return true;
    }
}
