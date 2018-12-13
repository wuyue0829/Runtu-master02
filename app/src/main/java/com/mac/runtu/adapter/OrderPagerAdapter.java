package com.mac.runtu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mac.runtu.utils.FragmentFactory;
import com.mac.runtu.utils.LogUtils;


public class OrderPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "OrderPagerAdapter";

    private String[] name = {"全部订单", "待付款", "待发货", "待收货", "已完成"};

    public OrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return FragmentFactory.getOrderFragment(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return name.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        //return name[position];
        LogUtils.e(TAG, "position=" + position);

        return null;
    }
}