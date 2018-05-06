package com.example.yungui.zhifeiji.interfaze;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by yungui on 2017/3/28.
 */

public interface onImageDownLoadListener {

    void onImageDownloadSuccess(Bitmap bitmap);
    void onImageDownloadSuccess(File file);

    void onImageDownloadFailed();

}
