package com.example.yungui.zhifeiji.customtabs;

import android.app.Activity;
import android.net.Uri;

/**
 * To be used as a fallback to open the Uri when Custom Tabs is not available.
 */
public interface CustomTabFallback {
    /**
     *
     * @param activity The Activity that wants to open the Uri.
     * @param uri The uri to be opened by the fallback.
     */
    void openUri(Activity activity, Uri uri);
}
