package com.binhdz.wifibooster.model;

/**
 * Created by admin on 3/29/2018.
 */

public class MenuModel {
    private String name;
    private int icon;
    private int badge;

    public MenuModel(String name, int icon, int badge) {
        this.name = name;
        this.icon = icon;
        this.badge = badge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }
}
