package com.binhdz.wifibooster.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 1/6/2018.
 */

public class ShowNotiWarningCPUReciever  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context,ShowNotiWarringCPUService.class);
        context.startService(mIntent);

    }
}
