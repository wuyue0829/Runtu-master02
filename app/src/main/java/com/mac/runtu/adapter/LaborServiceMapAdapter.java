package com.mac.runtu.adapter;

import android.content.Context;

import com.mac.runtu.holder.BaseHolder;
import com.mac.runtu.javabean.LaborServiceDetailInfo;

import java.util.ArrayList;

/**
 * Description:
 * Copyright  : Copyright (c) 2016
 * Company    :
 * Author     : 牒倩楠
 * Date       : 2016/11/3 0003 上午 9:57
 */
public class LaborServiceMapAdapter extends
        MyBaseAdapter<LaborServiceDetailInfo> {
    public LaborServiceMapAdapter(Context context,
                                  ArrayList<LaborServiceDetailInfo> data) {
        super(context, data);
    }

    @Override
    public BaseHolder getHolder(Context context) {
        return new LaborServiceMapHolder(context);
    }
}
