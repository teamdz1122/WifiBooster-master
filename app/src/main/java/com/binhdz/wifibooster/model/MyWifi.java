package com.binhdz.wifibooster.model;

import java.io.Serializable;

/**
 * Created by admin on 1/6/2018.
 */

public class MyWifi implements Serializable {
    private String SSID, BSSID,securityType,password;
    private int level, frequency;
    private boolean isFree, isConnect;

    public MyWifi(String SSID, String BSSID, String securityType, int frequency, int level) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.securityType = securityType;
        this.frequency = frequency;
        this.level = level;
    }

    public MyWifi() {
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(boolean free) {
        isFree = free;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setIsConnect(boolean connect) {
        isConnect = connect;
    }
}
