package com.binhdz.wifibooster.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.util.AppConstant;
import com.binhdz.wifibooster.view.MyTextViewBold;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by admin on 1/6/2018.
 */

public class HostpotWifiActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean IS_ENABLE_HOSTPOT = false;

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1001;
    private boolean IS_ON_PASSWORD = true;

    private EditText etName, etPass;
    private ImageView ivTopHostpot, ivRadar, ivTopBack, ivSwith;
    private MyTextViewBold tvTitle, tvOpen;

    private WifiManager wifiManager;
    private WifiConfiguration netConfigHostpot;
    private Method methodHostpot;
    private NativeExpressAdView mContainerAd;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_hostpot);

        settingPermission();
        initViews();

        initData();

        if (IS_ENABLE_HOSTPOT) {

            etName.setTextColor(getResources().getColor(R.color.color_while));
            etName.setBackground(getResources().getDrawable(R.drawable.bg_editext_name));

            etPass.setTextColor(getResources().getColor(R.color.color_while));
            etPass.setBackground(getResources().getDrawable(R.drawable.bg_editext_name));
            tvOpen.setText("Close");
            tvOpen.setTextColor(getResources().getColor(R.color.text_close_hotspot));
            tvOpen.setBackground(getResources().getDrawable(R.drawable.boder_bt_close_hostpot));

        }

    }

    private void initData() {
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
                super.onAdLoaded();

            }
        });


        SharedPreferences sp = getSharedPreferences("wifiHostpot", Context.MODE_PRIVATE);
        String nameHostpot = sp.getString(AppConstant.NAME_HOSTPOT_WIFI, null);
        String password = sp.getString(AppConstant.PASS_HOSTPOT_WIFI, null);
        if (nameHostpot != null && password != null) {
            etPass.setText(password);
            etName.setText(nameHostpot);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initViews() {
        etName = (EditText) findViewById(R.id.mobie_hostpot__edt_name);
        etPass = (EditText) findViewById(R.id.mobie_hostpot__edt_pass);
        mContainerAd = (NativeExpressAdView)findViewById(R.id.mobie_hostpot__adview);

        if (!IS_ENABLE_HOSTPOT) {
            setDefaultEDT(true);
        }

        ivTopBack = (ImageView) findViewById(R.id.view_top_iv_back);
        ivRadar = (ImageView) findViewById(R.id.mobie_hostpot__iv_radar);
        ivTopHostpot = (ImageView) findViewById(R.id.mobie_hostpot__iv_top);
        ivSwith = (ImageView) findViewById(R.id.mobie_hostpot__iv_swith);


        tvOpen = (MyTextViewBold) findViewById(R.id.mobie_hostpot__tv_open);
        tvTitle = (MyTextViewBold) findViewById(R.id.view_top__iv_title);
        tvTitle.setText(getResources().getString(R.string.mobie_hostpot));

        tvOpen.setOnClickListener(this);
        ivSwith.setOnClickListener(this);
        ivTopBack.setOnClickListener(this);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    private void setDefaultEDT(boolean isDefault) {
        if (isDefault) {

            etPass.setFocusable(true);
            etPass.setFocusableInTouchMode(true);
            etName.setFocusable(true);
            etName.setFocusableInTouchMode(true);

            etName.setTextColor(getResources().getColor(R.color.color_black));
            etName.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_not_selected));

            etPass.setTextColor(getResources().getColor(R.color.color_black));
            etPass.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_not_selected));
        } else {

            etPass.setFocusable(false);
            etPass.setFocusableInTouchMode(false);
            etName.setFocusable(false);
            etName.setFocusableInTouchMode(false);


            etName.setTextColor(getResources().getColor(R.color.color_while));
            etName.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_editext_name));

            etPass.setTextColor(getResources().getColor(R.color.color_while));
            etPass.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_editext_name));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mobie_hostpot__tv_open:
                String nameHostpot = etName.getText().toString();
                String passHostpot = etPass.getText().toString();

                if (IS_ON_PASSWORD) {
                    if (nameHostpot.length() == 0 || passHostpot.length() == 0) {
                        Toast.makeText(HostpotWifiActivity.this, "Please input name hotspot or password !", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }else {
                    if (nameHostpot.length() == 0) {
                        Toast.makeText(HostpotWifiActivity.this, "Please input name hotspot or password !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (!IS_ENABLE_HOSTPOT) {
                    tvOpen.setText("Close");
                    tvOpen.setTextColor(getResources().getColor(R.color.text_close_hotspot));
                    tvOpen.setBackgroundDrawable(getResources().getDrawable(R.drawable.boder_bt_close_hostpot));

                    createWifiAccessPoint(nameHostpot, passHostpot);
                    SharedPreferences sp = getSharedPreferences("wifiHotspot", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(AppConstant.NAME_HOSTPOT_WIFI, nameHostpot);
                    editor.putString(AppConstant.PASS_HOSTPOT_WIFI, passHostpot);
                    editor.commit();

                    setMobileDataEnabled(true);
                    Toast.makeText(getApplicationContext(), "Hotspot Created", Toast.LENGTH_SHORT).show();
                        setDefaultEDT(false);
                    IS_ENABLE_HOSTPOT = true;


                } else {

                    tvOpen.setText("Open");
                    tvOpen.setTextColor(getResources().getColor(R.color.text_open_hotspot));
                    tvOpen.setBackgroundDrawable(getResources().getDrawable(R.drawable.boder_bt_open_hostpot));

                    offHotspotWifi();

                    setDefaultEDT(true);
                    IS_ENABLE_HOSTPOT = false;

                }
                 /*   try {
                        Thread.sleep(5000);
                        IS_RUN_HOSTPOT = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/


                break;
            case R.id.mobie_hostpot__iv_swith:

                if (!IS_ENABLE_HOSTPOT) {
                    if (IS_ON_PASSWORD) {
                        ivSwith.setImageResource(R.drawable.ic_swith_off);
                        IS_ON_PASSWORD = false;
                        etPass.setFocusable(false);
                        etPass.setFocusableInTouchMode(false);
                        etPass.setText("----------");
                        etPass.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_editext_name));
                    } else {
                        ivSwith.setImageResource(R.drawable.ic_swith_on);
                        IS_ON_PASSWORD = true;
                        etPass.setFocusable(true);
                        etPass.setFocusableInTouchMode(true);
                        etPass.setText("");
                        etPass.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_edt_not_selected));
                    }
                }
                break;
            case R.id.view_top_iv_back:
                finish();
                break;
        }
    }


    private void createWifiAccessPoint(String nameHostpot, String passHostPpot) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
        Method[] wmMethods = wifiManager.getClass().getDeclaredMethods();

        boolean methodFound = false;
        for (Method method : wmMethods) {
            if (method.getName().equals("setWifiApEnabled")) {
                methodFound = true;
                methodHostpot = method;

                netConfigHostpot = new WifiConfiguration();
                netConfigHostpot.SSID = nameHostpot;

                netConfigHostpot.preSharedKey = passHostPpot;

                netConfigHostpot.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

                netConfigHostpot.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED, true);

                try {
                    boolean apstatus = (Boolean) method.invoke(wifiManager, netConfigHostpot, true);
                    for (Method isWifiApEnabledmethod : wmMethods) {
                        if (isWifiApEnabledmethod.getName().equals("isWifiApEnabled")) {
                            while (!(Boolean) isWifiApEnabledmethod.invoke(wifiManager)) {
                            }
                            for (Method method1 : wmMethods) {
                                if (method1.getName().equals(
                                        "getWifiApState")) {
                                }
                            }
                        }
                    }
                    if (apstatus) {
                        Log.d("Splash Activity",
                                "Access Point created");
                    } else {
                        Log.d("Splash Activity",
                                "Access Point creation failed");
                    }

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!methodFound) {
            Log.d("Splash Activity",
                    "cannot configure an access point");

        }

    }

    private void offHotspotWifi() {
        try {
            methodHostpot.invoke(wifiManager, netConfigHostpot, false);
            setMobileDataEnabled(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setMobileDataEnabled(boolean enabled) {
        Log.i("NetworkUtil", "Mobile data enabling: " + enabled);
        boolean bool = true;
        final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Boolean.TYPE;
            Method enableDataMethod = ConnectivityManager.class.getMethod("setMobileDataEnabled", arrayOfClass);
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Boolean.valueOf(enabled);
            enableDataMethod.invoke(conman, arrayOfObject);

        } catch (Exception localException) {


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // TODO: What you want to do when it works or maybe .PERMISSION_DENIED if it works better
        }
    }

    public void settingPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
    }
}
