package com.binhdz.wifibooster.callback;

import android.net.wifi.ScanResult;

import java.util.ArrayList;

/**
 * Created by admin on 1/5/2018.
 */

public interface UpdateApdaterView {
    void updateIconConnect(ArrayList<ScanResult> arrWifi);
}
