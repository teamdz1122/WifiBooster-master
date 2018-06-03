package com.binhdz.wifibooster.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.callback.CompleteClearCache;
import com.binhdz.wifibooster.callback.LoadCache;
import com.binhdz.wifibooster.fragment.FragmentListAppCache;
import com.binhdz.wifibooster.loadcache.LoadAppInfo;
import com.binhdz.wifibooster.model.AppInfo;
import com.binhdz.wifibooster.util.AppConstant;
import com.binhdz.wifibooster.view.MyTextViewBold;
import com.binhdz.wifibooster.view.loading.LoadingView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by admin on 1/6/2018.
 */

public class BootNetwork extends AppCompatActivity implements LoadCache, CompleteClearCache, View.OnClickListener {
    Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            ivScan.animate().rotationBy(360).withEndAction(this).setDuration(2000).
                    setInterpolator(new LinearInterpolator()).start();
        }
    };
    private ArrayList<AppInfo> listAppInfo;
    private TextView tvScanning;
    private MyTextViewBold tvBootedSpeed, tvBootSpeed;
    private ImageView ivBgScan, ivScan, ivBack;
    private FragmentListAppCache mFragmentListAppCache;
    private LinearLayout lnTopSpeed, lnBottomScan, lnBotomSpeed, lnBootedComplete;
    private RelativeLayout lyTopScan;
    private LoadingView mLoadingView;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String nameApp = (String) msg.obj;

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot_network);

        initViews();
        setUpSlidingUpLayour();
    }

    private void setUpSlidingUpLayour() {

        mFragmentListAppCache = new FragmentListAppCache();
        mFragmentListAppCache.setCallback(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.boot_network__content, mFragmentListAppCache, AppConstant.FRM_LIST_APP_CACHE);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initViews() {
        //setup layout
        lyTopScan = (RelativeLayout) findViewById(R.id.boot_net__ly_top_scan);
        lnBottomScan = (LinearLayout) findViewById(R.id.boot_net__ln_bottom_scan);

        lnTopSpeed = (LinearLayout) findViewById(R.id.boot_net_ln_top_speed);
        lnBotomSpeed = (LinearLayout) findViewById(R.id.boot_network__content);
        lnBootedComplete = (LinearLayout) findViewById(R.id.boot_net__ln_boot_complete);

        ivBgScan = (ImageView) findViewById(R.id.boot_net__iv_bg_scan);
        ivScan = (ImageView) findViewById(R.id.boot_net__iv_scan);
        ivBack = (ImageView) findViewById(R.id.boot_net_iv_back);
        ivBack.setOnClickListener(this);
        ivScan.animate().rotationBy(360).withEndAction(runnable).setDuration(2000).
                setInterpolator(new LinearInterpolator()).start();

        tvScanning = (TextView) findViewById(R.id.boot_net__iv_scanning);
        tvBootedSpeed = (MyTextViewBold) findViewById(R.id.boot_net__tv_booted_speed);
        tvBootSpeed = (MyTextViewBold) findViewById(R.id.boot_net__tv_boot_speed);

        mLoadingView = (LoadingView) findViewById(R.id.boot_net__loding_fish_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CLEAR_APP_CACHE);


        }

        LoadAppInfo loadAppInfo = new LoadAppInfo(this, this);
        loadAppInfo.execute();
    }

    @Override
    public void showCache(long size, String nameApp) {
        Message msg = handler.obtainMessage();
        msg.arg1 = (int) size;
        msg.obj = nameApp;
        handler.sendMessage(msg);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void canAnim(ArrayList<AppInfo> arrAppinfo, long totalCacheSizeUser, long totalCachSizeSystem) {
        ArrayList<AppInfo> arrAppInfoUse = new ArrayList<>();
        for (AppInfo appInfo : arrAppinfo) {
            if (appInfo.isUserApp() && appInfo.getCacheSize() > 12 * 1024)
                arrAppInfoUse.add(appInfo);
        }
        if (arrAppInfoUse.size() > 2) {

            mFragmentListAppCache.setData(arrAppinfo, totalCachSizeSystem);
            Random random = new Random();
            int speedPecent = random.nextInt(15) + 5;
            tvBootSpeed.setText(speedPecent + "");
            tvBootedSpeed.setText(getString(R.string.speed_is_booted) + " " + speedPecent + "%");

            Animation bottomUp = AnimationUtils.loadAnimation(BootNetwork.this, R.anim.bottom_up);

            lnBottomScan.setVisibility(View.GONE);
            lyTopScan.setVisibility(View.GONE);

            lnBotomSpeed.startAnimation(bottomUp);
            lnTopSpeed.setVisibility(View.VISIBLE);
            lnBotomSpeed.setVisibility(View.VISIBLE);

        } else {
            lnBottomScan.setVisibility(View.GONE);
            lyTopScan.setVisibility(View.GONE);
            completeClear();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void completeClear() {

        Animation bottomDown = AnimationUtils.loadAnimation(BootNetwork.this, R.anim.bottom_down);
        lnBotomSpeed.startAnimation(bottomDown);
        lnTopSpeed.setVisibility(View.GONE);
        lnBotomSpeed.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);

        lnBootedComplete.setVisibility(View.VISIBLE);
    }

    @Override
    public void startClearCache() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boot_net_iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
