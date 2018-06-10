package com.binhdz.wifibooster.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.activity.BootNetwork;
import com.binhdz.wifibooster.activity.CkeckConnecActivity;
import com.binhdz.wifibooster.adapter.AdapterListWifi;
import com.binhdz.wifibooster.adapter.AdapterListWifiFree;
import com.binhdz.wifibooster.callback.OptionWifiListener;
import com.binhdz.wifibooster.callback.ShowBottomDialog;
import com.binhdz.wifibooster.controller.WifiConnector;
import com.binhdz.wifibooster.service.ShowNotiWarningBootsReciever;
import com.binhdz.wifibooster.util.AppConstant;
import com.binhdz.wifibooster.util.CommomUtil;
import com.binhdz.wifibooster.util.ItemClickSupport;
import com.binhdz.wifibooster.util.NetworkUtil;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.dialog.DialogBottomSheet;
import com.binhdz.wifibooster.view.dialog.DialogBottomSheetWifiFree;
import com.binhdz.wifibooster.view.dialog.DialogChangePassWifi;
import com.binhdz.wifibooster.view.dialog.DialogConnectWifi;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 1/6/2018.
 */

public class ListWifiFragment extends Fragment implements View.OnClickListener, OptionWifiListener, ShowBottomDialog {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final Handler handler = new Handler();
    //view
    private MyTextView tvWifiConnect, tvSpeedUp, tvSpeedDown, tvBootWifi;
    private TextView tvLableWifiFree, tvLableWifiPass, tvLableWifiNonePass;
    private LinearLayout mLySpeed, mLyTurnOnWifi;
    private NestedScrollView mLyContent;
    private RecyclerView rcvListWifi, rcvListWifiFree, rcvListWifiNonePass;
    private View rootView;
    //adapter
    private AdapterListWifi adapterListWifi, adtListWifiNonePass;
    private AdapterListWifiFree mAdapterListWifiFree;
    // manager system
    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    private NetworkStateReceiver mNetworkStateReceiver;
    //value
    private WifiInfo mWifiConnect;
    private List<WifiConfiguration> arrWifiConfig;
    private ArrayList<WifiConfiguration> arrWifiConfigEnable;
    private ArrayList<ScanResult> arrWifiFree;
    private ArrayList<ScanResult> arrWifiNonePass;
    private ArrayList<ScanResult> arrListWifi;
    private int isWifiConnected;
    private boolean isCanEnableWifi;
    private InterstitialAd mInterstitialAd;
    private DecimalFormat showFloatFormat = new DecimalFormat("0.0");
    private DecimalFormat byteFloatFormat = new DecimalFormat("0");
    // receiver update speed
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstant.ACTION_UPDATE_SPEED_DOWN)) {
                double speedDown = intent.getDoubleExtra(AppConstant.VALUE_SPEED_DOWN, 0);
                tvSpeedDown.setText(showSpeed(speedDown));
            }

            if (intent.getAction().equals(AppConstant.ACTION_UPDATE_SPEED_UP)) {
                double speedUp = intent.getDoubleExtra(AppConstant.VALUE_SPEED_UP, 0);
                tvSpeedUp.setText(showSpeed(speedUp));
            }
        }
    };
    private Runnable mRunnableScanWifi;
    private int positonWifiFree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_list_wifi, container, false);

        initView();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.ACTION_UPDATE_SPEED_DOWN);
        intentFilter.addAction(AppConstant.ACTION_UPDATE_SPEED_UP);

        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-9569615767688214/1204964831");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        setUp();

        return rootView;
    }

    private String showSpeed(double speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormat.format(speed / 1048576d) + "MB/s";
        } else if (speed >= 1024d) {
            speedString = showFloatFormat.format(speed / 1024d) + "KB/s";
        } else {
            speedString = byteFloatFormat.format(speed) + "B/s";
        }
        return speedString;
    }

    private void initData() {
        if (isWifiConnected == 1) {
            tvWifiConnect.setText(CommomUtil.getStr(mWifiConnect.getSSID()));
        } else if (isWifiConnected == 0) {
            tvWifiConnect.setText(getString(R.string.not_network_connected));
        }
    }

    private void setUp() {
        IntentFilter filterNetworkChange = new IntentFilter();
        filterNetworkChange.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filterNetworkChange.addAction("android.net.wifi.WIFI_STATE_CHANGED");


        if (checkAndRequestPermissions() == true) {

            mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            mNetworkStateReceiver = new NetworkStateReceiver();
            getActivity().registerReceiver(mNetworkStateReceiver, filterNetworkChange);

            receiverWifi = new WifiReceiver();
            getActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

            if (mainWifi.isWifiEnabled() == false) {
                mainWifi.setWifiEnabled(true);
            }

            mainWifi.startScan();
            doInback();

        }


    }

    @Override
    public void onResume() {

        super.onResume();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


    }

    @Override
    public void onPause() {

        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverWifi);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mainWifi.startScan();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, "Refresh");
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initView() {

        arrListWifi = new ArrayList<>();
        arrWifiConfig = new ArrayList<>();
        arrWifiNonePass = new ArrayList<>();
        arrWifiFree = new ArrayList<>();
        arrWifiConfigEnable = new ArrayList<>();

        tvBootWifi = (MyTextView) rootView.findViewById(R.id.fr_wifi__root_wifi);
        tvBootWifi.setOnClickListener(this);
        tvSpeedDown = (MyTextView) rootView.findViewById(R.id.fr_wifi__speed_down);
        tvSpeedUp = (MyTextView) rootView.findViewById(R.id.fr_wifi__speed_up);
        tvWifiConnect = (MyTextView) rootView.findViewById(R.id.fr_wifi_boot__conect);

        tvLableWifiFree = (TextView) rootView.findViewById(R.id.fr_wifi__tv_wifi_free);
        tvLableWifiPass = (TextView) rootView.findViewById(R.id.fr_wifi__tv_wifi_pass);
        tvLableWifiNonePass = (TextView) rootView.findViewById(R.id.fr_wifi__tv_wifi_none_pass);

        mLySpeed = (LinearLayout) rootView.findViewById(R.id.fr_wifi__ly_speed);
        mLyContent = (NestedScrollView) rootView.findViewById(R.id.fr_list_app__ly_content);
        mLyTurnOnWifi = (LinearLayout) rootView.findViewById(R.id.fr_wifi__turn_on_wifi);
        mLyTurnOnWifi.setOnClickListener(this);

        rcvListWifi = (RecyclerView) rootView.findViewById(R.id.fr_wifi__list_wifi);
        rcvListWifiFree = (RecyclerView) rootView.findViewById(R.id.fr_wifi__list_wifi_free);
        rcvListWifiNonePass = (RecyclerView) rootView.findViewById(R.id.fr_wifi__list_wifi_none_pass);

        // setup recycleview
        LinearLayoutManager layoutManagerPass = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerFree = new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManagerNonePass = new LinearLayoutManager(getActivity());

        adapterListWifi = new AdapterListWifi(getActivity());
        adtListWifiNonePass = new AdapterListWifi(getActivity());
        mAdapterListWifiFree = new AdapterListWifiFree(getActivity());
        mAdapterListWifiFree.setCallBack(this);

        rcvListWifi.setLayoutManager(layoutManagerPass);
        rcvListWifiFree.setLayoutManager(layoutManagerFree);
        rcvListWifiNonePass.setLayoutManager(layoutManagerNonePass);

        rcvListWifi.setAdapter(adapterListWifi);
        rcvListWifiFree.setAdapter(mAdapterListWifiFree);
        rcvListWifiNonePass.setAdapter(adtListWifiNonePass);
        setupRecycleview();

        isWifiConnected = 0;

    }

    private void setupRecycleview() {

        ItemClickSupport itemPass = new ItemClickSupport(rcvListWifi);
        itemPass.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v, RecyclerView.ViewHolder holder) {


                DialogConnectWifi dialogConnectWifi = new DialogConnectWifi(getActivity(), arrListWifi.get(position));

                dialogConnectWifi.show();


            }
        });

        itemPass.setOnLongClickListener(null);

        ItemClickSupport itemFree = new ItemClickSupport(rcvListWifiFree);
        itemFree.setOnItemClickListener(new ItemClickSupport.OnItemClickListener()

                                        {
                                            @Override
                                            public void onItemClicked(RecyclerView recyclerView, final int position, View
                                                    v, RecyclerView.ViewHolder holder) {
                                                Log.d("LIST_WIFI", "name  = " + mWifiConnect.getSSID());


                                            }
                                        }

        );

        itemFree.setOnLongClickListener(null);


        ItemClickSupport itemNonePass = new ItemClickSupport(rcvListWifiNonePass);
        itemNonePass.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v, RecyclerView.ViewHolder holder) {
                connectWifiNonePass(arrWifiNonePass.get(position));
            }
        });
        itemNonePass.setOnLongClickListener(null);
    }

    /*private WifiConfiguration getConfiguration(ArrayList<WifiConfiguration> arrWifiConfig, ScanResult mWifi) {
        for (WifiConfiguration wifiConfiguration : arrWifiConfig) {
            if (getStr(wifiConfiguration.SSID).equals(mWifi.SSID)) {
                return wifiConfiguration;

            }

        }
        return null;
    }*/
    // ckeck wifi da connect chua

    private void connectWifiFree(int position) {
        WifiConnector wifiConnector = new WifiConnector(arrWifiConfigEnable.get(position), getActivity());


        wifiConnector.connectToWifi(new WifiConnector.ConnectionResultListener() {
            @Override
            public void successfulConnect(String SSID) {
                mWifiConnect = mainWifi.getConnectionInfo();
                mAdapterListWifiFree.setUpListWifi(arrWifiFree, mWifiConnect);
                Log.d("LIST_WIFI", "name  = " + mWifiConnect.getSSID());

            }

            @Override
            public void errorConnect(int codeReason) {
                Toast.makeText(getActivity(), "Connect fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateChange(SupplicantState supplicantState) {

            }
        });
    }

    private void connectWifiNonePass(ScanResult mWifiNonePass) {
        WifiConnector connector = new WifiConnector(getActivity(), mWifiNonePass.SSID);
        connector.connectToWifi(new WifiConnector.ConnectionResultListener() {
            @Override
            public void successfulConnect(String SSID) {
                Toast.makeText(getActivity(), "Connect Successfull", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void errorConnect(int codeReason) {
                Toast.makeText(getActivity(), "Connect Error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStateChange(SupplicantState supplicantState) {
                // update UI!
            }
        });
    }

    private void updateListConfigEnable() {
        arrWifiConfigEnable.clear();
        arrWifiFree.clear();

        if (arrWifiConfig != null && arrListWifi != null) {
            for (int i = 0; i < arrWifiConfig.size(); i++) {
                for (int j = 0; j < arrListWifi.size(); j++) {
                    if (getStr(arrWifiConfig.get(i).SSID).equals(arrListWifi.get(j).SSID) && !arrListWifi.get(j).capabilities.equals("[ESS]")) {

                        arrWifiConfigEnable.add(arrWifiConfig.get(i));
                        arrWifiFree.add(arrListWifi.get(j));

                    }
                }

            }
        }
    }

    private void updateListWifiFree() {
        arrWifiNonePass.clear();

        for (int i = 0; i < arrListWifi.size(); i++) {
            if (arrListWifi.get(i).capabilities.equals("[ESS]")) {
                arrWifiNonePass.add(arrListWifi.get(i));
            }

        }
        Log.i("LIST_WIFI", "size  " + arrWifiNonePass.size());
        if (arrWifiFree.size() == 0) {
            tvLableWifiFree.setVisibility(View.GONE);
        } else {
            tvLableWifiFree.setVisibility(View.VISIBLE);
        }
        if (arrWifiNonePass.size() == 0) {
            tvLableWifiNonePass.setVisibility(View.GONE);
        } else {
            tvLableWifiNonePass.setVisibility(View.VISIBLE);
        }
    }

    private String getStr(String s) {
        StringBuilder str = new StringBuilder(s);
        str.deleteCharAt(s.length() - 1);
        str.deleteCharAt(0);
        return str.toString();
    }

    public void doInback() {
        mRunnableScanWifi = new Runnable() {
            @Override
            public void run() {
                mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                receiverWifi = new WifiReceiver();
                mainWifi.startScan();
                doInback();
            }
        };
        handler.postDelayed(mRunnableScanWifi, 5000);

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("PERMISSIONS", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "ACCESS_COARSE_LOCATION GRANTED");
                        //Initialize and save a reference to wifiManager.
                        mainWifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                        receiverWifi = new WifiReceiver();
                        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                        if (mainWifi.isWifiEnabled() == false) {
                            mainWifi.setWifiEnabled(true);

                        }
                        mainWifi.startScan();

                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            showDialogOK("COARSE_Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    getActivity().finish();

                                                    dialog.cancel();
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean checkAndRequestPermissions() {
        int permissionAcessCoarseLocation = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionAcessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fr_wifi__root_wifi:
                //setupNoti
                Intent mintent = new Intent(getActivity(), ShowNotiWarningBootsReciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, mintent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                try {
                    alarmManager.cancel(pendingIntent);
                } catch (Exception ex) {

                }
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);

                Intent intent = new Intent(getActivity(), BootNetwork.class);
                getActivity().startActivity(intent);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;
            case R.id.fr_wifi__turn_on_wifi:
              /*  mLyContent.setVisibility(View.VISIBLE);
                mLyTurnOnWifi.setVisibility(View.GONE);
                isWifiConnected = 1;
                isCanEnableWifi = true;

                arrWifiFree.clear();
                mAdapterListWifiFree.setUpListWifi(arrWifiFree, mWifiConnect);

                arrWifiNonePass.clear();
                adtListWifiNonePass.setUpListWifi(arrWifiNonePass, mWifiConnect, true);

                arrListWifi.clear();
                adapterListWifi.setUpListWifi(arrListWifi, mWifiConnect, false);*/

                //mainWifi.setWifiEnabled(true);

                break;
        }
    }

    @Override
    public void SafetyCkeck() {
        Intent intent = new Intent(getActivity(), CkeckConnecActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void ConnectTest() {
        Intent intent = new Intent(getActivity(), CkeckConnecActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void SpeedTest() {
        Intent intent = new Intent(getActivity(), CkeckConnecActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void WifiInfo() {

    }

    @Override
    public void Forget() {
        int networkId = mainWifi.getConnectionInfo().getNetworkId();
        mainWifi.removeNetwork(networkId);
        mainWifi.saveConfiguration();
        Toast.makeText(getActivity(), getActivity().getString(R.string.forget_pass_suscess), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Disconnect() {
        mainWifi.disconnect();
    }

    @Override
    public void otConncect() {
        connectWifiFree(positonWifiFree);
    }

    @Override
    public void otChangePass() {
        DialogChangePassWifi mDialogChangePassWifi = new DialogChangePassWifi(getActivity(), arrWifiConfigEnable.get(positonWifiFree));
        mDialogChangePassWifi.show();

    }

    @Override
    public void showDialogWifiFree(int position) {
        DialogBottomSheetWifiFree dialogBottomSheetWifiFree = new DialogBottomSheetWifiFree();
        dialogBottomSheetWifiFree.show(getActivity().getSupportFragmentManager(), dialogBottomSheetWifiFree.getTag());
        dialogBottomSheetWifiFree.setData(arrWifiFree.get(position), this);
        positonWifiFree = position;
    }

    @Override
    public void ClickContent(int position) {
        if (mWifiConnect.getSSID().equals(arrWifiConfigEnable.get(position).SSID)) {
            DialogBottomSheet bottomSheetDialogFragment = new DialogBottomSheet();

            //show it
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            bottomSheetDialogFragment.setData(arrWifiFree.get(position), ListWifiFragment.this);
        } else {
            connectWifiFree(position);
        }
    }

    @Override
    public void onDestroyView() {
        if (receiverWifi != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverWifi);
            Log.d("LIST_WIFI", "CANG VOAI DOAI");
        }
        if (mBroadcastReceiver != null) {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
        if (mNetworkStateReceiver != null) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mNetworkStateReceiver);

        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(mRunnableScanWifi);

        super.onDestroy();
    }

    private String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        String name = mTelephonyManager.getNetworkOperatorName();

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:

                return name + " 2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:

                return name + " 3G";
            case TelephonyManager.NETWORK_TYPE_LTE:

                return name + " 4G";
            default:
                return "Mobie data";
        }
    }

    class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int state = NetworkUtil.getConnectivityStatus(context);
            if (!mainWifi.isWifiEnabled()) {
                mLyContent.setVisibility(View.GONE);
                mLyTurnOnWifi.setVisibility(View.VISIBLE);
            } else {
                mLyContent.setVisibility(View.VISIBLE);
                mLyTurnOnWifi.setVisibility(View.GONE);
            }
            if (state == NetworkUtil.TYPE_NOT_CONNECTED) {
                isWifiConnected = 0;
                mLySpeed.setVisibility(View.GONE);

                if (receiverWifi != null) {

                    LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiverWifi);

                }
            } else if (state == NetworkUtil.TYPE_WIFI) {
                isCanEnableWifi = false;
                isWifiConnected = 1;
                mLySpeed.setVisibility(View.VISIBLE);


            } else if (state == NetworkUtil.TYPE_MOBILE) {
                isCanEnableWifi = false;
                isWifiConnected = 2;
                String typeMobie = getNetworkType(getActivity());
                tvWifiConnect.setText(typeMobie);
                mLySpeed.setVisibility(View.VISIBLE);
            }

        }
    }

    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {

            arrListWifi.clear();
            if (arrWifiConfig != null) {
                arrWifiConfig.clear();
            }

            arrListWifi.addAll(mainWifi.getScanResults());
            mWifiConnect = mainWifi.getConnectionInfo();
            arrWifiConfig = mainWifi.getConfiguredNetworks();

            updateListConfigEnable();
            updateListWifiFree();

            initData();

            mAdapterListWifiFree.setUpListWifi(arrWifiFree, mWifiConnect);

            arrListWifi.removeAll(arrWifiFree);
            arrListWifi.removeAll(arrWifiNonePass);
            if (arrListWifi.size() == 0) {
                tvLableWifiPass.setVisibility(View.GONE);
            } else {
                tvLableWifiPass.setVisibility(View.VISIBLE);
            }

            Collections.sort(arrListWifi, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult s1, ScanResult s2) {
                    if (s1.level < s2.level) {
                        return 1;
                    } else {
                        if (s1.level == s2.level) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }

                }
            });
            adapterListWifi.setUpListWifi(arrListWifi, mWifiConnect, false);

            Collections.sort(arrWifiNonePass, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult s1, ScanResult s2) {
                    if (s1.level < s2.level) {
                        return 1;
                    } else {
                        if (s1.level == s2.level) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }

                }
            });
            adtListWifiNonePass.setUpListWifi(arrWifiNonePass, mWifiConnect, true);

        }

    }
}
