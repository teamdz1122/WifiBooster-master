package com.binhdz.wifibooster.util;

import android.net.wifi.ScanResult;

/**
 * Created by admin on 1/6/2018.
 */

public class CommomUtil {
    public static String getWifiSecurityType(ScanResult result) {
        if (result.capabilities.contains("WEP")) {
            return AppConstant.SECURITY_WEP;
        } else if (result.capabilities.contains("WPA")) {
            return AppConstant.SECURITY_WPA;
        } else if (result.capabilities.contains("PSK")) {
            return AppConstant.SECURITY_PSK;
        } else if (result.capabilities.contains("EAP")) {
            return AppConstant.SECURITY_EAP;
        }
        return AppConstant.SECURITY_NONE;
    }
    public static int getLevelWifi(int level) {
        if (Math.abs(level) > 100) {

            return 1;

        } else if (Math.abs(level) > 80) {

            return 2;

        } else if (Math.abs(level) > 70) {

            return 2;

        } else if (Math.abs(level) > 60) {

            return 3;

        } else if (Math.abs(level) > 50) {

            return 3;

        } else {

            return 4;
        }
    }

    public static String getStr(String s) {
        StringBuilder str = new StringBuilder(s);
        str.deleteCharAt(s.length()-1);
        str.deleteCharAt(0);
        return str.toString();
    }
}
