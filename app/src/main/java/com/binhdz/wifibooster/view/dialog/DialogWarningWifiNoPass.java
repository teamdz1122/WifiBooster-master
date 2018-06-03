package com.binhdz.wifibooster.view.dialog;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.MyTextViewBold;

/**
 * Created by admin on 1/5/2018.
 */

public class DialogWarningWifiNoPass extends AlertDialog implements View.OnClickListener {
    private String TAG = "DialogWarningWifiNoPass";
    private MyTextView tvMahoa, tvTanSo, tvMac, tvBeforeConect, tvName;
    private MyTextViewBold tvCance, tvConnect;

    private View rootView;
    private Context mContext;
    private ScanResult mMyWifi;

    public DialogWarningWifiNoPass(Context context, ScanResult myWifi) {
        super(context);
        mContext = context;
        mMyWifi = myWifi;
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_connect_wifi, null);
        setView(rootView);
        initViews();
        initData();
    }

    private void initViews() {

    }

    private void initData() {

    }

    @Override
    public void onClick(View view) {

    }
}