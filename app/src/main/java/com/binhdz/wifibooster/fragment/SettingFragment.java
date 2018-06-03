package com.binhdz.wifibooster.fragment;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.util.AppConstant;
import com.binhdz.wifibooster.util.PreferenceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by admin on 1/6/2018.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private InterstitialAd mInterstitialAd;

    Dialog dialogConfirm;
    private ImageView ivStatusNoti, ivScrenlock, ivMobiData, ivFreeWifi, ivRate;
    private LinearLayout lySendFeedBack;
    private View rootView;
    private boolean isOnStatusNoti, isOnScrenLock, isOnMobiData, isOnWifiFree;
    private PreferenceUtil mPreferenceUtil;

    // private boolean isLighOn = false;


    //private Camera camera;
    // private boolean isFlashOn;
    // private boolean hasFlash;
    //Camera.Parameters params;
    // MediaPlayer mp;

    // getting camera parameters

//    @SuppressLint("LongLogTag")
//    private void getCamera() {
//        if (camera == null) {
//            try {
//                camera = Camera.open();
//                params = camera.getParameters();
//            } catch (RuntimeException e) {
//                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
//            }
//        }
//    }
//     private void turnOnFlash() {
//        if (!isFlashOn) {
//            if (camera == null || params == null) {
//                return;
//            }
//
//            params = camera.getParameters();
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            camera.setParameters(params);
//            camera.startPreview();
//            isFlashOn = true;
//
//        }
//
//    }

//    private void turnOffFlash() {
//        if (isFlashOn) {
//            if (camera == null || params == null) {
//                return;
//            }
//
//            params = camera.getParameters();
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            camera.setParameters(params);
//            camera.stopPreview();
//            isFlashOn = false;
//
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        // on pause turn off the flash
//        turnOffFlash();
//    }

//    @Override
//    public void onRestart() {
//        super.onRestart();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // on resume turn on the flash
//        if(hasFlash)
//            turnOnFlash();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // on starting the app get the camera params
//        getCamera();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // on stop release the camera
//        if (camera != null) {
//            camera.release();
//            camera = null;
//        }
//    }

    public SettingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_setting, container, false);
        initViews();
        return rootView;

    }

    private void initViews() {
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-8694555122491369/5626855412");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        mPreferenceUtil = PreferenceUtil.getSingleton(getActivity());
        isOnMobiData = mPreferenceUtil.getBoolean(AppConstant.IS_ON_MOBI_DATA, true);
        isOnWifiFree = mPreferenceUtil.getBoolean(AppConstant.IS_ON_WIFI_FREE, true);
        isOnStatusNoti = mPreferenceUtil.getBoolean(AppConstant.IS_ON_STATUS_NOTI, true);
        isOnScrenLock = mPreferenceUtil.getBoolean(AppConstant.IS_ON_SCREEN_LOOK, true);

        ivFreeWifi = (ImageView) rootView.findViewById(R.id.fr_setting__iv_scren_free_wifi);
        ivMobiData = (ImageView) rootView.findViewById(R.id.fr_setting__iv_scren_mobie_data);
        ivRate = (ImageView) rootView.findViewById(R.id.fr_setting__iv_rate);
        ivStatusNoti = (ImageView) rootView.findViewById(R.id.fr_setting__iv_notification);
        ivScrenlock = (ImageView) rootView.findViewById(R.id.fr_setting__iv_scren_look);

        if (isOnMobiData) {
            ivMobiData.setImageResource(R.drawable.ic_setting_swith_on);
        } else {
            ivMobiData.setImageResource(R.drawable.ic_setting_swith_off);
        }
        if (isOnScrenLock) {
            ivScrenlock.setImageResource(R.drawable.ic_setting_swith_on);
        } else {
            ivScrenlock.setImageResource(R.drawable.ic_setting_swith_off);
        }
        if (isOnWifiFree) {
            ivFreeWifi.setImageResource(R.drawable.ic_setting_swith_on);
        } else {
            ivFreeWifi.setImageResource(R.drawable.ic_setting_swith_off);
        }
        if (isOnStatusNoti) {
            ivStatusNoti.setImageResource(R.drawable.ic_setting_swith_on);
        } else {
            ivStatusNoti.setImageResource(R.drawable.ic_setting_swith_off);
        }
        lySendFeedBack = (LinearLayout) rootView.findViewById(R.id.fr_setting_rating_google);

        ivFreeWifi.setOnClickListener(this);
        ivMobiData.setOnClickListener(this);
        ivRate.setOnClickListener(this);
        ivStatusNoti.setOnClickListener(this);
        ivScrenlock.setOnClickListener(this);
        lySendFeedBack.setOnClickListener(this);

    }

    private void initConfirm() {
        dialogConfirm = new Dialog(getContext());
        dialogConfirm.setContentView(R.layout.dialog_custom);

        TextView ms = (TextView) dialogConfirm.findViewById(R.id.message_rate);
        Button btn_later = (Button) dialogConfirm.findViewById(R.id.btn_later);
        Button btn_rate = (Button) dialogConfirm.findViewById(R.id.btn_rate);
        dialogConfirm.setCanceledOnTouchOutside(false);
        dialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirm.dismiss();
            }
        });
        btn_rate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));

                }
                dialogConfirm.dismiss();
            }
        });
        dialogConfirm.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fr_setting__iv_rate:
                initConfirm();
                break;

            case R.id.fr_setting__iv_notification:
                if (isOnStatusNoti) {
                    isOnStatusNoti = false;
                    ivStatusNoti.setImageResource(R.drawable.ic_setting_swith_off);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_STATUS_NOTI, false);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_off_status), Toast.LENGTH_SHORT).show();
                } else {
                    isOnMobiData = true;
                    ivStatusNoti.setImageResource(R.drawable.ic_setting_swith_on);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_STATUS_NOTI, true);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_on_status), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.fr_setting__iv_scren_free_wifi:
                if (isOnWifiFree) {
                    isOnWifiFree = false;
                    ivFreeWifi.setImageResource(R.drawable.ic_setting_swith_off);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_WIFI_FREE, false);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_off_free), Toast.LENGTH_SHORT).show();
                } else {
                    isOnWifiFree = true;
                    ivFreeWifi.setImageResource(R.drawable.ic_setting_swith_on);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_WIFI_FREE, true);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_on_free), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.fr_setting__iv_scren_look:
                if (isOnScrenLock) {
                    isOnScrenLock = false;
                    ivScrenlock.setImageResource(R.drawable.ic_setting_swith_off);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_SCREEN_LOOK, false);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_off_screen), Toast.LENGTH_SHORT).show();
                } else {
                    isOnScrenLock = true;
                    ivScrenlock.setImageResource(R.drawable.ic_setting_swith_on);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_SCREEN_LOOK, true);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_on_screen), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.fr_setting__iv_scren_mobie_data:
                if (isOnMobiData) {
                    isOnMobiData = false;
                    ivMobiData.setImageResource(R.drawable.ic_setting_swith_off);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_MOBI_DATA, false);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_off_mobie), Toast.LENGTH_SHORT).show();
                } else {
                    isOnMobiData = true;
                    ivMobiData.setImageResource(R.drawable.ic_setting_swith_on);
                    mPreferenceUtil.saveBoolean(AppConstant.IS_ON_MOBI_DATA, true);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.setting_turn_on_mobie), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.fr_setting_rating_google:
                initConfirm();
                
                break;
            default:
                break;

        }
    }
}
