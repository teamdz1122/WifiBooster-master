package com.binhdz.wifibooster.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.adapter.AdapterListAppInBg;
import com.binhdz.wifibooster.model.AppInfo;
import com.binhdz.wifibooster.view.BinhdzProgressBar;
import com.binhdz.wifibooster.view.loading.LoadingView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 1/6/2018.
 */

public class RealTimeProcess extends AppCompatActivity implements View.OnClickListener {

    private BinhdzProgressBar mPrgRam, mPrgStorate, mPrgCpu;
    private RecyclerView mRecyclerView;
    private AdapterListAppInBg adapterListAppInBg;
    private ImageView ivTopBack;
    private NativeExpressAdView mContainerAd;

    private long totalMemory, nowMemory;
    private long workT, totalT, total, work, totalBefore, workBefore;
    private BufferedReader reader;
    private float cpuTotal;
    private String[] sa;
    private DecimalFormat mFormatPercent = new DecimalFormat("##0");

    private Method mGetPackageSizeInfoMethod;
    private LoadingView mLoadingView;

    private Handler mHandler = new Handler();
    public Runnable mUpdateTimeTask = new Runnable() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void run() {

            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = registerReceiver(null, intentFilter);
            //pin
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            long s = stat.getBlockCountLong();
            //ram
            Log.d("REAL_TIME", "Memory  :  " + (100 - availableBlocks * 100 / s) + "%");
            mPrgStorate.setProgress((int) (100 - availableBlocks * 100 / s));
            //memory
            try {
                ActivityManager actManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
                actManager.getMemoryInfo(memInfo);
                totalMemory = memInfo.totalMem / 1024;
                long usermemory = totalMemory - memInfo.availMem / 1024;
                nowMemory = usermemory * 100 / totalMemory;
                Log.d("REAL_TIME", "Ram = " + nowMemory + "%");
                mPrgRam.setProgress((int) nowMemory);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //cpu

            try {
                reader = new BufferedReader(new FileReader("/proc/stat"));
                sa = reader.readLine().split("[ ]+", 9);
                work = Long.parseLong(sa[1]) + Long.parseLong(sa[2]) + Long.parseLong(sa[3]);
                total = work + Long.parseLong(sa[4]) + Long.parseLong(sa[5]) + Long.parseLong(sa[6]) + Long.parseLong(sa[7]);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (totalBefore != 0) {
                totalT = total - totalBefore;
                workT = work - workBefore;
                cpuTotal = restrictPercentage(workT * 100 / (float) totalT);
            }
            totalBefore = total;
            workBefore = work;
            Log.d("REAL_TIME", " cpu = " + mFormatPercent.format(cpuTotal) + "%");
            mPrgCpu.setProgress(Integer.parseInt(mFormatPercent.format(cpuTotal)));
            mHandler.postDelayed(this, 3000);
        }
    };

    public static String getAppNameByPID(Context context, int pid) {
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_cpu);
        initView();
        Thread thread = new Thread(mUpdateTimeTask);
        thread.start();
        LoadAppInBg loadAppInBg = new LoadAppInBg(this);
        loadAppInBg.execute();

        final NativeExpressAdView mAdView = new NativeExpressAdView(this);
        final AdRequest request = new AdRequest.Builder().build();
        mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH,AdSize.AUTO_HEIGHT));
        mAdView.setAdUnitId("ca-app-pub-9569615767688214/5083951357");
        mContainerAd.addView(mAdView);
        mAdView.loadAd(request);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();

            }
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

            }

            @Override
            public void onAdLoaded() {
                mContainerAd.setVisibility(View.VISIBLE);
                super.onAdLoaded();

            }
        });



    }

    private ArrayList<AppInfo> initData() {
        ArrayList<AppInfo> arrListApp = new ArrayList<>();

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> arrListService = am.getRunningServices(100);
        PackageManager pm = getPackageManager();

        for (ActivityManager.RunningServiceInfo runningServiceInfo : arrListService) {
            final AppInfo appInfo = new AppInfo();

            ApplicationInfo app = null;
            try {
                app = this.getPackageManager().getApplicationInfo(getPackageManager().getNameForUid(runningServiceInfo.uid), 0);
                Drawable icon = pm.getApplicationIcon(app);
                try {
                    PackageInfo applicationInfo = pm.getPackageInfo(getAppNameByPID(this, runningServiceInfo.pid), 0);
                    String name = applicationInfo.applicationInfo.loadLabel(pm).toString();
                    int flags = applicationInfo.applicationInfo.flags;


                    if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        appInfo.setPackName(getAppNameByPID(this, runningServiceInfo.pid));


                        appInfo.setAppIcon(icon);
                        appInfo.setAppName(name);

                        arrListApp.add(appInfo);

                    }


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

        return arrListApp;
    }

    private void initView() {
        mPrgStorate = (BinhdzProgressBar) findViewById(R.id.real_cpu__prg_cpu);
        mPrgCpu = (BinhdzProgressBar) findViewById(R.id.real_cpu__prg_ram);
        mPrgRam = (BinhdzProgressBar) findViewById(R.id.real_cpu__prg_storate);

        ivTopBack = (ImageView) findViewById(R.id.view_top_iv_back);
        ivTopBack.setOnClickListener(this);

        mContainerAd = (NativeExpressAdView)findViewById(R.id.ads_real_time);


        mRecyclerView = (RecyclerView) findViewById(R.id.real_cpu__rcv_list_app);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mLoadingView = (LoadingView) findViewById(R.id.real_cpu__loding_fish_view);

    }

    private float restrictPercentage(float percentage) {
        if (percentage > 100)
            return 100;
        else if (percentage < 0)
            return 0;
        else return percentage;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_top_iv_back:
                finish();
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    class LoadAppInBg extends android.os.AsyncTask<Void, Integer, List<AppInfo>> {
        Context mContext;

        public LoadAppInBg(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected List<AppInfo> doInBackground(Void... voids) {
            ArrayList<AppInfo> arrListApp = new ArrayList<>();

            ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> arrListService = am.getRunningServices(200);
            PackageManager pm = mContext.getPackageManager();

            for (ActivityManager.RunningServiceInfo runningServiceInfo : arrListService) {
                final AppInfo appInfo = new AppInfo();

                ApplicationInfo app = null;
                try {
                    app = mContext.getPackageManager().getApplicationInfo(getPackageManager().getNameForUid(runningServiceInfo.uid), 0);
                    Drawable icon = pm.getApplicationIcon(app);
                    try {
                        PackageInfo applicationInfo = pm.getPackageInfo(getAppNameByPID(mContext, runningServiceInfo.pid), 0);
                        String name = applicationInfo.applicationInfo.loadLabel(pm).toString();
                        int flags = applicationInfo.applicationInfo.flags;

                        if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                            appInfo.setUserApp(false);
                        } else {
                            appInfo.setUserApp(true);
                        }

                        appInfo.setPackName(getAppNameByPID(mContext, runningServiceInfo.pid));


                        appInfo.setAppIcon(icon);
                        appInfo.setAppName(name);
                        try {
                            mGetPackageSizeInfoMethod = mContext.getPackageManager().getClass().getMethod(
                                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);


                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                        try {
                            mGetPackageSizeInfoMethod.invoke(mContext.getPackageManager(), new Object[]{
                                    appInfo.getPackName(),
                                    new IPackageStatsObserver.Stub() {
                                        @Override
                                        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

                                            synchronized (appInfo) {
                                                appInfo.setPkgSize(pStats.cacheSize + pStats.codeSize + pStats.dataSize);
                                                Log.i("XXX", "size  =  " + appInfo.getPkgSize());
                                                appInfo.setCacheSize(pStats.cacheSize);


                                            }

                                        }
                                    }
                            });
                        } catch (Exception e) {
                        }


                        arrListApp.add(appInfo);


                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }

            return arrListApp;

        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            super.onPostExecute(appInfos);
            if (appInfos != null) {
                AdapterListAppInBg adapterListAppInBg = new AdapterListAppInBg(mContext);
                mRecyclerView.setAdapter(adapterListAppInBg);

                mLoadingView.setVisibility(View.GONE);


                // delete element same
                HashMap<String, AppInfo> mapAppInfo = new HashMap<>();
                for (AppInfo appInfo : appInfos) {
                    mapAppInfo.put(appInfo.getAppName(), appInfo);
                }
                appInfos.clear();
                Set set = mapAppInfo.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry) iterator.next();
                    appInfos.add((AppInfo) mentry.getValue());
                }

                Collections.sort(appInfos, new Comparator<AppInfo>() {
                    @Override
                    public int compare(AppInfo appInfo, AppInfo t1) {
                        if (appInfo.getPkgSize() > t1.getPkgSize()) {
                            return -1;
                        } else {
                            if (appInfo.getPkgSize() == t1.getPkgSize()) {
                                return 0;
                            } else return 1;
                        }

                    }
                });


                adapterListAppInBg.setUpData((ArrayList<AppInfo>) appInfos);
               /* for (AppInfo appInfo : appInfos) {
                    Log.i("XXX", appInfo.getAppName());
                    Log.i("XXX", appInfo.getPackName());
                    Log.i("XXX", "-----------------------------");

                }
                Log.i("XXX", "size ==" + appInfos.size());*/
            } else Log.i("XXX", "NULLLLLLLLLLLLLLLLLL");
        }
    }
}
