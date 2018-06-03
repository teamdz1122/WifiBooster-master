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

public class DialogBottomSheetWifiFree extends BottomSheetDialogFragment implements View.OnClickListener {
    private ScanResult mWifiFree;

    private MyTextViewBold tvNameWifi, tvCancel;
    private LinearLayout lyConnectWifi, lyForget, lyChangePass;

    private OptionWifiListener mOptionWifi;

    public void setData(ScanResult mWifiFree, OptionWifiListener mOptionWifi) {
        this.mWifiFree = mWifiFree;
        this.mOptionWifi = mOptionWifi;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initView(View contentView) {
        tvCancel = (MyTextViewBold) contentView.findViewById(R.id.btdl_wifi_free__tv_cancel);
        tvNameWifi = (MyTextViewBold) contentView.findViewById(R.id.btdl_wifi_free__tv_name_wifi);
        tvCancel.setOnClickListener(this);

        lyConnectWifi = (LinearLayout) contentView.findViewById(R.id.btdl_wifi_free__ly_concect);
        lyChangePass = (LinearLayout) contentView.findViewById(R.id.btdl_wifi_free__ly_change_pass);
        lyForget = (LinearLayout) contentView.findViewById(R.id.btdl_wifi_free__ly_forget);

        lyForget.setOnClickListener(this);
        lyConnectWifi.setOnClickListener(this);
        lyChangePass.setOnClickListener(this);

        tvNameWifi.setText(mWifiFree.SSID);

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
        View contentView = View.inflate(getContext(), R.layout.bottom_dialog_wifi_free, null);
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
            case R.id.btdl_wifi_free__ly_change_pass:
                mOptionWifi.otChangePass();
                dismiss();
                break;

            case R.id.btdl_wifi_free__ly_forget:
                mOptionWifi.Forget();
                dismiss();
                break;

            case R.id.btdl_wifi_free__ly_concect:
                mOptionWifi.otChangePass();
                dismiss();
                break;

            case R.id.bottom_dialog__tv_cancel:
                dismiss();

                break;
        }
    }
}
