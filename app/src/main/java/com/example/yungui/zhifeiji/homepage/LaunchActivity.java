package com.example.yungui.zhifeiji.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.yungui.zhifeiji.R;

/**
 * Created by yungui on 2017/4/2.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch);
        new IntentHandler().sendEmptyMessageDelayed(0x123, 800);

    }

    private class IntentHandler extends Handler {

        public IntentHandler() {
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                LaunchActivity.this.finish();
            }
        }
    }


}
