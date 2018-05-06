package com.example.yungui.zhifeiji.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.yungui.zhifeiji.interfaze.onImageDownLoadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


/**
 * Created by yungui on 2017/3/28.
 */

public class ImageDownload implements Runnable {

    private Context context;
    private String imageUrl;
    //文件名字
    private String picName;
    //文件保存目录
    private File picFile;
    private onImageDownLoadListener downLoadListener;
    private File file = null;
    private Bitmap bitmap = null;
    public static final String TAG = ImageDownload.class.getSimpleName();

    public ImageDownload(Context context, String url, onImageDownLoadListener downLoadListener) {
        this.context = context;
        imageUrl = url;
        this.downLoadListener = downLoadListener;
    }

    @Override
    public void run() {
        try {
            bitmap = Glide.with(context)
                    .load(imageUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            file = Glide.with(context)
                    .load(imageUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();

            //执行图片的保存
            if (bitmap != null) {
                saveImage(context, bitmap);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            downLoadListener.onImageDownloadFailed();
        } catch (ExecutionException e) {
            e.printStackTrace();
            downLoadListener.onImageDownloadFailed();

        } finally {
            if (bitmap != null) {
                downLoadListener.onImageDownloadSuccess(bitmap);
            } else {
                downLoadListener.onImageDownloadFailed();
            }
            if (file != null) {
                downLoadListener.onImageDownloadSuccess(file);
            } else {
                downLoadListener.onImageDownloadFailed();
            }

        }


    }


    private void saveImage(Context context, Bitmap bitmap) {
        //此处范围的所谓外部存储是手机的自带内存32G,64G，并不是SD卡，是否有访问权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File newFileDir = new File(Environment.getExternalStorageDirectory(), "zhifeiji");
            if (!newFileDir.exists()) {
                newFileDir.mkdir();
            }
            File file = new File(newFileDir, System.currentTimeMillis() + ".jpg");
            Log.e(TAG, "根目录里面的所有目录：" + newFileDir.exists());
            //打开文件输出流
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
