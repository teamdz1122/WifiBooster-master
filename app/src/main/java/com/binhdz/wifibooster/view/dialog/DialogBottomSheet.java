package com.binhdz.wifibooster.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.callback.OptionWifiListener;
import com.binhdz.wifibooster.view.MyTextViewBold;

/**
 * Created by admin on 1/5/2018.
 */

public class DialogBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    private ScanResult mWifiConnected;

    private MyTextViewBold tvNameWifi, tvCancel;
    private LinearLayout lyCkeckWifi, lyConnecTest, lySpeedTest, lyWifiInfo, lyForget, lyDisconnect;

    private OptionWifiListener mOptionWifi;

    public void setData(ScanResult mWifiConnected, OptionWifiListener mOptionWifi) {
        this.mWifiConnected = mWifiConnected;
        this.mOptionWifi = mOptionWifi;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView(View contentView) {
        tvCancel = (MyTextViewBold) contentView.findViewById(R.id.bottom_dialog__tv_cancel);
        tvNameWifi = (MyTextViewBold) contentView.findViewById(R.id.bottom_dialog__tv_name_wifi);
        tvCancel.setOnClickListener(this);

        lyCkeckWifi = (LinearLayout) contentView.findViewById(R.id.bottom_dialog__ly_ckeck_wifi);
        lyConnecTest = (LinearLayout) contentView.findViewById(R.id.bottom_dialog__ly_connect_test);
        lyDisconnect = (LinearLayout) contentView.findViewById(R.id.bottom_dialog__ly_disconnect);
        lyForget = (LinearLayout) contentView.findViewById(R.id.bottom_dialog__ly_forget);
        lySpeedTest = (LinearLayout) contentView.findViewById(R.id.bottom_dialog__ly_speed_test);
        lyWifiInfo = (LinearLayout) contentView.findViewById(R.id.bottom_dialog__ly_info);

        lyCkeckWifi.setOnClickListener(this);
        lyWifiInfo.setOnClickListener(this);
        lySpeedTest.setOnClickListener(this);
        lyForget.setOnClickListener(this);
        lyDisconnect.setOnClickListener(this);
        lyConnecTest.setOnClickListener(this);

        tvNameWifi.setText(mWifiConnected.SSID);

    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_dialog_wifi_connect, null);
        dialog.setContentView(contentView);

        initView(contentView);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_dialog__ly_ckeck_wifi:
                mOptionWifi.SafetyCkeck();
                dismiss();
                break;

            case R.id.bottom_dialog__ly_connect_test:
                mOptionWifi.ConnectTest();
                dismiss();
                break;

            case R.id.bottom_dialog__ly_disconnect:
                mOptionWifi.Disconnect();
                dismiss();
                break;

            case R.id.bottom_dialog__ly_forget:
                mOptionWifi.Forget();
                dismiss();
                break;

            case R.id.bottom_dialog__ly_info:
                mOptionWifi.WifiInfo();
                dismiss();
                break;

            case R.id.bottom_dialog__ly_speed_test:
                mOptionWifi.SpeedTest();
                dismiss();
                break;

            case R.id.bottom_dialog__tv_cancel:
                dismiss();

                break;
        }
    }
}
