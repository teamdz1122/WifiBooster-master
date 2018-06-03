package com.binhdz.wifibooster.loadcache;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.binhdz.wifibooster.callback.LoadCache;
import com.binhdz.wifibooster.model.AppInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 1/6/2018.
 */

public class LoadAppInfo extends AsyncTask<Void, Integer, List<AppInfo>> {
    public static final String LIST_APPINFO = "LIST_APPINFO";
    public static final String SIZE_CACHE = "SIZE_CACHE";
    public static ArrayList<AppInfo> mListAppInfo = new ArrayList<>();
    private Context mContext;
    private Method mGetPackageSizeInfoMethod;
    private LoadCache loadCache;
    private long mTotalSizeCache = 0;
    private long mTotalSizeCacheSystem =0;

    private int count = 0;
    private int sizeProgrss = 0;

/*    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return false;
        }
    });*/

    public LoadAppInfo(Context mContext, LoadCache loadCache) {
        this.mContext = mContext;
        this.loadCache = loadCache;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<AppInfo> doInBackground(Void... voids) {

        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> listAppInfo = pm.getInstalledPackages(0);
        ArrayList<AppInfo> arrAppInfo = new ArrayList<>();

        for (PackageInfo packageInfo : listAppInfo) {




            final AppInfo appInfo = new AppInfo();
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(pm);
            appInfo.setAppIcon(appIcon);


            int flags = packageInfo.applicationInfo.flags;
            int uid = packageInfo.applicationInfo.uid;
            appInfo.setUid(uid);

            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                appInfo.setUserApp(false);
            } else {
                appInfo.setUserApp(true);
            }
            if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
                appInfo.setInRom(false);
            } else {
                appInfo.setInRom(true);
            }

            String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
            appInfo.setAppName(appName);

            String packname = packageInfo.packageName;

            appInfo.setPackName(packname);


            String version = packageInfo.versionName;
            appInfo.setVersion(version);
            try {
                mGetPackageSizeInfoMethod = mContext.getPackageManager().getClass().getMethod(
                        "getPackageSizeInfo", String.class, IPackageStatsObserver.class);


            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            try {
                mGetPackageSizeInfoMethod.invoke(mContext.getPackageManager(), new Object[]{
                        packname,
                        new IPackageStatsObserver.Stub() {
                            @Override
                            public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

                                synchronized (appInfo) {

                                    appInfo.setPkgSize(pStats.cacheSize + pStats.codeSize + pStats.dataSize);
                                    appInfo.setCacheSize(pStats.cacheSize);

                                    if (appInfo.isUserApp() && appInfo.getCacheSize() > 12 * 1024) {
                                        mTotalSizeCache = mTotalSizeCache + appInfo.getCacheSize();
                                    }else if(!appInfo.isUserApp()  && appInfo.getCacheSize()> 12*1024){
                                        mTotalSizeCacheSystem += appInfo.getCacheSize();
                                    }

                                }

                            }
                        }
                });
            } catch (Exception e) {
            }


            arrAppInfo.add(appInfo);
        }

        Log.i("LOAD_CACHE", "Tao da load xong");
        return arrAppInfo;
    }

    @Override
    protected void onPostExecute(List<AppInfo> appInfos) {
        super.onPostExecute(appInfos);
        loadCache.canAnim((ArrayList<AppInfo>) appInfos, mTotalSizeCache, mTotalSizeCacheSystem);

    }

 /*   private void convertList( List<PackageInfo> listAppInfo ){
        ArrayList<PackageInfo> listAppUser = new ArrayList<>();
        for (PackageInfo packageInfo : listAppInfo){
          if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==0)  {

          }
        }
    }*/
}
/* try {
                Thread.sleep(10);
                if (appInfo.isUserApp() == true) {
                    loadCache.showCache(mTotalSizeCache, appInfo.getAppName());

                } else {
                    loadCache.showCache(mTotalSizeCache, appInfo.getAppName());
                }
                count++;

                if (count % 10 == 0) {
                    sizeProgrss = sizeProgrss + 1000 / listAppInfo.size() + 1;


                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
 */