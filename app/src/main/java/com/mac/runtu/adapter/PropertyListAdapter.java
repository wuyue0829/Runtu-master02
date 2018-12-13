package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.holder.PropertyHolder;
import com.mac.runtu.javabean.PropertyDetailInfo;

import java.util.ArrayList;

/**
 * Description:产权流转适配器
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/10/25 0025 下午 5:03
 */
public class PropertyListAdapter extends MyBaseAdapter<PropertyDetailInfo> {

    public PropertyListAdapter(Context context, ArrayList<PropertyDetailInfo>
            data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new PropertyHolder(context);
    }
}
