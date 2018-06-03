package com.binhdz.wifibooster.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.RemoteViews;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.activity.MainActivity;
import com.binhdz.wifibooster.util.AppConstant;
import com.binhdz.wifibooster.util.Constants;
import com.binhdz.wifibooster.util.NetworkUtil;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 1/6/2018.
 */

public class FloatWindowService extends Service implements Constants {

    private Handler handler = new Handler();
    private Timer timer;
    private WifiManager mWifiManager;
    private NetworkStateReceiver mNetworkStateReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstant.ACTION_CLICK_NOTIFICATION);

        registerReceiver(mBroadcast, intentFilter);

        IntentFilter filterNetworkChange = new IntentFilter();
        filterNetworkChange.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filterNetworkChange.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mNetworkStateReceiver = new FloatWindowService.NetworkStateReceiver();
        registerReceiver(mNetworkStateReceiver, filterNetworkChange);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        showNotification(getApplicationContext());
        if (timer == null) {
            initData();
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0L, (long) TIME_SPAN);
        }

        return START_STICKY;
    }

    private BroadcastReceiver mBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case AppConstant.ACTION_CLICK_NOTIFICATION:
                    ClickNotification(FloatWindowService.this);
                    break;

            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop Service
        timer.cancel();
        timer = null;
        sendBroadcast(new Intent("YouWillNeverKillMe"));
        if (mBroadcast != null) {
            unregisterReceiver(mBroadcast);
        }

        if (mNetworkStateReceiver != null) {
            unregisterReceiver(mNetworkStateReceiver);
        }
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateViewData(getApplicationContext());

                }
            });
        }


    }


    //Notification
    public static final int STATUS_BAR_NOTIFICATION = 10000;
    RemoteViews views;
    RemoteViews bigViews;
    Notification status;

    private NotificationManager notificationmanager;
    Notification status1;

    private void updateSpeedBar(Context context, String speed) {

               /* status1 = new NotificationCompat.Builder(context)
                .setAutoCancel(true)
                .setSmallIcon( getResources().getIdentifier(speed,"drawable",getPackageName()))
                .setContent(bigViews)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
             bigViews.setImageViewResource(R.id.status_bar_icon,
                R.drawable.ic_wifi_noti);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            status1 = new NotificationCompat.Builder(context)
                    .setAutoCancel(true)
                    .setSmallIcon(getResources().getIdentifier(speed, "drawable", getPackageName()))
                    .setContent(bigViews)
                    .setOngoing(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();

        } else {
            status1.icon = getResources().getIdentifier(speed, "drawable", getPackageName());
        }


        notificationmanager.notify(STATUS_BAR_NOTIFICATION, status1);
    }

    private void showNotification(Context context) {

        try {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                bigViews = new RemoteViews(getPackageName(), R.layout.notifi_bar_1);
            }else {
                bigViews = new RemoteViews(getPackageName(), R.layout.notifi_bar);
            }
            bigViews.setTextViewText(R.id.bar_noti__tv_name_wifi, "Not Wi-Fi");
            bigViews.setTextViewText(R.id.bar_noti__tv_connect, "Wi-Fi Connected");
            bigViews.setTextViewText(R.id.bar_noti__tv_speed_down, "0Kb");
            bigViews.setTextViewText(R.id.bar_noti__tv_speed_up, "0Kb");

            //set lick notify
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent piContent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            bigViews.setOnClickPendingIntent(R.id.layout_notification, piContent);

            status1 = new NotificationCompat.Builder(context)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.wifi_icon)
                    .setContent(bigViews)
                    .setOngoing(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();
/*

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            status1.bigContentView = bigViews;
        }
*/

            notificationmanager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);
            notificationmanager.notify(STATUS_BAR_NOTIFICATION, status1);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// get speed

    private long rxtxTotal = 0;
    private long mobileRecvSum = 0;
    private long mobileSendSum = 0;
    private long wlanRecvSum = 0;
    private long wlanSendSum = 0;
    private long exitTime = 0;
    private DecimalFormat showFloatFormat = new DecimalFormat("0");

    public void initData() {
        mobileRecvSum = TrafficStats.getMobileRxBytes();
        mobileSendSum = TrafficStats.getMobileTxBytes();
        wlanRecvSum = TrafficStats.getTotalRxBytes() - mobileRecvSum;
        wlanSendSum = TrafficStats.getTotalTxBytes() - mobileSendSum;
        rxtxTotal = TrafficStats.getTotalRxBytes()
                + TrafficStats.getTotalTxBytes();
    }

    private boolean isNotNetwork;

    public void updateViewData(Context context) {
        if (!isNotNetwork) {

            long tempSum = TrafficStats.getTotalRxBytes()
                    + TrafficStats.getTotalTxBytes();
            long rxtxLast = tempSum - rxtxTotal;
            double totalSpeed = rxtxLast * 1000 / TIME_SPAN;
            rxtxTotal = tempSum;
            long tempMobileRx = TrafficStats.getMobileRxBytes();
            long tempMobileTx = TrafficStats.getMobileTxBytes();
            long tempWlanRx = TrafficStats.getTotalRxBytes() - tempMobileRx;
            long tempWlanTx = TrafficStats.getTotalTxBytes() - tempMobileTx;
            long mobileLastRecv = tempMobileRx - mobileRecvSum;
            long mobileLastSend = tempMobileTx - mobileSendSum;
            long wlanLastRecv = tempWlanRx - wlanRecvSum;
            long wlanLastSend = tempWlanTx - wlanSendSum;
            double mobileRecvSpeed = mobileLastRecv * 1000 / TIME_SPAN;
            double mobileSendSpeed = mobileLastSend * 1000 / TIME_SPAN;
            double wlanRecvSpeed = wlanLastRecv * 1000 / TIME_SPAN;
            double wlanSendSpeed = wlanLastSend * 1000 / TIME_SPAN;
            mobileRecvSum = tempMobileRx;
            mobileSendSum = tempMobileTx;
            wlanRecvSum = tempWlanRx;
            wlanSendSum = tempWlanTx;


// send speed up

            Intent intentSpeedUp = new Intent();
            intentSpeedUp.setAction(AppConstant.ACTION_UPDATE_SPEED_UP);
            if (wlanSendSpeed >= 0d && wlanSendSpeed > mobileSendSpeed) {

                intentSpeedUp.putExtra(AppConstant.VALUE_SPEED_UP, wlanSendSpeed);
                bigViews.setTextViewText(R.id.bar_noti__tv_speed_up, showSpeedNumber(wlanSendSpeed));

            } else if (mobileSendSpeed >= 0d && wlanSendSpeed < mobileSendSpeed) {
                intentSpeedUp.putExtra(AppConstant.VALUE_SPEED_UP, mobileSendSpeed);
                bigViews.setTextViewText(R.id.bar_noti__tv_speed_up, showSpeedNumber(mobileSendSpeed));

            }
            sendBroadcast(intentSpeedUp);

            // send speed down
            Intent intentSpeedDown = new Intent();
            intentSpeedDown.setAction(AppConstant.ACTION_UPDATE_SPEED_DOWN);
            if (wlanRecvSpeed >= 0d && wlanRecvSpeed > mobileRecvSpeed) {

                intentSpeedDown.putExtra(AppConstant.VALUE_SPEED_DOWN, wlanRecvSpeed);
                bigViews.setTextViewText(R.id.bar_noti__tv_speed_down, showSpeedNumber(wlanRecvSpeed));

                if (Math.round(wlanRecvSpeed / 1024d) < 1) {

                    updateSpeedBar(getApplicationContext(), "ickb0");

                } else {

                    updateSpeedBar(getApplicationContext(), showSpeed(wlanRecvSpeed));

                }


            } else if (mobileRecvSpeed >= 0d && wlanRecvSpeed < mobileRecvSpeed) {

                intentSpeedDown.putExtra(AppConstant.VALUE_SPEED_DOWN, mobileRecvSpeed);
                bigViews.setTextViewText(R.id.bar_noti__tv_speed_down, showSpeedNumber(mobileRecvSpeed));
                updateSpeedBar(getApplicationContext(), showSpeed(mobileRecvSpeed));
            }
            sendBroadcast(intentSpeedDown);
        } else {
            bigViews.setTextViewText(R.id.bar_noti__tv_speed_up, "0Kb");
            bigViews.setTextViewText(R.id.bar_noti__tv_speed_down, "0Kb");
            updateSpeedBar(getApplicationContext(), "wifi_icon");
        }
    }

    private DecimalFormat showFloatFormatNoti = new DecimalFormat("0.0");
    private DecimalFormat byteFloatFormatNoti = new DecimalFormat("0");

    private String showSpeedNumber(double speed) {
        String speedString;
        if (speed >= 1048576d) {
            speedString = showFloatFormatNoti.format(speed / 1048576d) + "MB/s";
        } else if (speed >= 1024d) {
            speedString = showFloatFormatNoti.format(speed / 1024d) + "KB/s";
        } else {
            speedString = byteFloatFormatNoti.format(speed) + "B/s";
        }
        return speedString;
    }

    private String showSpeed(double speed) {
        String speedString = "ickb0";
        if (speed >= 1048576d) {
            speed = speed / 1048576d;
            if (speed <= 10) {
                speed = speed * 10;
                speedString = "icmb" + showFloatFormat.format(speed);
            } else if (speed <= 200 && speed > 10) {
                speed = 100 + speed - 10;
                speedString = "icmb" + showFloatFormat.format(speed);
            } else {
                speedString = "icmb291";
            }

        } else {
            speedString = "ickb" + showFloatFormat.format(Math.round(speed / 1024d));
        }
        return speedString;
    }


    private void ClickNotification(Context context) {
        try {

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {

        }
    }


    class NetworkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = NetworkUtil.getConnectivityStatus(context);
            switch (state) {
                case NetworkUtil.TYPE_MOBILE:

                    String typeMobie = getNetworkType(FloatWindowService.this);
                    bigViews.setTextViewText(R.id.bar_noti__tv_connect, "Mobie Data");
                    bigViews.setTextViewText(R.id.bar_noti__tv_name_wifi, typeMobie);
                    isNotNetwork = false;
                    break;
                case NetworkUtil.TYPE_NOT_CONNECTED:
                    bigViews.setTextViewText(R.id.bar_noti__tv_connect, "Open Network");
                    bigViews.setTextViewText(R.id.bar_noti__tv_name_wifi, "Not Wi-Fi Connected");

                    isNotNetwork = true;
                    break;

                case NetworkUtil.TYPE_WIFI:
                    WifiInfo mWifiConnect = mWifiManager.getConnectionInfo();
                    String nameWifi = mWifiConnect.getSSID();

                    bigViews.setTextViewText(R.id.bar_noti__tv_connect, "Wi-Fi Connected");
                    bigViews.setTextViewText(R.id.bar_noti__tv_name_wifi, nameWifi.substring(1, nameWifi.length() - 1));
                    isNotNetwork = false;
                    break;
            }

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

}
