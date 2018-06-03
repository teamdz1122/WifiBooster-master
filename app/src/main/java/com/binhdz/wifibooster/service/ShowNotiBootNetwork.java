package com.binhdz.wifibooster.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.binhdz.wifibooster.R;
import com.binhdz.wifibooster.activity.BootNetwork;

/**
 * Created by admin on 1/6/2018.
 */

public class ShowNotiBootNetwork extends Service {
    private RemoteViews bigViews;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent x, int flags, int startId) {

        bigViews = new RemoteViews(getPackageName(), R.layout.notifi_warning_bootsnet);
        Intent mIntent = new Intent(getApplicationContext(), BootNetwork.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent piContent = PendingIntent.getActivity(getApplicationContext(), 100, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        bigViews.setOnClickPendingIntent(R.id.layout_notification, piContent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, BootNetwork.class), 0);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_noti_warning)
                .setColor(Color.WHITE)
                .setCustomContentView(bigViews)
                .setDefaults(Notification.DEFAULT_SOUND);

        builder.mNotification.flags = NotificationCompat.FLAG_AUTO_CANCEL | NotificationCompat.DEFAULT_LIGHTS;
        builder.setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());

        return START_NOT_STICKY;
    }
}
