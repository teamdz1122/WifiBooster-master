package com.binhdz.wifibooster.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2018.
 */

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<Fragment> arrFragment = new ArrayList<>();
    private final ArrayList<String> arrTitleFragment = new ArrayList<>();

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrFragment.size();
    }

    public void addFragment(Fragment fragment, String name) {
        arrFragment.add(fragment);
        arrTitleFragment.add(name);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrTitleFragment.get(position);
    }
}
