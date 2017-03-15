package com.example.yungui.zhifeiji.about;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.yungui.zhifeiji.license.LicenseActivity;

/**
 * Created by yungui on 2017/3/14.
 */

public class AboutPreferencePresenter implements AboutPreferenceContract.Presenter {
    private AppCompatActivity activity;
    private AboutPreferenceContract.View view;
    private SharedPreferences sharedPreferences;

    public AboutPreferencePresenter(AppCompatActivity activity, AboutPreferenceContract.View view) {
        this.activity = activity;
        this.view = view;

        //为view绑定presenter
        view.setPresenter(this);
        sharedPreferences = activity.getSharedPreferences("user_setting", Context.MODE_PRIVATE);
    }

    @Override
    public void start() {

    }

    @Override
    public void openAppStore() {
        view.showVersionError();

    }

    @Override
    public void openEmail() {
        Uri uri = Uri.parse("mailto:2289201033@qq.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "纸飞机使用反馈级建议");
        intent.putExtra(Intent.EXTRA_TEXT, "设备型号：" + Build.MODEL + "\n"
                + "软件版本：" + Build.VERSION.RELEASE + "\n"
                + "版本号：V1.0");
        activity.startActivity(intent);

    }

    @Override
    public void followGitHub() {
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/")));

    }

    @Override
    public void copyAliPay() {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setTitle("捐赠");
        dialog.setMessage("您通过支付宝向开发者进行打赏捐赠！");
        dialog.setButton(Dialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //将支付宝复制
                //获取剪贴板
                ClipboardManager manager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                //剪切内容
                ClipData clipData = ClipData.newPlainText("text", "18729901404");
                manager.setPrimaryClip(clipData);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();


    }

    @Override
    public void openLicense() {
        activity.startActivity(new Intent(activity, LicenseActivity.class));
    }
}
