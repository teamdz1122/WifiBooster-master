package com.binhdz.wifibooster.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 1/6/2018.
 */

public class ShowNotiWarningBootsReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context,ShowNotiBootNetwork.class);
        context.startService(mIntent);

    }
}
