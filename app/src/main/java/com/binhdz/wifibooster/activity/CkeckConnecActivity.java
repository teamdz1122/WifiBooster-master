package com.binhdz.wifibooster.activity;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.fragment.LoadAdFragment;
import com.binhdz.wifibooster.view.MyTextView;

/**
 * Created by admin on 1/6/2018.
 */

public class CkeckConnecActivity extends AppCompatActivity implements View.OnClickListener {
    Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            ivCkeckConnectScan.animate().rotationBy(360).withEndAction(this).setDuration(2000).setInterpolator(new LinearInterpolator()).start();
        }
    };
    Runnable runnableIc3 = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            icLoading_3.setImageResource(R.drawable.ic_loading_complete);
            tvScan_3.setTextColor(getResources().getColor(R.color.color_while_blur));

            icLoading_4.setVisibility(View.VISIBLE);
            icLoading_4.animate().rotationBy(360).withEndAction(runnableIc4).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
            tvScan_4.setTextColor(getResources().getColor(R.color.color_while));
        }
    };
    Runnable runnableIc2 = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            icLoading_2.setImageResource(R.drawable.ic_loading_complete);
            tvScan_2.setTextColor(getResources().getColor(R.color.color_while_blur));

            icLoading_3.setVisibility(View.VISIBLE);
            icLoading_3.animate().rotationBy(360).withEndAction(runnableIc3).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
            tvScan_3.setTextColor(getResources().getColor(R.color.color_while));
        }
    };
    private ImageView ivBack, ivCkeckConnectScan, ivBgConnect, icLoading_1, icLoading_2, icLoading_3, icLoading_4;
    private LinearLayout lyBottom, lyCheckComplete;
    private RelativeLayout lyTop;
    private MyTextView tvScan_1, tvScan_2, tvScan_3, tvScan_4;
    Runnable runnableIc1 = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            icLoading_1.setImageResource(R.drawable.ic_loading_complete);
            tvScan_1.setTextColor(getResources().getColor(R.color.color_while_blur));

            icLoading_2.setVisibility(View.VISIBLE);
            icLoading_2.animate().rotationBy(360).withEndAction(runnableIc2).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
            tvScan_2.setTextColor(getResources().getColor(R.color.color_while));
        }
    };
    private boolean isShowAd;
    private boolean isSafeTest = false;
    private boolean isDone = false;
    Runnable runnableIc4 = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            icLoading_4.setImageResource(R.drawable.ic_loading_complete);
            tvScan_4.setTextColor(getResources().getColor(R.color.color_while_blur));

            ivBgConnect.setImageResource(R.drawable.ic_bg_ckeck_safe);
            isSafeTest = true;
            if (isDone && !isShowAd) {
                lyTop.setVisibility(View.GONE);
                lyBottom.setVisibility(View.GONE);
                lyCheckComplete.setVisibility(View.VISIBLE);
                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.bottom_up);
                lyCheckComplete.startAnimation(slide_down);


                icLoading_4.animate().cancel();
                icLoading_3.animate().cancel();
                icLoading_2.animate().cancel();
                icLoading_1.animate().cancel();
                ivCkeckConnectScan.animate().cancel();
                isShowAd = true;

            }
            if (isSafeTest) {
                icLoading_1.setImageResource(R.drawable.ic_loading_ckeck);
                icLoading_1.setVisibility(View.GONE);
                icLoading_2.setImageResource(R.drawable.ic_loading_ckeck);
                icLoading_2.setVisibility(View.GONE);
                icLoading_3.setImageResource(R.drawable.ic_loading_ckeck);
                icLoading_3.setVisibility(View.GONE);
                icLoading_4.setImageResource(R.drawable.ic_loading_ckeck);
                icLoading_4.setVisibility(View.GONE);

                tvScan_1.setText(getString(R.string.ckeck_safe_des_1));
                tvScan_2.setText(getString(R.string.ckeck_safe_des_2));
                tvScan_3.setText(getString(R.string.ckeck_safe_des_3));
                tvScan_4.setText(getString(R.string.ckeck_safe_des_4));
                icLoading_1.setVisibility(View.VISIBLE);
                icLoading_1.animate().rotationBy(360).withEndAction(runnableIc1).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
                isDone = true;
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_connect);

        initView();
        setUpViews();

        LoadAdFragment mLoadAdFragment = new LoadAdFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.ckeck_connect__ly_ad, mLoadAdFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        icLoading_1.setVisibility(View.VISIBLE);
        icLoading_1.animate().rotationBy(360).withEndAction(runnableIc1).setDuration(1000).setInterpolator(new LinearInterpolator()).start();
        tvScan_1.setTextColor(getResources().getColor(R.color.color_while));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setUpViews() {
        ivCkeckConnectScan.animate().rotationBy(360).withEndAction(runnable).setDuration(2000).setInterpolator(new LinearInterpolator()).start();

    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.check_connect__iv_back);
        ivBack.setOnClickListener(this);
        ivBgConnect = (ImageView) findViewById(R.id.ckeck_connect__iv_bg_connect);
        ivCkeckConnectScan = (ImageView) findViewById(R.id.ckeck_connect__iv_scan);

        icLoading_1 = (ImageView) findViewById(R.id.ckeck_connect__iv_loading_1);
        icLoading_2 = (ImageView) findViewById(R.id.ckeck_connect__iv_loading_2);
        icLoading_3 = (ImageView) findViewById(R.id.ckeck_connect__iv_loading_3);
        icLoading_4 = (ImageView) findViewById(R.id.ckeck_connect__iv_loading_4);

        tvScan_1 = (MyTextView) findViewById(R.id.ckeck_connect__tv_scanning_1);
        tvScan_2 = (MyTextView) findViewById(R.id.ckeck_connect__tv_scanning_2);
        tvScan_3 = (MyTextView) findViewById(R.id.ckeck_connect__tv_scanning_3);
        tvScan_4 = (MyTextView) findViewById(R.id.ckeck_connect__tv_scanning_4);

        lyTop = (RelativeLayout) findViewById(R.id.ckeck_connect__ly_top_scan);
        lyCheckComplete = (LinearLayout) findViewById(R.id.ckeck_connect__ln_boot_complete);
        lyBottom = (LinearLayout) findViewById(R.id.ckeck_connect__ln_bottom_scan);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_connect__iv_back:
                    finish();
                break;

        }
    }
}
