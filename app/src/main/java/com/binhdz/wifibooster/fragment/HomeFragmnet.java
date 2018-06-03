package com.binhdz.wifibooster.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.activity.BootNetwork;
import com.binhdz.wifibooster.activity.CkeckConnecActivity;
import com.binhdz.wifibooster.activity.HostpotWifiActivity;
import com.binhdz.wifibooster.activity.RealTimeProcess;
import com.binhdz.wifibooster.callback.ChangeTabViewpager;
import com.binhdz.wifibooster.util.CommomUtil;
import com.binhdz.wifibooster.util.NetworkUtil;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.MyTextViewBold;
import com.binhdz.wifibooster.view.homeview.RadarDrawable;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by admin on 1/6/2018.
 */

public class HomeFragmnet extends Fragment implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float SHOW_RADA = 0.01f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 300;
    RadarDrawable radarDrawable;
    private View rootView;
    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    private boolean isNotNetwork = false;
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private WifiManager mWifiManager;
    private Toolbar mToolbar;
    private RelativeLayout lyHostpotWifi, lyRealTimeProcess, lyBootsNet;
    private ImageView ivNenWifi, ivWifiState;
    private ImageView mRadarImage;
    private MyTextView tvNameWifiConnect;
    private MyTextViewBold tvOpenNetwork;
    private NetworkStateReceiver mNetworkStateReceiver;
    private ChangeTabViewpager mChangeTabViewpager;
    private NativeExpressAdView mContainerAd;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private InterstitialAd mInterstitialAd;


    public static HomeFragmnet newInstance(String text) {
        HomeFragmnet f = new HomeFragmnet();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initViews();
        setUpAdView();
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        mRadarImage = (ImageView) rootView.findViewById(R.id.radar);

        radarDrawable = new RadarDrawable(getActivity());
        radarDrawable.setMinRadius(Math.min(mRadarImage.getWidth(), mRadarImage.getHeight()) / 2);
        radarDrawable.start();
        mRadarImage.setImageDrawable(radarDrawable);

        setupData();

        return rootView;
    }

    public void setUpAdView() {
        final NativeExpressAdView mAdView = new NativeExpressAdView(getActivity().getApplicationContext());
        final AdRequest request = new AdRequest.Builder().build();
        mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 250));
        mAdView.setAdUnitId("ca-app-pub-9569615767688214/5083951357");
        mContainerAd.addView(mAdView);
        mAdView.loadAd(request);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d("MAIN", "Close");
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
                Log.d("MAIN", "LOAD Fail");

            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mContainerAd.setVisibility(View.VISIBLE);
                Log.d("MAIN", "LOADED");

            }
        });

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
    }

    private void setupData() {

        IntentFilter filterNetworkChange = new IntentFilter();
        filterNetworkChange.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filterNetworkChange.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mNetworkStateReceiver = new NetworkStateReceiver();
        getActivity().registerReceiver(mNetworkStateReceiver, filterNetworkChange);

        mWifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

    }

    public void setCallBack(ChangeTabViewpager mChangeTabViewpager) {
        this.mChangeTabViewpager = mChangeTabViewpager;

    }

    private void initViews() {
        mContainerAd = (NativeExpressAdView) rootView.findViewById(R.id.fr_home__adview);
        mToolbar = (Toolbar) rootView.findViewById(R.id.main_toolbar);
        mTitle = (TextView) rootView.findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) rootView.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) rootView.findViewById(R.id.main_appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.main_collapsing);
        mAppBarLayout.setExpanded(true);


        ivNenWifi = (ImageView) rootView.findViewById(R.id.fr_home__iv_nen_wifi);

        lyHostpotWifi = (RelativeLayout) rootView.findViewById(R.id.fr_home__ly_hostpot);
        lyHostpotWifi.setOnClickListener(this);

        lyRealTimeProcess = (RelativeLayout) rootView.findViewById(R.id.fr_home__ly_real_process);
        lyRealTimeProcess.setOnClickListener(this);

        lyBootsNet = (RelativeLayout) rootView.findViewById(R.id.fr_home__ly_boots_net);
        lyBootsNet.setOnClickListener(this);

        ivWifiState = (ImageView) rootView.findViewById(R.id.fr_home__iv_state_wifi);
        tvNameWifiConnect = (MyTextView) rootView.findViewById(R.id.fr_home__tv_name_wifi);
        tvOpenNetwork = (MyTextViewBold) rootView.findViewById(R.id.fr_home__tv_ckeck_again);
        tvOpenNetwork.setOnClickListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;

            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;

            }
        }
        if (percentage >= SHOW_RADA) {
            mRadarImage.setVisibility(View.GONE);
            ivNenWifi.setVisibility(View.GONE);

        } else {
            mRadarImage.setVisibility(View.VISIBLE);
            ivNenWifi.setVisibility(View.VISIBLE);
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.fr_home__ly_hostpot:
                intent = new Intent(getActivity(), RealTimeProcess.class);
                getActivity().startActivity(intent);

                break;
            case R.id.fr_home__ly_real_process:
                intent = new Intent(getActivity(), HostpotWifiActivity.class);
                getActivity().startActivity(intent);

                break;
            case R.id.fr_home__tv_ckeck_again:
                if (isNotNetwork) {
                    mChangeTabViewpager.changeTab(1);

                } else {
                    intent = new Intent(getActivity(), CkeckConnecActivity.class);
                    getActivity().startActivity(intent);
                }
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
                break;

            case R.id.fr_home__ly_boots_net:

                intent = new Intent(getActivity(), BootNetwork.class);
                getActivity().startActivity(intent);

                break;

        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNetworkStateReceiver != null) {
            getActivity().unregisterReceiver(mNetworkStateReceiver);
        }
    }

    class NetworkStateReceiver extends BroadcastReceiver {

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = NetworkUtil.getConnectivityStatus(context);
            if (state == NetworkUtil.TYPE_NOT_CONNECTED) {
                isNotNetwork = true;
                tvNameWifiConnect.setText(getString(R.string.not_network_connected));
                ivWifiState.setImageResource(R.drawable.ic_home_state_not_connected);
                tvOpenNetwork.setText(getActivity().getString(R.string.opent_network));
                tvOpenNetwork.setBackground(getResources().getDrawable(R.drawable.bg_tv_open_network));

            } else if (state == NetworkUtil.TYPE_MOBILE) {
                isNotNetwork = false;
                String typeMobie = getNetworkType(getActivity());
                tvNameWifiConnect.setText(typeMobie);

                ivWifiState.setImageResource(R.drawable.ic_home_state_mobie);
                tvOpenNetwork.setText(typeMobie.substring(typeMobie.length() - 2, typeMobie.length()) + " TEST");
                tvOpenNetwork.setBackground(getResources().getDrawable(R.drawable.border_button_ckeck_again));

            } else if (state == NetworkUtil.TYPE_WIFI) {
                isNotNetwork = false;
                ivWifiState.setImageResource(R.drawable.ic_home_wifi);
                WifiInfo mWifiConnect = mWifiManager.getConnectionInfo();
                tvNameWifiConnect.setText(CommomUtil.getStr(mWifiConnect.getSSID()) + " is safe");

                tvOpenNetwork.setText(getActivity().getString(R.string.ckeck_again));
                tvOpenNetwork.setBackground(getResources().getDrawable(R.drawable.border_button_ckeck_again));
            }


        }
    }
}
