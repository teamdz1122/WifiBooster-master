package com.binhdz.wifibooster.view.dialog;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.controller.WifiConnector;
import com.binhdz.wifibooster.util.CommomUtil;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.MyTextViewBold;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by admin on 1/5/2018.
 */

public class DialogConnectWifi extends AlertDialog implements View.OnClickListener {
    private String TAG = "DialogConnectWifi";
    private MyTextView tvMahoa, tvTanSo, tvMac, tvBeforeConect, tvName;
    private EditText etPassword;
    private MyTextViewBold tvCance, tvConnect;
    private CheckBox cbShowPass;


    private View rootView;
    private Context mContext;
    private ScanResult mMyWifi;

    public DialogConnectWifi(Context context, ScanResult myWifi) {
        super(context);
        mContext = context;
        mMyWifi = myWifi;
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_connect_wifi, null);
        setView(rootView);
        initViews();
        initData();
    }
    private DecimalFormat showFloatFormat = new DecimalFormat("0.0");
    private void initData() {

        tvMahoa.setText(mContext.getString(R.string.encryption)+ " "+CommomUtil.getWifiSecurityType(mMyWifi));
        tvName.setText(mMyWifi.SSID);
        tvTanSo.setText(mContext.getString(R.string.signal_strength) + " "+Math.abs(mMyWifi.level)+"%" + " , "+mContext.getString(R.string.frequency) +" "+ (showFloatFormat.format(mMyWifi.frequency / 1000d)) + "GHZ");
        if (isBefoConnect()) {
            tvBeforeConect.setText(mContext.getString(R.string.wifi_safe));
        } else {
            tvBeforeConect.setText(mContext.getString(R.string.wifi_never_connected));
        }
        tvMac.setText("Mac : "+mMyWifi.BSSID);

    }

    private boolean isBefoConnect() {
        List<WifiConfiguration> arrWifiConfig;
        WifiManager mainWifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        arrWifiConfig = mainWifi.getConfiguredNetworks();
        for (int i = 0; i < arrWifiConfig.size(); i++) {
            Log.i("DIALOG", "SSID = " + CommomUtil.getStr(arrWifiConfig.get(i).SSID));
            if (CommomUtil.getStr(arrWifiConfig.get(i).SSID).equals(mMyWifi.SSID)) {
                return true;
            }
        }
        return false;
    }

    private void initViews() {
        tvName = (MyTextView) rootView.findViewById(R.id.dialog_wifi__tv_name_wifi);
        tvMahoa = (MyTextView) rootView.findViewById(R.id.dialog_wifi__tv_mahoa);
        tvTanSo = (MyTextView) rootView.findViewById(R.id.dialog_wifi__tv_frequency);
        tvMac = (MyTextView) rootView.findViewById(R.id.dialog_wifi__tv_mac);
        tvBeforeConect = (MyTextView) rootView.findViewById(R.id.dialog_wifi__tv_connect_before);

        etPassword = (EditText) rootView.findViewById(R.id.dialog_wifi__et_password);
        cbShowPass = (CheckBox) rootView.findViewById(R.id.dialog_wifi__cb_show_pass);

        tvCance = (MyTextViewBold) rootView.findViewById(R.id.dialog_wifi__tv_cancel);
        tvConnect = (MyTextViewBold) rootView.findViewById(R.id.dialog_wifi__tv_connect);

        tvConnect.setOnClickListener(this);
        tvCance.setOnClickListener(this);

        if(!cbShowPass.isChecked()){
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!cbShowPass.isChecked()){
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_wifi__tv_cancel:
                cancel();
                break;
            case R.id.dialog_wifi__tv_connect:
                connectWifi();
                cancel();
                break;
        }
    }

    private void connectWifi() {
        WifiConnector connector = new WifiConnector(mContext, mMyWifi.SSID, mMyWifi.BSSID, CommomUtil.getWifiSecurityType(mMyWifi), etPassword.getText().toString());
        connector.connectToWifi(new WifiConnector.ConnectionResultListener() {
            @Override
            public void successfulConnect(String SSID) {
                String state = mContext.getString(R.string.state_connect_success);
                Toast.makeText(mContext, state, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void errorConnect(int codeReason) {
                String state = mContext.getString(R.string.state_connect_error);
                Toast.makeText(mContext, state, Toast.LENGTH_SHORT).show();
                DialogConnectWifi dialogConnectWifi = new DialogConnectWifi(mContext,mMyWifi);
                dialogConnectWifi.show();
            }

            @Override
            public void onStateChange(SupplicantState supplicantState) {
                // update UI!
            }
        });

    }

}
