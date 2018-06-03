package com.binhdz.wifibooster.callback;

import com.binhdz.wifibooster.model.AppInfo;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2018.
 */

public interface LoadCache {
    public void showCache(long size, String nameApp);
    void canAnim(ArrayList<AppInfo> arrAppinfo, long totalCacheSizeUser, long totalCachSizeSystem);
}
