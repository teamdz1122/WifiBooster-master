package com.binhdz.wifibooster.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.service.FloatWindowService;
import com.binhdz.wifibooster.util.PreferenceUtil;
import com.google.android.gms.ads.MobileAds;

import java.lang.reflect.Field;

import static com.binhdz.wifibooster.util.Constants.OVERLAY_PERMISSION_REQ_CODE;
import static com.binhdz.wifibooster.util.Constants.SP_STATUSBAR_HEIGHT;

/**
 * Created by admin on 1/6/2018.
 */

public class SlpashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    private PreferenceUtil preUtil;
    private Intent intentStartService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, "ca-app-pub-9569615767688214~3120681738");
        setUpService();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SlpashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    private void setUpService() {
        preUtil = PreferenceUtil.getSingleton(getApplicationContext());
        intentStartService = new Intent(SlpashActivity.this, FloatWindowService.class);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_alert), Toast.LENGTH_LONG).show();
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(permissionIntent, OVERLAY_PERMISSION_REQ_CODE);
        } else {*/
        startService(intentStartService);
        /*}*/
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_result), Toast.LENGTH_SHORT).show();
            } else {
                startService(intentStartService);
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        saveStatusBarHeight();
    }

    @SuppressLint("PrivateApi")
    private void saveStatusBarHeight() {
        if (preUtil.getInt(SP_STATUSBAR_HEIGHT, 0) == 0) {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            if (statusBarHeight == 0) {
                Class<?> c;
                Object obj;
                Field field;
                int x;
                try {
                    c = Class.forName("com.android.internal.R$dimen");
                    obj = c.newInstance();
                    field = c.getField("status_bar_height");
                    x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = getResources().getDimensionPixelSize(x);
                    Log.d("gjy", "statusBarHeight2=" + statusBarHeight);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            preUtil.saveInt(SP_STATUSBAR_HEIGHT, statusBarHeight);
        }
    }
}

