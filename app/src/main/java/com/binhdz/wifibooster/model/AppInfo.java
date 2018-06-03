package com.binhdz.wifibooster.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 1/5/2018.
 */

public class AppInfo implements Parcelable {
    private Drawable appIcon;
    private int uid;
    private String appName, packName, version;
    private boolean inRom, userApp, isCkeck;
    private long pkgSize, cacheSize;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public AppInfo() {
    }

    public boolean isCkeck() {
        return isCkeck;
    }

    public void setIsCkeck(boolean ckeck) {
        isCkeck = ckeck;
    }

    public AppInfo(Drawable appIcon, int uid, String appName, String packName, String version, boolean inRom, boolean userApp, long pkgSize, long cacheSize) {
        this.appIcon = appIcon;
        this.uid = uid;
        this.appName = appName;
        this.packName = packName;
        this.version = version;
        this.inRom = inRom;
        this.userApp = userApp;
        this.pkgSize = pkgSize;
        this.cacheSize = cacheSize;
        isCkeck = true;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    public long getPkgSize() {
        return pkgSize;
    }

    public void setPkgSize(long pkgSize) {
        this.pkgSize = pkgSize;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable((Parcelable) appIcon, 0);
        parcel.writeString(appName);
        parcel.writeString(packName);
        parcel.writeString(version);
        parcel.writeLong(pkgSize);
        parcel.writeLong(cacheSize);
        parcel.writeByte((byte) (inRom ? 1 : 0));
        parcel.writeByte((byte) (userApp ? 1 : 0));
    }

    public AppInfo(Parcel in) {
        appIcon = in.readParcelable(getClass().getClassLoader());
        appName = in.readString();
        packName = in.readString();
        version = in.readString();
        pkgSize = in.readLong();
        cacheSize = in.readLong();
        inRom = in.readByte() != 0;
        userApp = in.readByte() != 0;
    }
    public static final Parcelable.Creator<AppInfo> CREATOR = new Parcelable.Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
}
