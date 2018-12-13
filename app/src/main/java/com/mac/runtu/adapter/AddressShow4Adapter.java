package com.mac.runtu.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mac.runtu.databasehelper.AddressDataInfo;

import java.util.ArrayList;

public class AddressShow4Adapter extends BaseAdapter {

    private Context context;

    private ArrayList<AddressDataInfo> mAddressInfoList;

    public AddressShow4Adapter(Context context, ArrayList<AddressDataInfo>
            mList) {
        this.context = context;
        mAddressInfoList = mList;
    }

    @Override
    public int getCount() {
        return mAddressInfoList.size();
    }

    @Override
    public AddressDataInfo getItem(int position) {
        return mAddressInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tv = null;

        if (convertView == null) {
            //MainActivity.this MainActivity对象
            tv = new TextView(context);
            tv.setSingleLine();
            tv.setTextSize(14);
        } else {
            tv = (TextView) convertView;
        }

        AddressDataInfo addressDataInfo = getItem(position);

        tv.setText(addressDataInfo.district_name);

        return tv;
    }
}