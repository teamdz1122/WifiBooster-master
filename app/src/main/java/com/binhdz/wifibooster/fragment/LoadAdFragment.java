package com.binhdz.wifibooster.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.view.MyTextView;
import com.binhdz.wifibooster.view.MyTextViewBold;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by admin on 1/6/2018.
 */

public class LoadAdFragment extends Fragment {
    private View rootView;
    private LinearLayout lyTop, lyBottom;
    private MyTextViewBold tvTitle;
    private MyTextView tvContent;
    private ImageView mIcon;
    private NativeExpressAdView mContainerAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_show_ad, container, false);
        initViews();
        setUpData();
        return rootView;
    }

    public void setUpData() {
        NativeExpressAdView mAdView = new NativeExpressAdView(getActivity().getApplicationContext());
        AdRequest request = new AdRequest.Builder().build();
        mAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 400));
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
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.d("MAIN", "LOAD Fail");
                mIcon.setImageResource(R.drawable.ic_check_connect_fail);
                tvTitle.setText(getActivity().getString(R.string.check_connect_fail));
                tvContent.setText(getActivity().getString(R.string.ckeck_conenct_fail_des));
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("MAIN", "LOADED");
                mIcon.setImageResource(R.drawable.ic_boot_complete);
                tvTitle.setText(getActivity().getString(R.string.check_connect_suscess));
                tvContent.setText(getActivity().getString(R.string.check_connect_sucsess_des));
            }
        });
    }

    private void initViews() {
        tvTitle = (MyTextViewBold) rootView.findViewById(R.id.ly_ad__tv_title);
        tvContent = (MyTextView) rootView.findViewById(R.id.ly_ad__tv_content);

        lyTop = (LinearLayout) rootView.findViewById(R.id.ly_ad_top);
        lyBottom = (LinearLayout) rootView.findViewById(R.id.ly_ad__native);

        mIcon = (ImageView) rootView.findViewById(R.id.ly_ad__icon);

        mContainerAd = (NativeExpressAdView) rootView.findViewById(R.id.ly_ad__adview);

    }
}
