package com.binhdz.wifibooster.view.dialog;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.MyTextViewBold;

/**
 * Created by admin on 1/5/2018.
 */

public class DialogChangePassWifi extends AlertDialog implements View.OnClickListener {
    private View rootView;
    private Context mContext;

    private MyTextView tvNameWifi;
    private EditText etPassword;
    private MyTextViewBold tvCance, tvConnect;
    private CheckBox cbShowPass;
    private WifiConfiguration mWifiConfiguration;

    public DialogChangePassWifi(Context context, WifiConfiguration mWifiConfiguration) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_change_password, null);
        setView(rootView);
        mContext = context;
        this.mWifiConfiguration = mWifiConfiguration;
        initViews();
    }

    private void initViews() {
        tvNameWifi = (MyTextView) rootView.findViewById(R.id.dl_change_pass__tv_name_wifi);
        tvConnect = (MyTextViewBold) rootView.findViewById(R.id.dl_change_pass____tv_connect);
        tvCance = (MyTextViewBold) rootView.findViewById(R.id.dl_change_pass____tv_cancel);
        etPassword = (EditText) rootView.findViewById(R.id.dl_change_pass____et_password);
        cbShowPass = (CheckBox)rootView.findViewById(R.id.dl_change_pass____cb_show_pass);
        tvConnect.setOnClickListener(this);
        tvCance.setOnClickListener(this);


        if (!cbShowPass.isChecked()) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        tvNameWifi.setText(mWifiConfiguration.SSID);
        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!cbShowPass.isChecked()) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dl_change_pass____tv_cancel:
                cancel();
                break;

            case R.id.dl_change_pass____tv_connect:

                mWifiConfiguration.preSharedKey = "\"" + etPassword.getText().toString() + "\"";
                addNetwork(mWifiConfiguration);
                Toast.makeText(mContext, "Pass changed", Toast.LENGTH_SHORT).show();
                cancel();
                break;
                default:
                    break;
        }
    }

    public boolean addNetwork(WifiConfiguration wcg) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (wcg == null) {
            return false;
        }

        int wcgID = wifiManager.addNetwork(wcg);
        if (-1 == wcgID) {
            return false;
        }
        boolean b = wifiManager.enableNetwork(wcgID, true);
        wifiManager.saveConfiguration();
        return b;
    }
}
