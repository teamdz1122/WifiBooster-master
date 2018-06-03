package com.binhdz.wifibooster.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.adapter.MenuLeftAdapter;
import com.binhdz.wifibooster.adapter.MyViewPagerAdapter;
import com.binhdz.wifibooster.callback.ChangeTabViewpager;
import com.binhdz.wifibooster.fragment.HomeFragmnet;
import com.binhdz.wifibooster.fragment.ListWifiFragment;
import com.binhdz.wifibooster.fragment.SettingFragment;
import com.binhdz.wifibooster.model.MenuModel;
import com.binhdz.wifibooster.util.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ChangeTabViewpager, AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int LEVEL_BACK = 1;
    private static final int LEVEL_MENU = 0;

    List<MenuModel> lstMdl = new ArrayList<>();
    Camera.Parameters params;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int tabCurent;
    private boolean isLogin = true;
    private ImageView imageMenu;
    private ToggleButton ivOnOff;
    private DrawerLayout drawerLayout;
    private RelativeLayout content;
    private View.OnClickListener onClickDrawer;
    private ListView lvLeft;
    private LinearLayout headerMenu;
    private RelativeLayout headerHome;
    private boolean isLighOn = false;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private InterstitialAd mInterstitialAd;
    private View.OnClickListener headerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openCloseDrawer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ckeckPermission();
        requestPermissions();
        initViews();

    }

    public void closeDrawer() {
        Utils.expand(headerHome);
    }

    public void openDrawer() {
        Utils.collapse(headerHome);
    }

    public void setOpenDrawer() {
        if (this != null) this.openDrawer();
    }

    public void setCloseDrawer() {
        if (this != null) this.closeDrawer();

    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissionGranted(Manifest.permission.CAMERA)) {
                //TODO


            } else {
                String[] permission = new String[]{
                        Manifest.permission.CAMERA
                };
                requestPermissions(permission, REQUEST_CODE_PERMISSION);
            }
        } else {
            //TODO

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissionGranted(String permission) {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO


            } else {
                Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


/*
    private void setUpService() {
        preUtil = PreferenceUtil.getSingleton(getApplicationContext());
        intentStartService = new Intent(MainActivity.this, FloatWindowService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.permission_alert), Toast.LENGTH_LONG).show();
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(permissionIntent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            startService(intentStartService);
        }
    }*/

 //   private void ckeckPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Check Permissions Now
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_P);
//        }
//    }

    @SuppressLint({"WrongViewCast", "CutPasteId"})
    private void initViews() {

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-8694555122491369/5626855412");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//
//        });
      /*  mToolbar = (LinearLayout) findViewById(R.id.tbl_home);*/
        mViewPager = (ViewPager) findViewById(R.id.vpg_home);
        mTabLayout = (TabLayout) findViewById(R.id.tab_home);
        imageMenu = (ImageView) findViewById(R.id.view_top__iv_icon);
        imageMenu.setOnClickListener(onClickDrawer);
        imageMenu.setOnClickListener(this);
        lvLeft = (ListView) findViewById(R.id.lv_menu);
        headerMenu = (LinearLayout) findViewById(R.id.header);
        headerMenu.setOnClickListener(headerClick);
        headerHome = (RelativeLayout) findViewById(R.id.hom__view_top);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        content = (RelativeLayout) findViewById(R.id.content);
        ivOnOff= (ToggleButton) findViewById(R.id.iv_on_off);
        ivOnOff.setOnClickListener(this);
        ivOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {

                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(false);
                } else {

                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifi.setWifiEnabled(true);
                }
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.stop) {
            private float scaleFactor = 6f;

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                content.setTranslationX(slideX);
                content.setScaleX(1 - (slideOffset / scaleFactor));
                content.setScaleY(1 - (slideOffset / scaleFactor));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
              //  imageMenu.setImageLevel(LEVEL_BACK);
//                content.setBackgroundDrawable(getResources().getDrawable(R.drawable.custom_button));
                setOpenDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
               // imageMenu.setImageLevel(LEVEL_MENU);

                setCloseDrawer();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);


        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setDrawerElevation(0f);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        HomeFragmnet mHomeFragmnet = new HomeFragmnet();
        mHomeFragmnet.setCallBack(this);
        myViewPagerAdapter.addFragment(mHomeFragmnet, "Home");
        myViewPagerAdapter.addFragment(new ListWifiFragment(), "WiFi Expert");
        myViewPagerAdapter.addFragment(new SettingFragment(), "Setting");
        mViewPager.setAdapter(myViewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        setUpTab(0);
        setTabCurrent(0);
        mTabLayout.setOnTabSelectedListener(this);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setScrollPosition(position, 0, true);
                mTabLayout.setSelected(true);
                tabCurent = position;
                mTabLayout.setupWithViewPager(mViewPager);

                setUpTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ListView lvMenu = (ListView) findViewById(R.id.lv_menu);
        lstMdl.add(new MenuModel(getResources().getString(R.string.language), R.drawable.ic_language_24dp, 0));
        lstMdl.add(new MenuModel(getResources().getString(R.string.about), R.drawable.ic_information_ic, 0));
        lstMdl.add(new MenuModel(getResources().getString(R.string.flash_mode), R.drawable.ic_highlight_24dp, 0));
        lstMdl.add(new MenuModel(getResources().getString(R.string.setting_rating_google), R.drawable.ic_stars_24dp, 0));


        MenuLeftAdapter adapter = new MenuLeftAdapter(lstMdl, this);
        lvMenu.setAdapter(adapter);
        lvMenu.setOnItemClickListener(this);

        //https://analytics.google.com
        // SendGoogleAnalytics.getInstance().sendActivity(this);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MenuModel mdl = lstMdl.get(i);
        if (mdl.getName().equals(getResources().getString(R.string.language))) {
//            if (mInterstitialAd.isLoaded()) {
//                mInterstitialAd.show();
//
//            } else {
//                Log.d("TAG", "The interstitial wasn't loaded yet.");
//            }

            Intent intent = new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);

            openCloseDrawer();
        } else if (mdl.getName().equals(getResources().getString(R.string.setting_rating_google))) {


            Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
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
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));

            }
            openCloseDrawer();
        } else if (mdl.getName().equals(getResources().getString(R.string.flash_mode))) {
            if (isFlashOn) {
                // turn off flash
                turnOffFlash();
            } else {
                // turn on flash
                turnOnFlash();
            }
        }

    }

    public void openCloseDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START, false);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }


//    private void setFragment(Fragment fragment) {
//        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//        t.replace(R.id.content, fragment);
//        t.commit();
//    }

    public void setOnClickImgTitle(View.OnClickListener onClickDrawer) {
        this.onClickDrawer = onClickDrawer;
    }

    private void setTabCurrent(int tabCurrent) {
        mViewPager.setCurrentItem(tabCurrent);
    }

  /*  private void setDefaultToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);

    }*/

    private void setUpTab(int tabCurrenr) {

        TextView tabOne, tabTwo, tabThree;

        switch (tabCurrenr) {
            case 0:

                tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_on, null);
                tabOne.setText(R.string.tab_home);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_on, 0, 0);
                mTabLayout.getTabAt(0).setCustomView(tabOne);

                tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_off, null);
                tabTwo.setText(R.string.tab_wifi_expert);
                tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_list_wifi_off, 0, 0);
                mTabLayout.getTabAt(1).setCustomView(tabTwo);

                tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_off, null);
                tabThree.setText(R.string.tab_setting);
                tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_setting_bar_off, 0, 0);
                mTabLayout.getTabAt(2).setCustomView(tabThree);
                break;

            case 1:

                tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_off, null);
                tabOne.setText(R.string.tab_home);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_off, 0, 0);
                mTabLayout.getTabAt(0).setCustomView(tabOne);

                tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_on, null);
                tabTwo.setText(R.string.tab_wifi_expert);
                tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_list_wifi_on, 0, 0);
                mTabLayout.getTabAt(1).setCustomView(tabTwo);

                tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_off, null);
                tabThree.setText(R.string.tab_setting);
                tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_setting_bar_off, 0, 0);
                mTabLayout.getTabAt(2).setCustomView(tabThree);
                break;

            case 2:

                tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_off, null);
                tabOne.setText(R.string.tab_home);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_off, 0, 0);
                mTabLayout.getTabAt(0).setCustomView(tabOne);

                tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_off, null);
                tabTwo.setText(R.string.tab_wifi_expert);
                tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_list_wifi_off, 0, 0);
                mTabLayout.getTabAt(1).setCustomView(tabTwo);

                tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_on, null);
                tabThree.setText(R.string.tab_setting);
                tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_setting_bar_on, 0, 0);
                mTabLayout.getTabAt(2).setCustomView(tabThree);
                break;
        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tabCurent);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void changeTab(int tabCurrent) {
        setTabCurrent(tabCurrent);
    }

    // getting camera parameters

    @SuppressLint("LongLogTag")
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;

        }

    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();

        // on pause turn off the flash
        turnOffFlash();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();

        // on resume turn on the flash
        if (hasFlash)
            turnOnFlash();
    }

    @Override
    public void onStart() {
        super.onStart();

        // on starting the app get the camera params
        getCamera();
    }

    @Override
    public void onStop() {
        super.onStop();

        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_top__iv_icon:

                if(drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START, false);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
                break;

        }
    }
}
